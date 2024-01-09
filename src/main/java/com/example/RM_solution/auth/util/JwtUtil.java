package com.example.RM_solution.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RM_solution.auth.AuthUser;

import java.util.Date;

public class JwtUtil {
    //임의의 서명 값
    public String secret = "pet-secret";

    //초/분/시간/하루/일주일
    public final long TOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 7;

    //JWT토큰 생성
    public String createToken(Long id, String username){
        Date now = new Date();

        Date expire = new Date(now.getTime()+TOKEN_TIMEOUT);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(id.toString())
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(algorithm);
    }

    public AuthUser validateToken(String token){
        System.out.println(token);
        //검증 객체
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();

        try{
            DecodedJWT decodedJWT = verifier.verify(token);
            long id = Long.valueOf(decodedJWT.getSubject());
            String username = decodedJWT.getClaim("username").asString();

            return AuthUser.builder().id(id).username(username).build();

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
