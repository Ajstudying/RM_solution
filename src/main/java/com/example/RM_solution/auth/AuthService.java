package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import com.example.RM_solution.auth.util.HashUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private HashUtil hash;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public boolean createIdentity(SignUpRequest req){
        try{
            User toSaveUser = User.builder()
                    .user_id(req.getUser_id())
                    .secret(hash.createHash(req.getPassword())).build();
            if(findUser(req.getUser_id()) != null){
                System.out.println("동일한 아이디 존재");
                return false;
            }
            userMapper.insert(toSaveUser);

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }

    public User findUser(String user_id){
        return userMapper.findByUser_id(user_id);
    }
}
