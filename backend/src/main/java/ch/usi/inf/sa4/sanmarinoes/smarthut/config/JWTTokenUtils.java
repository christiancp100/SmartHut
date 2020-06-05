package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/** A utility class to handle JWTs */
@Component
public class JWTTokenUtils {
    /** The duration in seconds of the validity of a single token */
    private static final long JWT_TOKEN_VALIDITY = (long) 5 * 60 * 60;

    /** The secret key used to encrypt all JWTs */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Retrieves the claimed username from a given token
     *
     * @param token the token to inspect
     * @return the username
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Returns whether the token given is expired or not
     *
     * @param token the given token
     * @return true if expired, false if not
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /**
     * Creates a new JWT for a given user. While creating the token - 1. Define claims of the token,
     * like Issuer, Expiration, Subject, and the ID 2. Sign the JWT using the HS512 algorithm and
     * secret key. 3. According to JWS Compact Serialization
     * (https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1) compaction of
     * the JWT to a URL-safe string
     *
     * @param user the user to which create a JWT
     * @return the newly generated token
     */
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Validates the token given against matching userDetails
     *
     * @param token the token given
     * @param userDetails user details to validate against
     * @return true if valid, false if not
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
