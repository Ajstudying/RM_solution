package com.example.RM_solution.companyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    CompanyMapper companyMapper;

    // 회사 정보 조회 또는 생성
    public Long getOrCreateCompany(String companyName, String companyTelephone, String companyMail, String companyAddress) {
        Long companyId = companyMapper.findByCompanyName(companyName);

        // 회사 정보가 없을 때 새로 만들기
        if (companyId == null) {
            Company newCompany = Company.builder()
                    .companyName(companyName)
                    .companyTelephone(companyTelephone)
                    .companyMail(companyMail)
                    .companyAddress(companyAddress)
                    .storageCapacity(1)
                    .build();
            companyMapper.companyInsert(newCompany);
            companyId = newCompany.getId();
        }

        return companyId;
    }
}
