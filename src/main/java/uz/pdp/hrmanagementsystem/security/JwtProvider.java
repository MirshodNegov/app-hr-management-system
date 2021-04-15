package uz.pdp.hrmanagementsystem.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.hrmanagementsystem.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private static final long expireTime = 1000 * 60 * 60 * 24;
    private static final String secretWord = "Secret Word";

    public String generateToken(String username, Set<Role> roles) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretWord)
                .compact();
        return token;
    }

    public String getEmailFromToken(String token){
        try {
            String email = Jwts
                    .parser()
                    .setSigningKey(secretWord)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return email;
        }catch (Exception e){
            return null;
        }
    }
}
