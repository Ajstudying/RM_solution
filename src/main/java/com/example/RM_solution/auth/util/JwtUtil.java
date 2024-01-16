package com.example.RM_solution.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RM_solution.auth.AuthUser;
import com.example.RM_solution.auth.UserRole;

import java.util.Date;

public class JwtUtil {
    //임의의 서명 값
    public String secret = "pet-secret";

    //초/분/시간/하루/일주일
    public final long TOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 7;

    //JWT토큰 생성
    public String createToken(Long id, String username, UserRole role){
        Date now = new Date();

        Date expire = new Date(now.getTime()+TOKEN_TIMEOUT);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        // UserRole 열거형을 문자열로 변환
        String roleString = role.name();

        return JWT.create()
                .withSubject(id.toString())
                .withClaim("username", username)
                .withClaim("role", roleString)
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(algorithm);
    }

    //토큰 검증
    public AuthUser validateToken(String token){
        //검증 객체
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();

        try{
            DecodedJWT decodedJWT = verifier.verify(token);
            long id = Long.valueOf(decodedJWT.getSubject());
            String username = decodedJWT.getClaim("username").asString();
            String roleString = decodedJWT.getClaim("role").asString();
            UserRole role = UserRole.valueOf(roleString);

            return AuthUser.builder().id(id).username(username).role(role).build();

        }catch (JWTVerificationException e){
            //토큰 검증 오류 상황
            return null;
        }
    }

    public String expireToken(String token){
        Date expire = new Date(System.currentTimeMillis());
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String subject = decodedJWT.getSubject();

        String renewedToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(expire)
                .sign(algorithm);
        return renewedToken;
    }

}
