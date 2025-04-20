package org.example.comment.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.example.comment.filter.TokenFilter;
import org.springframework.web.WebApplicationInitializer;

import java.util.EnumSet;

public class WebAppInitializer implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 定义常量，避免硬编码
        final String TOKEN_FILTER_NAME = "tokenFilter";
        final String COMMENT_URL_PATTERN = "/post/*";

        try {
            // 注册过滤器，并明确指定 DispatcherType
            FilterRegistration.Dynamic tokenFilter = servletContext.addFilter(TOKEN_FILTER_NAME, new TokenFilter());
            if (tokenFilter == null) {
                throw new ServletException("Failed to register filter: " + TOKEN_FILTER_NAME);
            }

            // 使用明确的 DispatcherType，避免不必要的过滤器应用
            EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
            tokenFilter.addMappingForUrlPatterns(dispatcherTypes, true, COMMENT_URL_PATTERN);

            // 日志记录，确认过滤器注册成功
            System.out.println("TokenFilter registered successfully for URL pattern: " + COMMENT_URL_PATTERN);
        } catch (Exception e) {
            // 捕获异常并记录日志
            System.err.println("Error occurred while registering TokenFilter: " + e.getMessage());
            throw new ServletException("Error during filter registration", e);
        }
    }

}

