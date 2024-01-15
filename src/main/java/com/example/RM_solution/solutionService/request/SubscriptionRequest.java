package com.example.RM_solution.solutionService.request;

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
    //서비스 형태(Basic, Standard, Premium)
    private String serviceType;
    //스토리지 용량
    private String storageCapacity;
    //구독기간
    private Integer subscriptionPeriod;

}
