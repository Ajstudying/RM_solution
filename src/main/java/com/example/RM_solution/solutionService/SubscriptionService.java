package com.example.RM_solution.solutionService;

import com.example.RM_solution.companyService.Company;
import com.example.RM_solution.companyService.CompanyMapper;
import com.example.RM_solution.solutionService.request.ModifySubscriptionRequest;
import com.example.RM_solution.solutionService.request.SubscriptionRequest;
import com.example.RM_solution.solutionService.response.AllSubscriptionsResponse;
import com.example.RM_solution.solutionService.response.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class SubscriptionService {

    @Autowired
    SubscriptionMapper subscriptionMapper;

    @Autowired
    CompanyMapper companyMapper;

    //전체 조회
    public List<AllSubscriptionsResponse> getSubscriptionData(){
        try{
            List<AllSubscriptionsResponse> allList = subscriptionMapper.findAllSubscription();
            if(allList.isEmpty()){
                return List.of();
            }
            return allList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //구독 정보 생성
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
                        .companyMail(req.getCompanyMail())
                        .companyAddress(req.getCompanyAddress())
                        .storageCapacity(1).build();
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

            //이미 구독 정보가 있을 때 추가적으로 구독 정보 생성 안되게
            Subscription isSubscription = subscriptionMapper.findByUserIdAndCompanyId(userId, companyId);
            if(isSubscription != null){
                return false;
            }
//            //회사 정보로 구독 정보가 있는지 확인
//            Long isSubscription = subscriptionMapper.findByCompanyId(companyId);
//
//            if(isSubscription != null){
//                return false;
//            }
            // 구독 정보 생성
            Subscription newSubscription = Subscription.builder()
                    .serviceType(req.getServiceType())
                    .subscriptionExpirationDate(date)
                    .subscriptionCost(20000)
                    .availableForSubscription(true)
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

    //유저의 구독 정보 데이터 조회
    @Transactional
    public List<SubscriptionResponse> getUsersSubscriptionData(long user_id) {
        try {
            //해당 유저의 구독 정보만 추출
            List<SubscriptionResponse> res = subscriptionMapper.findSubscriptionResponseByUser_id(user_id);

            //구독 정보가 없을 때 빈 리스트 내보내기
            if(res.isEmpty()){
                return List.of();
            }
            // 모든 구독 정보의 CompanyID를 추출
            List<Long> companyIds = res.stream()
                    .map(SubscriptionResponse::getCompanyId)
                    .collect(Collectors.toList());

            // 한 번의 쿼리(동적 sql로 서버에서 db간의 통신을 최소화) 모든 구독 정보와 사용자 수를 가져오기
            List<Map<String, Long>> userCounts = subscriptionMapper.findUserCountsByCompanyIds(companyIds);

            System.out.println(userCounts);

            // 구독 정보에 사용자 수 설정
            res.forEach(subscriptionResponse -> {
                int index = res.indexOf(subscriptionResponse);
                long companyId = userCounts.get(index).get("companyId");

                if (subscriptionResponse.getCompanyId() == companyId) {
                    long userCount = userCounts.get(index).get("userCount");
                    subscriptionResponse.setUserCount(userCount);
                }
            });

            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while processing the subscription data.", e);
        }
    }

    //유저의 구독 만료일 정보 수정 업데이트
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
            subscriptionMapper.update(date, true, foundSubs.getId());

            //수정된 결과 조회
            SubscriptionResponse updatedSubs = subscriptionMapper.findSubscriptionResponseById(foundSubs.getId());

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
    //매일 자정 지나고 30초에 확인
    @Scheduled(cron = "30 0 0 * * *")
    public void checkSubscription (){
        List<Subscription> allSubscription =
                subscriptionMapper.findAvailableForSubscriptionsAndSubscriptionExpirationDate();
        if(allSubscription.isEmpty()){
            return;
        }
        for (int i = 0; i < allSubscription.size(); i++) {
            // 현재 날짜
            LocalDate currentDate = LocalDate.now();
//            // LocalDate를 Date로 변환
//            long currentDay = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();
//            System.out.println(currentDay);
            //db에 저장 되어있는 날짜
            Date storedDate = allSubscription.get(i).getSubscriptionExpirationDate();
            // Date를 LocalDate로 변환
            LocalDate storedLocalDate = storedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // 만료일 계산
            LocalDate expirationDate = storedLocalDate.plus(1, ChronoUnit.DAYS);

            Subscription subs = allSubscription.get(i);
            //구독 만료일 당일까지는 사용가능하게끔
            //오늘 날짜와 저장된 만료일부터 하루 지난 날짜가 같으면 false/0으로 업데이트
            if(currentDate.equals(expirationDate)){
                System.out.println("만료됨");
                subscriptionMapper.updateAvailableForSubscription(subs.getId());
            }
        }
    }

}
