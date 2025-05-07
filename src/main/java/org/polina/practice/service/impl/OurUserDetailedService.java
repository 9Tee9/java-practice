package org.polina.practice.service.impl;

import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.User;
import org.polina.practice.repository.UserRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OurUserDetailedService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        return user;
    }

    public void increaseFailedAttempts(User user) {
        int newFailedAttempts = user.getFailedAttempts() + 1;
        if (newFailedAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountNonLocked(false);
        }
        user.setFailedAttempts(newFailedAttempts);
        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }
    public void unlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setAccountNonLocked(true);
        user.setFailedAttempts(0);
        userRepository.save(user);
    }
}
