package snc.app.Security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class ConfigurationSecurite {
    private static final int TAILLE_MINIMALE_CLE_OCTETS = 32;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecretKey cleSignatureJwt(@Value("${JWT_SECRET}") String secret) {
        byte[] cle = secret.getBytes(StandardCharsets.UTF_8);
        if (cle.length < TAILLE_MINIMALE_CLE_OCTETS) {
            throw new IllegalStateException("JWT_SECRET doit contenir au moins 32 octets.");
        }
        return new SecretKeySpec(cle, "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey cleSignatureJwt) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(cleSignatureJwt));
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey cleSignatureJwt) {
        return NimbusJwtDecoder.withSecretKey(cleSignatureJwt)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    SecurityFilterChain filtreSecurite(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST, "/api/auth/connexion").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()))
                .build();
    }
}
