package com.example.RM_solution.auth.entity;

import com.example.RM_solution.auth.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private long id;
    private String username;
    private String secret;
    //역할 부여 일반회원/기업회원
    private UserRole role;
    // 초반 할당된 스토리지 용량
    private int totalStorage;
}
