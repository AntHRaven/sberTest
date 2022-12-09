package sber.test.task.sbertesttask.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Lazy
public class JwtTokenUtil
    implements Serializable {

  public static final long JWT_TOKEN_VALIDITY = 7 * 24 * 60 * 60; //1 неделя
  @Serial
  private static final long serialVersionUID = -2550185165626007488L;
  private static final ObjectMapper objectMapper = getDefaultObjectMapper();

  private final String secret = "zdtlD3JK56m6wTTgsNFhqzjqP";

  private static ObjectMapper getDefaultObjectMapper() {
    return new ObjectMapper();
  }

  //получение логина пользователя из jwt токена
  //Claims - это тело токена.
  public String getUsernameFromToken(String token) {
    String subject = getClaimFromToken(token, Claims::getSubject);
    JsonNode subjectJSON = null;
    try {
      subjectJSON = objectMapper.readTree(subject);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    if (subjectJSON != null) {
      return subjectJSON.get("username").asText();
    } else {
      return null;
    }
  }

  //получение id пользователя из jwt токена
  public String getUserIdFromToken(String token) {
    String subject = getClaimFromToken(token, Claims::getSubject);
    JsonNode subjectJSON = null;
    try {
      subjectJSON = objectMapper.readTree(subject);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    if (subjectJSON != null) {
      return subjectJSON.get("user_id").asText();
    } else {
      return "";
    }
  }

  public String getUserRoleFromToken(String token) {
    String subject = getClaimFromToken(token, Claims::getSubject);
    JsonNode subjectJSON = null;
    try {
      subjectJSON = objectMapper.readTree(subject);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    if (subjectJSON != null) {
      return subjectJSON.get("user_role").asText();
    } else {
      return "";
    }
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token,
      Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateToken(UserDetails customUserDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, customUserDetails.toString());
  }

  private String doGenerateToken(Map<String, Object> claims,
      String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean validateToken(String token,
      UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
