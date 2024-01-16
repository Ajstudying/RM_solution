package com.example.RM_solution.companyService;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompanyMapper {
    //회사 정보 추가
    @Insert("INSERT INTO company (company_name, company_telephone, company_mail, company_address) " +
            "VALUES (#{companyName}, #{companyTelephone}, #{companyMail}, #{companyAddress})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long companyInsert(Company company);

    @Select("SELECT * FROM company WHERE company_name = #{companyName}")
    Long findByCompanyName(String CompanyName);
}
