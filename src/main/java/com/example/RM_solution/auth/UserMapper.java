package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE user_id = #{user_id}")
    User findByUser_id(String user_id);
    @Insert("INSERT INTO user (user_id, secret) VALUES (#{user_id}, #{secret})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);


}
