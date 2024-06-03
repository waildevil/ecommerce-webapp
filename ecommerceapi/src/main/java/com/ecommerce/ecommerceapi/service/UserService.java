package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.UserDTO;
import com.ecommerce.ecommerceapi.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;


public interface UserService{

    List<UserDTO> findAll();
    UserDTO findUser(Long theId);
    User getUserEntityById(Long userId);
    User createUser(UserDTO userDTO);
    User updateUser(Long userId, UserDTO updatedUserDTO, PasswordEncoder passwordEncoder);
    UserDTO changePassword(Long userId, String currentPassword, String newPassword, PasswordEncoder passwordEncoder);
    boolean deleteById(Long userId);

    List<UserDTO> searchUsersByNameAndUsername(String searchTerm, Optional<String> roleName);
    User findByUsername(String username);

}
