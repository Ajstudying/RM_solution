package com.example.RM_solution.solutionService;

import com.example.RM_solution.companyService.Company;
import com.example.RM_solution.solutionService.response.AllSubscriptionsResponse;
import com.example.RM_solution.solutionService.response.SubscriptionResponse;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SubscriptionMapper {

    //전체 정보 조회
    //구독은 유저의 요청당 1개씩 생기기 때문에 구독 id를 기준으로 하면 조합이 안됨
    //그래서 회사를 기준으로 조회함.
    @Select("SELECT DISTINCT subscription.company_id, COUNT(subscription.user_id) AS userCount, " +
            "company.company_name, subscription_cost, company.company_telephone, " +
            "company.company_mail FROM subscription " +
            "INNER JOIN company ON subscription.company_id = company.id " +
            "GROUP BY subscription.company_id, company.company_name, subscription_cost, " +
            "company.company_telephone, company.company_mail " +
            "ORDER BY subscription.company_id DESC;")
    List<AllSubscriptionsResponse> findAllSubscription();

    //구독서비스 이용 여부와 만료 날짜 조회
    @Select("SELECT id, available_for_subscription, subscription_expiration_date FROM subscription")
    List<Subscription> findAvailableForSubscriptionsAndSubscriptionExpirationDate();
    @Update("UPDATE subscription set available_for_subscription = 0 WHERE subscription.id = #{id}")
    void updateAvailableForSubscription(long id);

    //구독 정보 추가
    @Insert("INSERT INTO subscription " +
            "(service_type, storage_capacity, subscription_expiration_date, " +
            "subscription_cost, available_for_subscription, user_id, company_id ) " +
            "VALUES (#{serviceType}, #{storageCapacity}, #{subscriptionExpirationDate}, " +
            "#{subscriptionCost}, #{availableForSubscription}, #{user_id}, #{company_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Subscription subscription);

    @Select("SELECT * FROM subscription where user_id = #{userId} and company_id = #{companyId}")
    Subscription findByUserIdAndCompanyId(@Param("userId") long userId, @Param("companyId") long companyId);

    //회사정보 + 구독정보 조회
    @Select("SELECT * FROM subscription " +
            "INNER JOIN company on subscription.company_id = company.id " +
            "WHERE user_id = #{user_id}")
    List<SubscriptionResponse> findSubscriptionResponseByUser_id(long user_id);

    //해당 유저의 구독 정보가 있는지 유저 아이디와 구독 아이디로 조회
    @Select("SELECT * FROM subscription WHERE user_id = #{user_id} AND id = #{subscriptionId}")
    Subscription findByUser_idAndSubscriptionId(@Param("user_id") long user_id, @Param("subscriptionId") long subscriptionId);

    //구독 기간 수정
    @Update("UPDATE subscription set " +
            "subscription_expiration_date = #{subscriptionPeriod}, " +
            "available_for_subscription = #{availableForSubscription} WHERE subscription.id = #{id}")
    void update(
            @Param("subscriptionPeriod") Date subscriptionPeriod,
            @Param("availableForSubscription") boolean availableForSubscription, @Param("id")long id);

    //구독 아이디로 구독 정보 조회
    @Select("SELECT * FROM subscription " +
            "INNER JOIN company on subscription.company_id = company.id WHERE subscription.id = #{id}")
    SubscriptionResponse findSubscriptionResponseById(long id);

    //회사 id를 기준으로 userCount 세기
//    @Select("SELECT company_id, COUNT(DISTINCT user_id) AS userCount " +
//            "FROM subscription WHERE company_id = #{id} GROUP BY company_id")
//    Map<Long, Long> findUserCountsByCompanyIds(List<Long> id);
    //sql 동적 처리
    @Select({
            "<script>",
            "SELECT company_id AS companyId, COUNT(DISTINCT user_id) AS userCount",
            "FROM subscription",
            "WHERE company_id IN",
            "<foreach collection='companyId' item='companyId' open='(' separator=',' close=')'>",
            "#{companyId}",
            "</foreach>",
            "GROUP BY company_id",
            "</script>"
    })
    List<Map<String, Long>> findUserCountsByCompanyIds(List<Long> companyId);

    //    @Select({
//            "<script>",
//            "SELECT company_id AS companyId, COUNT(DISTINCT user_id) AS userCount",
//            "FROM subscription",
//            "INNER JOIN user on subscription.user_id = #{id}",
//            "WHERE company_id IN",
//            "<foreach collection='companyId' item='companyId' open='(' separator=',' close=')'>",
//            "#{companyId}",
//            "</foreach>",
//            "GROUP BY company_id",
//            "</script>"
//    })
//    List<Map<String, Long>> findUserCountsByCompanyIds(long id);

}
