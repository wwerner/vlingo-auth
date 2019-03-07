package io.vlingo.auth.model;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.vlingo.auth.model.crypto.JwtAuthenticationToken;
import org.junit.Test;

import java.time.Instant;

import static io.vlingo.auth.model.ModelFixtures.user;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class JwtAuthenticationTokenTest {

    @Test
    public void testThatTokenExpires() {
        assertTrue(JwtAuthenticationToken.of(user(), Instant.now().minusSeconds(1)).expired());
    }

    @Test
    public void testThatValidJwtIsProduced() {
        String token = JwtAuthenticationToken.of(user(), Instant.now().plusSeconds(1_000)).token();
        Jwt jwt = Jwts.parser()
            .setSigningKey("FIXME: secret from props/env")
            .parse(token);

        assertEquals("JWT", jwt.getHeader().getType());
    }

    @Test
    public void testUserMembershipsBecomeTokenClaims() {
        User user = user();
        TenantId tenantId = user.tenantId();

        Role role = Role.with(tenantId, "Foo", "");
        Group group = Group.with(tenantId, "Bar", "");
        Group group2 = Group.with(tenantId, "Baz", "");
        user.assignTo(group);
        user.assignTo(group2);
        user.assignTo(role);
        String token = JwtAuthenticationToken.of(user, Instant.now().plusSeconds(1_000)).token();
        Jwt jwt = Jwts.parser()
            .setSigningKey("FIXME: secret from props/env")
            .parse(token);

        assertThat(jwt.getBody().toString(), containsString("R=Foo"));
        assertThat(jwt.getBody().toString(), containsString("G=Bar,Baz"));
    }

}
