package com.example.RM_solution.companyService;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifyCompanyRequest {
    //수정할 회사전화번호
    private String companyTelephone;
}
