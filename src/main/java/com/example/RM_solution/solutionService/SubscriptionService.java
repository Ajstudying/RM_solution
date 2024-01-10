package com.example.RM_solution.solutionService;

import com.example.RM_solution.solutionService.entity.Company;
import com.example.RM_solution.solutionService.entity.Subscription;
import com.example.RM_solution.solutionService.response.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

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
                subscriptionMapper.companyInsert(newCompany);
                companyId = newCompany.getId();
            }
            //구독 만료일 설정
            int subscriptionPeriodInDays = req.getSubscriptionPeriod();

            // 현재 날짜에 일 수를 더하여 새로운 날짜를 얻기
            LocalDate currentDate = LocalDate.now();
            LocalDate newExpirationDate = currentDate.plus(subscriptionPeriodInDays, ChronoUnit.DAYS);

            // LocalDate를 Date로 변환
            Date date = Date.from(newExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // 구독 정보 생성
            Subscription newSubscription = Subscription.builder()
                    .userCount(req.getUserCount())
                    .serviceType(req.getServiceType())
                    .storageCapacityTB(req.getStorageCapacityTB())
                    .subscriptionExpirationDate(date)
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

    public List<SubscriptionResponse> getSubscriptionData(long user_id) {
        try {
            List<SubscriptionResponse> res = subscriptionMapper.findSubscriptionResponseByUser_id(user_id);
            return (res != null) ? res : List.of();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while processing the subscription data.", e);
        }
    }
}
