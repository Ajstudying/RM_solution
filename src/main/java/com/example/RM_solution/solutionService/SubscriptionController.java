package com.example.RM_solution.solutionService;

import com.example.RM_solution.auth.Auth;
import com.example.RM_solution.auth.AuthUser;
import com.example.RM_solution.auth.UserMapper;
import com.example.RM_solution.solutionService.request.ModifySubscriptionRequest;
import com.example.RM_solution.solutionService.request.SubscriptionRequest;
import com.example.RM_solution.solutionService.response.AllSubscriptionsResponse;
import com.example.RM_solution.solutionService.response.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/subscription")
public class SubscriptionController {

    @Autowired
    SubscriptionService service;

    @Autowired
    UserMapper userMapper;

    //전체 구독 정보 조회
    @GetMapping
    public ResponseEntity<List<AllSubscriptionsResponse>> getSubscription(){
        List<AllSubscriptionsResponse> result = service.getSubscriptionData();
        if(result != null){
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    //인증된 사용자의 구독 정보 추가
    @Auth
    @PostMapping
    public ResponseEntity addSubscription
            (@RequestBody SubscriptionRequest subs, @RequestAttribute AuthUser authUser){

        System.out.println(subs);
        //데이터 무결성 확인
        if(subs.getServiceType() == null || subs.getServiceType().isEmpty()){
            System.out.println("구독 정보 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(subs.getStorageCapacity() == null || subs.getStorageCapacity().isEmpty()){
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

    // 인증된 사용자의 구독 정보를 조회
    @Auth
    @GetMapping(value = "/{id}")
    public ResponseEntity<List<SubscriptionResponse>> getUsersSubscriptions(@RequestAttribute AuthUser authUser){
        List<SubscriptionResponse> res = service.getUsersSubscriptionData(authUser.getId());
        //자바에서 Date 타입을 Json으로 내보낼 때는 unix stamp 타입으로 변경돼서 나가게 됨.
        for (int i = 0; i < res.size(); i++) {
            //확인을 위한 구문
            Date date = res.get(i).getSubscriptionExpirationDate();
            System.out.println(date);
        }
        return ResponseEntity.ok(res);
    }

    //구독 정보 기간 수정
    @Auth
    @PutMapping
    public ResponseEntity<SubscriptionResponse> editSubscription(
            @RequestBody ModifySubscriptionRequest modifySubs,
            @RequestAttribute AuthUser authUser){

        System.out.println(modifySubs);

        Map<String, Object> result = service.modifySubscriptionData(authUser.getId(), modifySubs);

        //데이터가 null이 아니고 success가 true일때
        if((boolean) result.get("success") && result != null){
            SubscriptionResponse modifiedSubs = (SubscriptionResponse) result.get("data");

            return ResponseEntity.status(HttpStatus.OK).body(modifiedSubs);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
