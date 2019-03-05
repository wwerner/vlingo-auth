package io.vlingo.auth.model;

import java.time.Instant;

public class AuthenticationToken {
    private final Instant expiresOn;
    private final User subject;

    public static AuthenticationToken of(User user, Instant expiresOn) {
        return new AuthenticationToken(user, expiresOn);
    }

    protected AuthenticationToken(User user, Instant expiresOn) {
        this.subject = user;
        this.expiresOn = expiresOn;
    }

    public boolean expired() {
        return expiresOn.isBefore(Instant.now());
    }

    //public boolean signed();

}
