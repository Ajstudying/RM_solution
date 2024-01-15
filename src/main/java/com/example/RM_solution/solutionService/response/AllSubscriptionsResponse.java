package com.example.RM_solution.solutionService.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AllSubscriptionsResponse {
    //사용인원
    private long userCount;
    //구독비용
    private int subscriptionCost;
    //회사 id
    private long companyId;
    //회사명
    private String companyName;
    //회사전화번호
    private String companyTelephone;
    //이메일
    private String companyMail;
}
