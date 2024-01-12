package com.example.RM_solution.solutionService;

import com.example.RM_solution.companyService.Company;
import com.example.RM_solution.solutionService.response.SubscriptionResponse;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface SubscriptionMapper {

    //구독 정보 추가
    @Insert("INSERT INTO subscription " +
            "(user_count, service_type, storage_capacity, subscription_expiration_date, subscription_cost, user_id, company_id ) " +
            "VALUES (#{userCount}, #{serviceType}, #{storageCapacity}, #{subscriptionExpirationDate}, #{subscriptionCost}, #{user_id}, #{company_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Subscription subscription);

    //회사정보 + 구독정보 조회
    @Select("SELECT * FROM subscription " +
            "inner join company on subscription.company_id = company.id " +
            "WHERE user_id = #{user_id}")
    List<SubscriptionResponse> findSubscriptionResponseByUser_id(long user_id);

    //해당 유저의 구독 정보가 있는지 조회
    @Select("SELECT * FROM subscription WHERE user_id = #{user_id} AND id = #{subscriptionId}")
    Subscription findByUser_idAndSubscriptionId(@Param("user_id") long user_id, @Param("subscriptionId") long subscriptionId);

    @Update("UPDATE subscription set " +
            "subscription_expiration_date = #{subscriptionPeriod} where subscription.id = #{id}")
    void update(@Param("subscriptionPeriod") Date subscriptionPeriod, @Param("id")long id);


    @Select("SELECT * FROM subscription " +
            "inner join company on subscription.company_id = company.id WHERE subscription.id = #{id}")
    SubscriptionResponse findBySubscriptionId(long id);





}
