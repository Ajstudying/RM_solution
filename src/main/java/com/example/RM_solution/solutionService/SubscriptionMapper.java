package com.example.RM_solution.solutionService;

import com.example.RM_solution.solutionService.entity.Subscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SubscriptionMapper {
    @Select("SELECT * FROM subscription WHERE user_id = #{id}")
    Subscription findByUser_id(Long id);
}
