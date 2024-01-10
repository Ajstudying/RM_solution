package com.example.RM_solution.solutionService.entity;

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
    //사용인원
    private int userCount;
    //서비스 형태(Basic, Standard, Premium)
    private String serviceType;
    //스토리지 용량
    private String storageCapacityTB;
    //구독만료날짜
    private Date subscriptionExpirationDate;
    //구독비용
    private int subscriptionCost;
    //사용자 id
    private long user_id;
    //회사정보
    private long company_id;

}
