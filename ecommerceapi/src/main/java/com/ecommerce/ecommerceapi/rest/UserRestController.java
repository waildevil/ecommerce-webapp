package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.config.JwtService;
import com.ecommerce.ecommerceapi.dto.OrderDTO;
import com.ecommerce.ecommerceapi.dto.PasswordChangeDTO;
import com.ecommerce.ecommerceapi.dto.ProductDTO;
import com.ecommerce.ecommerceapi.dto.UserDTO;
import com.ecommerce.ecommerceapi.entity.Address;
import com.ecommerce.ecommerceapi.entity.Product;
import com.ecommerce.ecommerceapi.entity.Role;
import com.ecommerce.ecommerceapi.entity.User;
import com.ecommerce.ecommerceapi.service.AddressService;
import com.ecommerce.ecommerceapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private UserService userService;

    private AddressService addressService;

    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @Autowired
    public UserRestController(UserService theUserService, PasswordEncoder thePasswordEncoder, AddressService theAddressService, JwtService theJwtService) {
        userService = theUserService;
        passwordEncoder = thePasswordEncoder;
        addressService = theAddressService;
        jwtService = theJwtService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserWithOrders(@PathVariable Long userId) {
        try {

            UserDTO userDTO = userService.findUser(userId);
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO updatedUserDTO) {
        User updatedUser = userService.updateUser(userId, updatedUserDTO, passwordEncoder);
        UserDTO result = new UserDTO(updatedUser);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long userId,
                                            @RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            UserDTO updatedUser = userService.changePassword(
                    userId,
                    passwordChangeDTO.getCurrentPassword(),
                    passwordChangeDTO.getNewPassword(),
                    passwordEncoder
            );
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        boolean isDeleted = userService.deleteById(userId);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User marked as inactive - id: " + userId);
    }



    @GetMapping("/search")
    public List<UserDTO> searchUsers(
            @RequestParam String searchTerm,
            @RequestParam(required = false) String roleName) {
        Optional<String> roleNameOpt = Optional.ofNullable(roleName).filter(role -> !role.isEmpty());
        return userService.searchUsersByNameAndUsername(searchTerm, roleNameOpt);
    }


    @GetMapping("/roles")
    public ResponseEntity<Role[]> getRoles() {
        Role[] roles = Role.values();
        return ResponseEntity.ok(roles);
    }



}
