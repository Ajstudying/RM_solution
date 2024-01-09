package com.example.RM_solution.solutionService;

import com.example.RM_solution.auth.Auth;
import com.example.RM_solution.auth.AuthUser;
import com.example.RM_solution.auth.UserMapper;
import com.example.RM_solution.solutionService.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/subscription")
public class SubscriptionController {

    @Autowired
    SubscriptionService service;

    @Autowired
    UserMapper userMapper;

    @Auth
    @PostMapping
    public ResponseEntity addSubscription
            (@RequestBody SubscriptionRequest subs, @RequestAttribute AuthUser authUser){

        System.out.println(subs);
        //데이터 무결성 확인
        if(subs.getUserCount() == null){
            System.out.println("구독 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(subs.getServiceType() == null || subs.getServiceType().isEmpty()){
            System.out.println("구독 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(subs.getStorageCapacityTB() == null || subs.getStorageCapacityTB().isEmpty()){
            System.out.println("구독 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(subs.getSubscriptionPeriod() == null){
            System.out.println("구독 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(subs.getCompanyName() == null || subs.getCompanyName().isEmpty()){
            System.out.println("회사 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(subs.getCompanyTelephone() == null || subs.getCompanyTelephone().isEmpty()){
            System.out.println("회사 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(subs.getCompanyMail() == null || subs.getCompanyMail().isEmpty()){
            System.out.println("회사 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //토큰이 유저정보가 맞지 않을때
        if(userMapper.findByUserId(authUser.getId()) == null){
            System.out.println("유저정보 오류");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //구독 정보 생성
        boolean checkSubscriptionInsert = service.createSubscription(subs, authUser.getId());
        if(checkSubscriptionInsert){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
