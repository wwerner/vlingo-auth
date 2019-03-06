package io.vlingo.auth.model.crypto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vlingo.auth.model.EncodedMember;
import io.vlingo.auth.model.User;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationToken implements AuthenticationToken {
    private final Instant expiresOn;
    private final User user;

    private static final String signingSecret = "FIXME: secret from props/env"; //FIXME: make secret configurable, preferably from env
    private JwtAuthenticationToken(User user, Instant expiresOn) {
        this.user = user;
        this.expiresOn = expiresOn;
    }

    @Override
    public boolean expired() {
        return expiresOn.isBefore(Instant.now());
    }

    @Override
    public String token() {

        Map<String, String> membershipClaims = user.memberships().stream()
            .collect(
                Collectors.groupingBy(m -> String.valueOf(m.type()),
                    Collectors.mapping(EncodedMember::id, Collectors.joining(","))));



        return Jwts.builder()
            .setSubject(user.username())
            .setExpiration(Date.from(expiresOn))
            .setIssuedAt(Date.from(Instant.now()))
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // https://tools.ietf.org/html/rfc7519#section-5.1, https://tools.ietf.org/html/rfc7515#section-4.1.9
            .claim(Claims.SUBJECT, user.username())
            .claim("tnt", user.tenantId().value)
           .addClaims(Collections.unmodifiableMap(membershipClaims))
            .signWith(SignatureAlgorithm.HS512, signingSecret)
            .compact();
    }

    public static AuthenticationToken of(User user, Instant expiresOn) {
        return new JwtAuthenticationToken(user, expiresOn);
    }


}
