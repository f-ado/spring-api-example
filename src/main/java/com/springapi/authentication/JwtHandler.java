package com.springapi.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtHandler.class);

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private int expiration;

    public String generateJWT(final Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createJWT(createClaims(userPrincipal), userPrincipal.getId().toString());
    }

    private String createJWT(Map<String, Object> claims, String subject) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Map<String, Object> createClaims(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userPrincipal.getUsername());
        claims.put("email", userPrincipal.getEmail());
        return claims;
    }

    public UUID getIdFromJWT(final String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public <R> R extractClaim(String token, Function<Claims, R> claimsFn) {
        final Claims claims = extractClaimsFromToken(token);
        return claimsFn.apply(claims);
    }
    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOGGER.error("Invalid token signature");
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token claims string is empty.");
        }
        return false;
    }
}
