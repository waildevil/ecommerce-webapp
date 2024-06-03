package com.ecommerce.ecommerceapi.dto;

import com.ecommerce.ecommerceapi.entity.Role;
import com.ecommerce.ecommerceapi.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.RoleInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String password;
    private boolean active;
    private String role;
    private List<OrderDTO> orders = new ArrayList<>();
    private List<AddressDTO> addresses = new ArrayList<>();

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.active = user.isActive();
        this.role = user.getRole() != null ? user.getRole().name() : "No Role Assigned";


        this.orders = user.getOrders().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());

        this.addresses = user.getAddresses().stream()
                .map(AddressDTO::new)
                .collect(Collectors.toList());

    }

}
