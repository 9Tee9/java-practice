package org.polina.practice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.User;
import org.polina.practice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

        private final UserRepository userRepository;
        private static final Logger logger = LoggerFactory.getLogger(SocialAppService.class);
        @Value("${admin.email}")
        private String adminEmail;

        @Override
        @Transactional
        public OAuth2User loadUser(OAuth2UserRequest userRequest) {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            String login = oAuth2User.getAttribute("login");
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            User.Role role = email != null && email.equals(adminEmail) ? User.Role.ADMIN : User.Role.USER;

            User savedUser = userRepository.findById(login)
                    .orElseGet(() -> new User(login, name, email, role));

            if (!userRepository.existsById(login)) {
                savedUser = userRepository.save(savedUser);
            }
            logger.info("Successful authentication for user: {}", login);
            Collection<? extends GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + savedUser.getRole())
            );

            return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "login");
        }
}
