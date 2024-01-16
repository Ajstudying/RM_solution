package com.example.RM_solution.storageService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Storage {
    //회원 가입 시에 초기화해서 생성되도록 설정.
    // 스토리지 용량 //어카운트의 잔액 느낌 초기값 10으로 설정
    private int totalStorage;
    // 사용 중인 스토리지 용량 // 어카운트 출금액 느낌 초기값 0
    private int usedStorageCapacity;
    //사용자 id
    private long user_id;
}
