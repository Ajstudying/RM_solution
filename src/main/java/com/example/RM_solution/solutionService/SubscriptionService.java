package com.example.RM_solution.solutionService;

import com.example.RM_solution.companyService.Company;
import com.example.RM_solution.companyService.CompanyMapper;
import com.example.RM_solution.solutionService.request.ModifySubscriptionRequest;
import com.example.RM_solution.solutionService.request.SubscriptionRequest;
import com.example.RM_solution.solutionService.response.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionMapper subscriptionMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Transactional
    public boolean createSubscription(SubscriptionRequest req, long userId){

        try{
            // 회사 정보 조회
            Long companyId = companyMapper.findByCompanyName(req.getCompanyName());

            // 회사 정보가 없을 때 새로 만들기
            if (companyId == null) {
                // 회사정보 저장
                Company newCompany = Company.builder()
                        .companyName(req.getCompanyName())
                        .companyTelephone(req.getCompanyTelephone())
                        .companyMail(req.getCompanyMail()).build();
                companyMapper.companyInsert(newCompany);
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
                    .storageCapacity(req.getStorageCapacity())
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

    @Transactional
    public Map<String, Object> modifySubscriptionData
            (long user_id, ModifySubscriptionRequest modifySubs){
        Map<String, Object> result = new HashMap<>();
        try{
            //해당 구독 정보가 있는지 확인
            Subscription foundSubs = subscriptionMapper.findByUser_idAndSubscriptionId(user_id, modifySubs.getSubscriptionId());
            if(foundSubs == null){
                return null;
            }
            //구독 만료일 설정
            int subscriptionPeriodInDays = modifySubs.getSubscriptionPeriod();

            // 현재 날짜에 일 수를 더하여 새로운 날짜를 얻기
            LocalDate currentDate = LocalDate.now();
            LocalDate newExpirationDate = currentDate.plus(subscriptionPeriodInDays, ChronoUnit.DAYS);

            // LocalDate를 Date로 변환
            Date date = Date.from(newExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            //만료일 수정
            subscriptionMapper.update(date, foundSubs.getId());

            //수정된 결과 조회
            SubscriptionResponse updatedSubs = subscriptionMapper.findBySubscriptionId(foundSubs.getId());

            // 결과를 Map에 저장
            result.put("success", true);
            result.put("data", updatedSubs);
        }catch (Exception e){
            e.printStackTrace();
            // 예외 처리 및 로깅
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

}
