package com.ecommerce.ecommerceapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Register {

    private String name;
    private String username;
    private String email;
    private String password;
}
