package cl.duoc.pedidos360.bff.config;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class AudienceValidatorTest {
    private final AudienceValidator validator = new AudienceValidator("api://pedidos360");

    @Test
    void acceptsExpectedAudience() {
        Jwt jwt = token(List.of("api://pedidos360"));
        assertThat(validator.validate(jwt).hasErrors()).isFalse();
    }

    @Test
    void rejectsUnexpectedAudience() {
        Jwt jwt = token(List.of("api://other"));
        assertThat(validator.validate(jwt).hasErrors()).isTrue();
    }

    private Jwt token(List<String> audience) {
        return new Jwt("token", Instant.now(), Instant.now().plusSeconds(300),
            java.util.Map.of("alg", "RS256"), java.util.Map.of("aud", audience, "sub", "student"));
    }
}
