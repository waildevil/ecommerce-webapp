package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.entity.Login;
import com.ecommerce.ecommerceapi.entity.Register;

public interface AuthService {

    String register(Register register);
    String login(Login login);
}
