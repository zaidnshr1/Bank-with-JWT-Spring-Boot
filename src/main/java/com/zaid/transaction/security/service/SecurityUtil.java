package com.zaid.transaction.security.service;

import com.zaid.transaction.model.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Profile getLoggedInProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("User not Authenticated or context not set.");
        }

        return (Profile) authentication.getPrincipal();
    }

    public Long getLoggedInProfileId() {
        return getLoggedInProfile().getId();
    }

}
