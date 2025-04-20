package org.example.comment.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.comment.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class TokenFilter implements Filter  {
    //初始化Logger
    private static final Logger Logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        Logger.debug("初始化过滤器");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)throws IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //拦截请求并获取token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("Unauthorized");
            Logger.debug("请求头为空或者请求头开头没有加上Bearer前缀");
            return;
        }
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Logger.debug("成功解析JWT令牌");
            //解析token是否过期
            Date expiration = claims.getExpiration();
            if (claims.getExpiration().before(new Date())) {
                //解析refeshToken是否过期
                String refreshToken = request.getHeader("refreshToken");
                try {
                    if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
                        response.setStatus(401);
                        response.getWriter().write("Unauthorized");
                        Logger.debug("refreshToken为空或者refreshToken开头没有加上Bearer前缀");
                        return;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Claims claims1 = JwtUtils.parseJWT(refreshToken);
                    Logger.debug("成功解析refreshToken");
                    Calendar calendar = Calendar.getInstance();
                    //记录当前时间
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, -1); // 减去一分钟
                    Date oneMinuteAgo = calendar.getTime();
                    //解析refreshToken是否过期
                    if (claims1.getExpiration().before(oneMinuteAgo) ){
                        response.setStatus(401);
                        response.getWriter().write("Unauthorized");
                        Logger.debug("refreshToken已过期");
                        return;
                    } else {
                        //刷新token
                        String newToken = JwtUtils.generateJwt(claims);
                        response.setHeader("Authorization", newToken);
                        response.setHeader("refreshToken", refreshToken);
                        Logger.debug("刷新token成功");
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                }
                catch (Exception e) {
                    response.setStatus(401);
                    response.getWriter().write("Unauthorized");
                    Logger.debug("refreshToken解析失败");
                    return;
                }

            }
            else {
                //放行请求
                filterChain.doFilter(servletRequest, servletResponse);
            }

        }
        catch (Exception e) {
            response.setStatus(401);
            response.getWriter().write("Unauthorized");
            Logger.debug("JWT解析失败");
        }

    }

    @Override
    public void destroy() {
        Logger.debug("销毁过滤器");
    }
}
