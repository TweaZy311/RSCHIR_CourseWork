package ru.sinforge.practice5.Service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VerifyServiceImpl {

    public boolean verifyRole(int sellerId) {
        Authentication auth = getAuth();
        String authority = "";
        List<? extends GrantedAuthority> authorities =  auth.getAuthorities().stream().toList();
        for (GrantedAuthority grantedAuthority : authorities) {
            authority = grantedAuthority.getAuthority();
        }
        return sellerId == Integer.parseInt(auth.getName()) || authority.equals("ADMIN");
    }
    public boolean verifyUserId(int userId) {
        Authentication auth = getAuth();
        return userId == Integer.parseInt(auth.getName());
    }
    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
