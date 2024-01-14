package com.example.RM_solution.solutionService;

import com.example.RM_solution.companyService.Company;
import com.example.RM_solution.solutionService.response.SubscriptionResponse;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SubscriptionMapper {

    //회사 정보로 구독정보 조회
    @Select("SELECT * FROM subscription WHERE company_id = #{id}")
    Long findByCompanyId(long id);

    //구독 정보 추가
    @Insert("INSERT INTO subscription " +
            "(service_type, storage_capacity, subscription_expiration_date, subscription_cost, user_id, company_id ) " +
            "VALUES (#{serviceType}, #{storageCapacity}, #{subscriptionExpirationDate}, #{subscriptionCost}, #{user_id}, #{company_id})")
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
    //구독 기간 수정
    @Update("UPDATE subscription set " +
            "subscription_expiration_date = #{subscriptionPeriod} where subscription.id = #{id}")
    void update(@Param("subscriptionPeriod") Date subscriptionPeriod, @Param("id")long id);
    //해당 유저의 구독정보 모두 조회
    @Select("SELECT * FROM subscription " +
            "INNER JOIN company on subscription.company_id = company.id WHERE subscription.id = #{id}")
    SubscriptionResponse findSubscriptionResponseById(long id);

    //회사 id를 기준으로 userCount 세기
//    @Select("SELECT company_id, COUNT(DISTINCT user_id) AS userCount " +
//            "FROM subscription WHERE company_id = #{id} GROUP BY company_id")
//    Map<Long, Long> findUserCountsByCompanyIds(List<Long> id);
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
