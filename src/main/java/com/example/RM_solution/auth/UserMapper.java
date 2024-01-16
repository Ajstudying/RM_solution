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
    @Insert("INSERT INTO user (username, secret, role, total_storage) " +
            "VALUES (#{username}, #{secret}, #{role}, #{totalStorage})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    //유저의 스토리지 용량 업데이트
    @Update("UPDATE user SET total_storage = #{totalStorage} WHERE user.id = #{id}")
    void update(@Param("totalStorage") int totalStorage, @Param("id") long id);


}
