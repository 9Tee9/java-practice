package org.polina.practice.listener;

import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.User;
import org.polina.practice.service.impl.OurUserDetailedService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final OurUserDetailedService ourUserDetailedService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        try {
            UserDetails userDetails = ourUserDetailedService.loadUserByUsername(username);
            ourUserDetailedService.increaseFailedAttempts((User) userDetails);
        } catch (UsernameNotFoundException ignored) {}
    }
}
