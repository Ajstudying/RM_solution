package com.example.RM_solution.storageService;

import com.example.RM_solution.companyService.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    @Autowired
    StorageMapper storageMapper;
    @Autowired
    CompanyMapper companyMapper;

    // 스토리지 관리
    public void updateStorage(long userId, long companyId) {
        try{
            // 해당 유저의 스토리지 찾기
            Storage foundStorage = storageMapper.findByUserId(userId);

            // 해당 회사의 스토리지 사용량
            int usedStorage = companyMapper.findByCompanyId(companyId).getStorageCapacity();
            int remainedStorage = foundStorage.getTotalStorage() - usedStorage;

            // 스토리지 업데이트
            if (remainedStorage >= 0) {
                storageMapper.update(remainedStorage, usedStorage, userId);
            }else{
                // 예외 또는 로깅 등의 처리
                throw new RuntimeException("Not enough storage capacity");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
