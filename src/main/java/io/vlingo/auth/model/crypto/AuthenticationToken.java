package io.vlingo.auth.model.crypto;

public interface AuthenticationToken {
    public boolean expired();

    public String token();
}
