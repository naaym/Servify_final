package com.servify.auth.service;

import com.servify.user.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtServiceImpl implements  JwtService {

  private final SecretKey signingKey;
  private final long expirationMillis;

  public JwtServiceImpl(@Value("${app.security.jwt.secret}") String secret,
                        @Value("${app.security.jwt.expiration:3600000}") long expirationMillis) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expirationMillis = expirationMillis;
  }
@Override
  public String generateToken(UserEntity user) {

    return Jwts
      .builder()
      .claim("role", user.getRole().name())
      .setSubject(user.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
      .signWith(signingKey, SignatureAlgorithm.HS256)
      .compact();
  }

  public String extractEmail(String token) {
    return extractAllClaims(token).getSubject();
  }

  @Override
  public String extractRole(String token) {
    return extractAllClaims(token)
      .get("role").toString();
  }

  @Override

  public Claims extractAllClaims(String token) {
    return Jwts.parser()
      .setSigningKey(signingKey).
      parseClaimsJws(token).getBody();
  }

  @Override

  public Boolean isTokenExpired(String token) {
    return extractAllClaims(token)
      .getExpiration()
      .before(new Date());
  }

  @Override

  public boolean isTokenValid(String token, UserDetails user) {
    try {

      return (extractAllClaims(token).getSubject().equals(user.getUsername()) && !isTokenExpired(token));
    } catch (Exception e) {
      return false;
    }
  }
}
