package com.example.loginLab.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.loginLab.demo.dto.UserDto;
import com.example.loginLab.demo.entity.User;
import com.example.loginLab.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

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
                .withClaim("userid", user.getUserid())
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole())
                .sign(algorithm);

    }


    public Authentication validateToken(String token, HttpServletRequest request) {

        try {

            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decoded = verifier.verify(token);

            UserDto user = UserDto.builder()
                    .email(decoded.getSubject())
                    .userid(decoded.getClaim("userid").asLong())
                    .username(decoded.getClaim("username").asString())
                    .role(decoded.getClaim("role").asString())
                    .build();

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

            return new UsernamePasswordAuthenticationToken(user,null, authorities);
        } catch (TokenExpiredException e) {
            request.setAttribute("auth_error", "Token expired");
        } catch (JWTVerificationException e) {
            request.setAttribute("auth_error", "Invalid token");
        } catch (Exception e) {
            request.setAttribute("auth_error", "Authentication failed");
        }

        return null;
    }

    public Authentication  validateTokenStrongly(String token, HttpServletRequest request) {

       try {

           Algorithm algorithm = Algorithm.HMAC256(secretKey);

           JWTVerifier verifier = JWT.require(algorithm).build();

           DecodedJWT decoded = verifier.verify(token);

           Optional<User> singleUser = userRepository.findUserByEmail(decoded.getSubject());

           UserDto user = UserDto.builder()
                   .userid(singleUser.get().getUserid())
                   .email(singleUser.get().getEmail())
                   .username(singleUser.get().getUsername())
                   .role(singleUser.get().getRole())
                   .build();

           List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

           return new UsernamePasswordAuthenticationToken(user,null,authorities);
       }catch (TokenExpiredException e) {
           request.setAttribute("auth_error", "Token expired");
       } catch (JWTVerificationException e) {
           request.setAttribute("auth_error", "Invalid token");
       } catch (Exception e) {
           request.setAttribute("auth_error", "Authentication failed");
       }

       return null;
    }

    public Map<String,String> extractClaims(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);

        Map<String,String> claims = new HashMap<>();
        claims.put("email", decoded.getSubject());
        claims.put("username", decoded.getClaim("username").asString());
        claims.put("role", decoded.getClaim("role").asString());

        return claims;
    }

}
