package com.example.RM_solution.auth;

import com.example.RM_solution.auth.entity.User;
import com.example.RM_solution.auth.util.HashUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private HashUtil hash;

    private final SqlSession sqlSession;

    public AuthService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Transactional
    public void createIdentity(SignUpRequest req){
        try{
            User toSaveUser = User.builder()
                    .user_id(req.getUser_id())
                    .secret(req.getSecret()).build();

            int rowResult = sqlSession.insert("com.example.RM_solution.save", toSaveUser);

            if (rowResult > 0) {
                System.out.println("데이터베이스에 삽입이 완료되었습니다.");
            } else {
                System.out.println("데이터 삽입에 실패하였습니다.");
            }
        }catch (Exception e){
            e.printStackTrace();

        }

    }
}
