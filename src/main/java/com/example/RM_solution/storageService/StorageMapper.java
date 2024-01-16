package com.example.RM_solution.storageService;

import com.example.RM_solution.auth.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StorageMapper {

    //유저의 스토리지 찾기
    @Select("SELECT * FROM storage WHERE user_id = #{id}")
    Storage findByUserId(long id);

    //유저의 스토리지 용량 업데이트
    @Update("UPDATE storage " +
            "SET total_storage = #{totalStorage}, used_storage_capacity = #{usedStorage} " +
            "WHERE user_id = #{id}")
    void update(@Param("totalStorage") int totalStorage, @Param("usedStorage") int usedStorage, @Param("id") long id);

    //유저의 회원가입 시에 해당 row 추가됨.
    @Insert("INSERT INTO storage (total_storage, used_storage_capacity, user_id) " +
            "VALUES (#{totalStorage}, #{usedStorageCapacity}, #{user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Storage storage);
}
