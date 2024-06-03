/*package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.RoleRepository;
import com.ecommerce.ecommerceapi.dao.UserRepository;
import com.ecommerce.ecommerceapi.entity.Login;
import com.ecommerce.ecommerceapi.entity.Register;
import com.ecommerce.ecommerceapi.entity.Role;
import com.ecommerce.ecommerceapi.entity.User;
import com.ecommerce.ecommerceapi.exception.ProductAPIException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    @Override
    public String register(Register register) {

        if(userRepository.existsByUsername(register.getUsername())){
            throw new ProductAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        if(userRepository.existsByEmail(register.getEmail())){
            throw new ProductAPIException(HttpStatus.BAD_REQUEST,"Email already exists!");
        }

        User user = new User();
        user.setUsername(register.getUsername());
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);

        return "User Registered Successfully!";
    }

    @Override
    public String login(Login login) {

        Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getUsernameOrEmail(),
                login.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "User logged in Successfully!";
    }
}

*/







