package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import com.example.RM_solution.auth.request.SignUpRequest;
import com.example.RM_solution.auth.util.HashUtil;
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

    public User findUser(String username){
        return userMapper.findByUsername(username);
    }
}
