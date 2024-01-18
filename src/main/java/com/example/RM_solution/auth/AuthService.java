package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import com.example.RM_solution.auth.request.SignUpRequest;
import com.example.RM_solution.auth.util.HashUtil;
import com.example.RM_solution.companyService.CompanyService;
import com.example.RM_solution.storageService.Storage;
import com.example.RM_solution.storageService.StorageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private HashUtil hash;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StorageMapper storageMapper;
    @Autowired
    private CompanyService companyService;

    @Transactional
    public boolean createIdentity(SignUpRequest req){
        try{
            User toSaveUser = User.builder()
                    .username(req.getUsername())
                    .secret(hash.createHash(req.getPassword()))
                    .role(req.getRole()).build();
            if(findUser(req.getUsername()) != null){
                System.out.println("동일한 아이디 존재");
                return false;
            }
            userMapper.insert(toSaveUser);
            Storage toSaveStorage = Storage.builder()
                    .totalStorage(10)
                    .usedStorageCapacity(0)
                    .user_id(toSaveUser.getId()).build();
            storageMapper.insert(toSaveStorage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }

//    @Transactional
//    public boolean createCompanyIdentity(CompanySignUpRequest req){
//        try{
//            User toSaveUser = User.builder()
//                    .username(req.getUsername())
//                    .secret(hash.createHash(req.getPassword()))
//                    .role(req.getRole()).build();
//            if(findUser(req.getUsername()) != null){
//                System.out.println("동일한 아이디 존재");
//                return false;
//            }
//            userMapper.insert(toSaveUser);
//            //회사 정보 확인 후 없으면 새로 만들기
//            companyService.createCompany(
//                    req.getCompanyName(), req.getCompanyAddress(), req.getCompanyMail(),
//                    req.getCompanyTelephone(),req.getStorageCapacity());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return true;
//
//    }

    public User findUser(String username){
        return userMapper.findByUsername(username);
    }
}
