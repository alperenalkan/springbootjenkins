package com.tpe.PetStore.security.jwt;

import com.tpe.PetStore.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    //Amac: JWT'ler ile ilgili isimizi kolaylastirmak adina yardimci bir class.

    // 3 ana taskimiz var.

    //1 - GENERATE JWT
    public String generateJWT(Authentication auth){
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    //2 - VALIDATE JWT - Her istekte Authorization header icerisinde token olacak.
    public boolean validateJWT(String jwt){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException |
                 UnsupportedJwtException |
                 MalformedJwtException |
                 SignatureException |
                 IllegalArgumentException e) {
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    //3 - EXTRACT USERNAME FROM JWT - try/catch olmama nedeni, bir ustteki metoddan hemen sonra calisacak.
    //yani validasyon zaten yapilmis olacak.
    public String extractUsernameFromJwt(String jwt){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
