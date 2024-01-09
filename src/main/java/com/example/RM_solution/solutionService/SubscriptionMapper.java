package com.example.RM_solution.solutionService;

import com.example.RM_solution.solutionService.entity.Company;
import com.example.RM_solution.solutionService.entity.Subscription;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SubscriptionMapper {
    @Select("SELECT * FROM subscription WHERE user_id = #{id}")
    Subscription findByUser_id(Long id);

    //회사 정보 추가
    @Insert("INSERT INTO company (company_name, company_telephone, company_mail) " +
            "VALUES (#{companyName}, #{companyTelephone}, #{companyMail})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long companyInsert(Company company);

    @Select("SELECT * FROM company WHERE company_name = #{companyName}")
    Long findByCompanyName(String CompanyName);

    //구독 정보 추가
    @Insert("INSERT INTO subscription " +
            "(user_count, service_type, storage_capacity, subscription_period, subscription_cost, user_id, company_id ) " +
            "VALUES (#{userCount}, #{serviceType}, #{storageCapacityTB}, #{subscriptionPeriod}, #{subscriptionCost}, #{user_id}, #{company_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Subscription subscription);


}
