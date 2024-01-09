package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
    @Insert("INSERT INTO user (username, secret) VALUES (#{username}, #{secret})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);


}
