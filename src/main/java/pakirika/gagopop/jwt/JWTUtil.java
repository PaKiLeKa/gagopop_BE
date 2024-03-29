package pakirika.gagopop.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import java.util.Date;


@Component
public class JWTUtil {

    private SecretKey secretKey;


    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        secretKey = new SecretKeySpec(secret.getBytes( StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //아래 세가지 값 모두 토큰을 이용해서 페이로드에 담겨있는 특정 값을 가지고 나옴
    public String getUsername(String token) {

        if (token == null || token.isEmpty()) {
            return "token null";
        }
        try {
            Claims claims=Jwts.parser().verifyWith( secretKey ).build().parseSignedClaims( token ).getPayload();
            if(claims.containsKey( "username" )){
                return claims.get( "username", String.class );
            }
            else {
                return null;
            }

        }catch (ExpiredJwtException ex) {
            // 토큰이 만료된 경우
            ex.printStackTrace(); // 에러 로그 기록
            return "Token expired";
        } catch (SignatureException ex) {
            // 서명이 일치하지 않는 경우
            ex.printStackTrace(); // 에러 로그 기록
            return "Invalid token: Signature error";
        } catch (JwtException ex) {
            // 기타 JWT 예외 발생
            ex.printStackTrace(); // 에러 로그 기록
            return "Invalid token";
        }
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        try {
            Claims claims=Jwts.parser().verifyWith( secretKey ).build().parseSignedClaims( token ).getPayload();
            Date expiration=claims.getExpiration();
            return expiration.before( new Date() );
        } catch (ExpiredJwtException ex) {
            // 만료된 토큰이면 true 반환하기
            return true;
        } catch (JwtException ex) {
            // 유효하지 않은 토큰이면 false 반환하기
            return false;
        }
    }

    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String authToken) {
        return null;
    }
}
