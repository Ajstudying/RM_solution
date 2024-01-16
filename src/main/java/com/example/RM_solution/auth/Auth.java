package com.example.RM_solution.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Auth {
//    public boolean require() default true;
    public UserRole role() default UserRole.USER; // 기본은 일반 사용자 권한

}

