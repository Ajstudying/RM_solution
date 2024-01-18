package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import com.example.RM_solution.auth.request.SignUpRequest;
import com.example.RM_solution.auth.util.HashUtil;
import com.example.RM_solution.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        System.out.println(req.getUsername());
        //데이터 무결성
        if(req.getUsername() == null || req.getUsername().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getPassword() == null || req.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getRole() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //유저 생성
        boolean result = service.createIdentity(req);
        if(result){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
//    @PostMapping(value = "signup/company")
//    public ResponseEntity CompanySignUp(@RequestBody CompanySignUpRequest companyReq){
//        System.out.println(companyReq);
//        //데이터 무결성
//        if(companyReq.getUsername() == null || companyReq.getUsername().isEmpty()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        if(companyReq.getPassword() == null || companyReq.getPassword().isEmpty()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        if(companyReq.getRole() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        //유저 생성
//        boolean result = service.createIdentity(req);
//
//        if(result){
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//        }else {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//
//    }

    @PostMapping(value = "signin")
    public ResponseEntity signin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse res){
        System.out.println(username);

        User findedUser = service.findUser(username);
        if(findedUser == null){
            System.out.println("유저가 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean isVerified = hash.verifyHash(password, findedUser.getSecret());

        if(!isVerified){
            System.out.println("비밀번호 오류");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String token = jwt.createToken(findedUser.getId(),
                findedUser.getUsername(), findedUser.getRole());
        System.out.println(token);

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge((int)(jwt.TOKEN_TIMEOUT/1000));
        cookie.setDomain("localhost");

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(ServletUriComponentsBuilder
                        .fromHttpUrl("http://localhost:5500").build().toUri())
                .build();
    }

}
