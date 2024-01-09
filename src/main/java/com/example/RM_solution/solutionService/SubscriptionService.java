package com.example.RM_solution.solutionService;

import com.example.RM_solution.solutionService.entity.Company;
import com.example.RM_solution.solutionService.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionMapper subscriptionMapper;

    @Transactional
    public boolean createSubscription(SubscriptionRequest req, long userId){

        try{
            // 회사 정보 조회
            Long companyId = subscriptionMapper.findByCompanyName(req.getCompanyName());

            // 회사 정보가 없을 때 새로 만들기
            if (companyId == null) {
                // 회사정보 저장
                Company newCompany = Company.builder()
                        .companyName(req.getCompanyName())
                        .companyTelephone(req.getCompanyTelephone())
                        .companyMail(req.getCompanyMail()).build();
                companyId = subscriptionMapper.companyInsert(newCompany);
            }

            //구독 만료일
            long newExpirationDate = new Date().getTime() + (req.getSubscriptionPeriod() * 24 * 60 * 60 * 1000);

            // 구독 정보 생성
            Subscription newSubscription = Subscription.builder()
                    .userCount(req.getUserCount())
                    .serviceType(req.getServiceType())
                    .storageCapacityTB(req.getStorageCapacityTB())
                    .subscriptionExpirationDate(new Date(newExpirationDate))
                    .subscriptionCost(20000)
                    .user_id(userId)
                    .company_id(companyId).build();

            // 구독 정보 저장
            subscriptionMapper.insert(newSubscription);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
