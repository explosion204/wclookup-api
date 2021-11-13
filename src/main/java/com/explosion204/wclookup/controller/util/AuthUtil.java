package com.explosion204.wclookup.controller.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return authentication != null && authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }
}
