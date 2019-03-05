package io.vlingo.auth.model.crypto;

import org.junit.Test;

import java.time.Instant;

import static io.vlingo.auth.model.ModelFixtures.user;
import static org.junit.Assert.*;

public class JwtTokenTest {

    @Test
    public void testThatTokenExpires() {
        assertTrue(JwtToken.of(user(), Instant.now().minusSeconds(1)).expired());
    }

}
