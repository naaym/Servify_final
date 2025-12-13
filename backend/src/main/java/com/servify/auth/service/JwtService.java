package com.servify.auth.service;

import com.servify.user.model.UserEntity;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String generateToken(UserEntity user);
  String extractEmail(String token);
  String extractRole(String token);
  Claims extractAllClaims(String token);
  Boolean isTokenExpired(String token);
  boolean isTokenValid(String token, UserDetails user);
}
