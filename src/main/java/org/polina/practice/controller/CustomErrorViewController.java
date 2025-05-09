package org.polina.practice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class CustomErrorViewController implements ErrorController {
    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        ServletWebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorMap = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());

        Integer status = (Integer) errorMap.get("status");

        model.addAttribute("status", status != null ? status : 500);

        if (status != null) {
            return switch (status) {
                case 401 -> "error-401";
                case 403 -> "error-403";
                case 400 -> "error-400";
                case 500 -> "error-500";
                default -> "error";
            };
        }
        return "error";
    }
}
