package com.example.RM_solution.solutionService.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
public class SubscriptionResponse {

    //구독 id
    private long id;
    //사용인원
    private long userCount;
    //서비스 형태(Basic, Standard, Premium)
    private String serviceType;
    //구독만료날짜
    private Date subscriptionExpirationDate;
    //잔여기간
    private long extraSubscriptionDay;
    //구독비용
    private int subscriptionCost;
    //구독 서비스 이용 가능한지 여부
    private boolean availableForSubscription;
    //회사 id
    private long companyId;
    //회사명
    private String companyName;
    //회사전화번호
    private String companyTelephone;
    //이메일
    private String companyMail;
    //회사 주소
    private String companyAddress;
    //서비스의 스토리지 사용량
    private int storageCapacity;

}
