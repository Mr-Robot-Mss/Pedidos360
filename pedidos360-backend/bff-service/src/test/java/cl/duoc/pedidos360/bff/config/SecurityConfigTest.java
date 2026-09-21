package cl.duoc.pedidos360.bff.config;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class SecurityConfigTest {
    @Test
    void convertsScopesAndRolesIntoAuthorities() {
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(300),
            java.util.Map.of("alg", "RS256"),
            java.util.Map.of("sub", "student", "scp", "Pedidos.ReadWrite", "roles", List.of("Pedidos.Admin")));

        var authentication = new SecurityConfig().jwtAuthenticationConverter().convert(jwt);

        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities()).extracting("authority")
            .contains("SCOPE_Pedidos.ReadWrite", "ROLE_Pedidos.Admin");
    }
}
