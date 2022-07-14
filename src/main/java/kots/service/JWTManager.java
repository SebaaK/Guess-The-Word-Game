package kots.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JWTManager {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60L * 1000 * expiration))
                .withClaim("roles", getRolesAsStringList(user.getAuthorities()))
                .sign(getAlgorithm());
    }

    public UsernamePasswordAuthenticationToken validateToken(String token) {
        DecodedJWT verifiedToken = JWT.require(getAlgorithm()).build().verify(token);
        String username = verifiedToken.getSubject();
        Collection<SimpleGrantedAuthority> authorities = getAuthoritiesCollection(getRoles(verifiedToken));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    private String[] getRoles(DecodedJWT verifiedToken) {
        return verifiedToken.getClaim("roles").asArray(String.class);
    }

    private List<String> getRolesAsStringList(Collection<GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private Collection<SimpleGrantedAuthority> getAuthoritiesCollection(String[] roles) {
        return Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret);
    }
}
