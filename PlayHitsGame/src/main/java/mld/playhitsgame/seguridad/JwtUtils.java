/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.seguridad;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author miguel
 */
@Slf4j

@Component
public class JwtUtils {
    
    @Value("${jwt.secret.key}")
    private String secretkey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;
    
    // Generar token de acceso
    public String generateAccesToken(String usuario){
        
        return Jwts.builder()
                .setSubject(usuario)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.ES256)
                .compact();
        
    }
    
    // Validar el token de acceso
    public boolean isTokenValid(String token){
        
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception exception) {
            log.error("Token invalido ".concat(exception.getMessage()));
            return false;        
        }       
    }
    
    // Obtener firma el token
    public Key getSignatureKey(){
        
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);        
    }
    
    // Obtener los claims del token
    public Claims extractAllClaims(String token){
        
        return Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }
    
    // Obtener un solo claim
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);        
    }
    
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }
    
}
