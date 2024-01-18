package com.example.RM_solution.storageService;

import com.example.RM_solution.auth.entity.User;
import com.example.RM_solution.solutionService.response.StorageResponse;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StorageMapper {

    //유저의 스토리지 찾기
    @Select("SELECT * FROM storage WHERE user_id = #{id}")
    StorageResponse findByUserId(long id);

    //유저의 스토리지 용량 업데이트
    @Update("UPDATE storage " +
            "SET available_storage_capacity = #{availableStorageCapacity}, used_storage_capacity = #{usedStorage} " +
            "WHERE user_id = #{id}")
    void update(@Param("availableStorageCapacity") int availableStorageCapacity, @Param("usedStorage") int usedStorage, @Param("id") long id);

    //유저의 회원가입 시에 해당 row 추가됨.
    @Insert("INSERT INTO storage (available_storage_capacity, used_storage_capacity, user_id) " +
            "VALUES (#{availableStorageCapacity}, #{usedStorageCapacity}, #{user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Storage storage);
}
