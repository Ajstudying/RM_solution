package com.example.RM_solution.auth.request;

import com.example.RM_solution.auth.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {

    private String username;
    private String password;
    private UserRole role;

}
