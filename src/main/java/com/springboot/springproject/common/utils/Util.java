package com.springboot.springproject.common.utils;

import org.springframework.security.oauth2.jwt.Jwt;

public class Util {
    static public String emailMask(String email) {
        if (email == null || !email.contains("@")) return email;

        String[] parts = email.split("@", 2);
        String local = parts[0];
        String domain = parts[1];

        if (local.length() <= 3) {
            return "*".repeat(local.length()-1) + "@" + domain;
        }

        String maskedLocal = local.substring(0, local.length() - 3) + "***";
        return maskedLocal + "@" + domain;
    }
}
