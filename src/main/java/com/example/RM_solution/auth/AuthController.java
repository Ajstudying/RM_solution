package com.example.RM_solution.auth;

import com.example.RM_solution.auth.util.HashUtil;
import com.example.RM_solution.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService service;
    @Autowired
    private HashUtil hash;
    @Autowired
    private JwtUtil jwt;

    @PostMapping(value = "signup")
    public ResponseEntity signUp(@RequestBody SignUpRequest req){
        System.out.println(req);

        if(req.getUser_id() == null || req.getUser_id().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getSecret() == null || req.getSecret().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        service.createIdentity(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    public ResponseEntity signin(
//            @RequestParam String user_id,
//            @RequestParam String password,
//            HttpServletResponse res){
//        System.out.println(user_id);
//
//
//    }

}
