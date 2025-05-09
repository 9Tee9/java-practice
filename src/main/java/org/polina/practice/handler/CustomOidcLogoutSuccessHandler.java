package org.polina.practice.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomOidcLogoutSuccessHandler implements LogoutSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomOidcLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("User logged out: {}", authentication.getName());
        }
        response.sendRedirect("/");
    }
}
