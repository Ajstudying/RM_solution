package com.example.RM_solution.solutionService.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StorageResponse {
    // 사용 가능한 스토리지 용량
    private int availableStorageCapacity;
    // 사용 중인 스토리지 용량
    private int usedStorageCapacity;

//    public StorageResponse(int totalStorage, int usedStorageCapacity) {
//        this.availableStorageCapacity = totalStorage;
//        this.usedStorageCapacity = usedStorageCapacity;
//    }
}
