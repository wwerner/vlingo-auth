package io.vlingo.auth.model.crypto;

import io.vlingo.auth.model.AuthenticationToken;
import io.vlingo.auth.model.User;

import java.time.Instant;

public class JwtToken extends AuthenticationToken {
    public JwtToken(User user, Instant expiresOn) {
        super(user, expiresOn);
    }
}
