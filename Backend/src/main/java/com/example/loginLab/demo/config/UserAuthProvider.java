package com.example.loginLab.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.loginLab.demo.dto.UserDto;
import com.example.loginLab.demo.entity.User;
import com.example.loginLab.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAuthProvider {

    private final UserRepository userRepository;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @PostConstruct()
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto user) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + 600000); // 10 minute

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole())
                .sign(algorithm);

    }


    public Authentication validateToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        UserDto user = UserDto.builder()
                .email(decoded.getSubject())
                .username(decoded.getClaim("username").toString())
                .role(decoded.getClaim("role").toString())
                .build();

        return new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        Optional<User> singleUser = userRepository.findUserByEmail(decoded.getSubject());

        UserDto user = UserDto.builder()
                .email(singleUser.get().getEmail())
                .username(singleUser.get().getUsername())
                .role(singleUser.get().getRole())
                .build();

        return new UsernamePasswordAuthenticationToken(user,null,Collections.emptyList());
    }


}
