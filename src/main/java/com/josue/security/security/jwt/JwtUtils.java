package com.josue.security.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {

   @Value("${jwt.secret.key}")
   private String secretKey;

   @Value("${jwt.time.expiration}")
   private String timeExpiration;

   public String generateAccessToken(String username) {
      return Jwts.builder()
          .setSubject(username)
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
          .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
          .compact();
   }

   public boolean isTokenValid(String token) {
      try {
         Jwts.parserBuilder()
             .setSigningKey(getSignatureKey())
             .build()
             .parseClaimsJws(token)
             .getBody();

         return true;
      } catch (Exception e) {
         log.error("Token inválido. Error: ".concat(e.getMessage()));
         return false;
      }
   }

   public String getUsernameFromToken(String token) {
      return getClaims(token, Claims::getSubject);
   }

   private <T> T getClaims(String token, Function<Claims, T> claimsTFunction) {
      Claims claims = extractAllClaims(token);
      return claimsTFunction.apply(claims);
   }

   private Claims extractAllClaims(String token) {
      return Jwts.parserBuilder()
          .setSigningKey(getSignatureKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
   }

   private Key getSignatureKey() {
      byte[] keyBytes = Decoders.BASE64.decode(secretKey);
      return Keys.hmacShaKeyFor(keyBytes);
   }

}
