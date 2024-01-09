package com.example.RM_solution.solutionService;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptionRequest {
    //회사명
    private String companyName;
    //회사전화번호
    private String companyTelephone;
    //이메일
    private String companyMail;
    //사용인원
    private int userCount;
    //서비스 형태(Basic, Standard, Premium)
    private String serviceType;
    //스토리지 용량
    private String storageCapacityTB;
    //구독기간
    private String subscriptionPeriod;
}
