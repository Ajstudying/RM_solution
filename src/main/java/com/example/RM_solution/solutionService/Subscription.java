package com.example.RM_solution.solutionService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    private long id;
    //서비스 형태(Basic, Standard, Premium)
    private String serviceType;
//    //스토리지 용량
//    private String storageCapacity;
    //구독만료날짜
    private Date subscriptionExpirationDate;
    //구독비용
    private int subscriptionCost;
    //구독 서비스 이용 가능한지 여부
    private boolean availableForSubscription;
    //사용자 id
    private long user_id;
    //회사정보
    private long company_id;

}
