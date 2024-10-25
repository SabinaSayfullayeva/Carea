package com.example.carea.service;

import com.example.carea.entity.User;
import com.example.carea.repository.UserRepository;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor

@Service
public class JwtTokenService
{
    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);
    private final UserRepository userRepository;

    @Value("${jwt.token.secretKey}")
    private String secretKey;

    @Value("${jwt.token.expireDateInMilliSeconds}")
    private Long expireDate;


    public String generateToken(String subject)
    {
        Date date = new Date(System.currentTimeMillis() + expireDate);
        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(signKey(),SignatureAlgorithm.HS256)
                .compact();
    }
    public Key signKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public boolean validateToken(String token)
    {
        try
        {
            Jwts
                    .parser()
                    .setSigningKey(signKey())
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e)
        {
            log.warn("Expired JWT token");
        } catch (MalformedJwtException malformedJwtException)
        {
            log.warn("Malformed JWT token");
        } catch (SignatureException s)
        {
            log.warn("Invalid JWT token");
        } catch (UnsupportedJwtException unsupportedJwtException)
        {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException ex)
        {
            log.warn("JWT claims string is empty");
        }
        return false;
    }

    public User getUserFromToken(String token)
    {
        String subject = Jwts
                .parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userRepository.findByEmail(subject).orElse(null);

    }

}
