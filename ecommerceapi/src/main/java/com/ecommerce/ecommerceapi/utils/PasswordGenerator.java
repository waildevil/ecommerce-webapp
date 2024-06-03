package com.ecommerce.ecommerceapi.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordGenerator {


        private static final SecureRandom random = new SecureRandom();

        public static String generateRandomPassword( int length){
            byte[] bytes = new byte[length];
            random.nextBytes(bytes);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        }
    }

