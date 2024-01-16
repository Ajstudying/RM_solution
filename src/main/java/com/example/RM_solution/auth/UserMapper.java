package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findByUserId(long id);
    @Insert("INSERT INTO user (username, secret, role) " +
            "VALUES (#{username}, #{secret}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);




}
