package com.example.RM_solution.auth;

import com.example.RM_solution.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            Auth auth = method.getAnnotation(Auth.class);

            if(auth == null){
                return true;
            }

            String token = request.getHeader("Authorization");
            if(token == null || token.isEmpty()){
                response.setStatus(401);
                return false;
            }
            //토큰 검증
            AuthUser user = jwt.validateToken(token.replace("Bearer ", ""));
            if(user == null){
                response.setStatus(401);
                return false;
            }
            //권한 체크 (기업회원인지 일반회원인지 체크!)
            UserRole requiredRole = auth.value();
            if (!user.getRole().equals(requiredRole)) {
                // 권한이 없는 경우
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            request.setAttribute("authUser", user);
            return true;

        }
        return true;
    }

}