package org.example.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
            // 处理 OAuth2 用户（包括 GitHub）
            org.springframework.security.oauth2.core.user.DefaultOAuth2User oauth2User =
                    (org.springframework.security.oauth2.core.user.DefaultOAuth2User) authentication.getPrincipal();

            String userName = oauth2User.getAttribute("login"); // 或 "login"（GitHub 的用户名字段）
            Map<String, Object> userAttributes = oauth2User.getAttributes();

            // 返回 JSON 响应
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"success\", \"user\":\"" + userName + "\", \"attributes\":" + objectMapper.writeValueAsString(userAttributes) + "}");
        } else if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser) {
            // 处理 OIDC 用户（适用于 Google 等）
            org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser oidcUser =
                    (org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser) authentication.getPrincipal();

            String userName = oidcUser.getFullName(); // 或 "name"
            Map<String, Object> userAttributes = oidcUser.getClaims();

            // 返回 JSON 响应
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"success\", \"user\":\"" + userName + "\", \"attributes\":" + objectMapper.writeValueAsString(userAttributes) + "}");
        } else {
            // 处理其他类型的 Authentication
            response.sendRedirect("/index.html");
        }
    }

}