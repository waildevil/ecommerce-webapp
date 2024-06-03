package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.AddressRepository;
import com.ecommerce.ecommerceapi.dao.UserRepository;
import com.ecommerce.ecommerceapi.dto.AddressDTO;
import com.ecommerce.ecommerceapi.dto.OrderDTO;
import com.ecommerce.ecommerceapi.dto.UserDTO;
import com.ecommerce.ecommerceapi.entity.Role;
import com.ecommerce.ecommerceapi.entity.User;
import com.ecommerce.ecommerceapi.utils.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{


    private UserRepository userRepository;
    private AddressRepository addressRepository;

    private PasswordEncoder passwordEncoder;
    private EmailService emailService;


    public UserServiceImpl(UserRepository theUserRepository, AddressRepository theAddressRepository,
                           PasswordEncoder thePasswordEncoder,
                           EmailService theEmailService){

        userRepository = theUserRepository;
        addressRepository = theAddressRepository;

        passwordEncoder = thePasswordEncoder;
        emailService = theEmailService;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + userId));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }


    @Override
    public UserDTO findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));


        UserDTO userDTO = new UserDTO(user);



        List<OrderDTO> orderDTOs = user.getOrders().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());


        List<AddressDTO> addressDTOS = user.getAddresses().stream()
                .map(AddressDTO::new)
                .collect(Collectors.toList());

        userDTO.setOrders(orderDTOs);
        userDTO.setAddresses(addressDTOS);

        return userDTO;
    }



    public User createUser(UserDTO userDTO) {

        if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        Role role;
        try {
            role = Role.valueOf(userDTO.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + userDTO.getRole());
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setName(userDTO.getName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setActive(userDTO.isActive());

        String randomPassword = PasswordGenerator.generateRandomPassword(12);
        String hashedPassword = passwordEncoder.encode(randomPassword);
        newUser.setPassword(hashedPassword);

        newUser.setRole(role);

        emailService.sendSimpleMessage(userDTO.getEmail(), "Your New Account Password",
                "Your temporary password is: " + randomPassword);

        return userRepository.save(newUser);

    }

    @Override
    public User updateUser(Long userId, UserDTO updatedUserDTO, PasswordEncoder passwordEncoder) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));


        existingUser.setName(updatedUserDTO.getName());
        existingUser.setEmail(updatedUserDTO.getEmail());
        existingUser.setUsername(updatedUserDTO.getUsername());
        if (existingUser.isActive() != updatedUserDTO.isActive()) {
            existingUser.setActive(updatedUserDTO.isActive());
        }
        existingUser.setRole(Role.valueOf(updatedUserDTO.getRole()));
        return userRepository.save(existingUser);
    }


    public UserDTO changePassword(Long userId, String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedNewPassword);
        User updatedUser = userRepository.save(user);

        return new UserDTO(updatedUser);
    }


    @Transactional
    public boolean deleteById(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setActive(false);
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }


    public List<UserDTO> searchUsersByNameAndUsername(String searchTerm, Optional<String> roleNameOpt) {
        List<User> users;
        if (roleNameOpt.isPresent() && !roleNameOpt.get().isEmpty()) {
            Role role = Role.valueOf(roleNameOpt.get().toUpperCase());
            users = userRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseAndRole(searchTerm, searchTerm, role);
        } else {
            users = userRepository.findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(searchTerm, searchTerm);
        }
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

}
