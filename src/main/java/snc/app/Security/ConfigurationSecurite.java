package snc.app.Security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static snc.app.Services.AuthentificationService.NOM_COOKIE_JWT;

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
    BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver resolverEntete = new DefaultBearerTokenResolver();
        return request -> {
            String jetonEntete = resolverEntete.resolve(request);
            if (jetonEntete != null) {
                return jetonEntete;
            }
            if (request.getServletPath().startsWith("/api/")) {
                return null;
            }

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (NOM_COOKIE_JWT.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        };
    }

    @Bean
    SecurityFilterChain filtreSecurite(HttpSecurity http, BearerTokenResolver bearerTokenResolver) throws Exception {
        JwtAuthenticationConverter convertisseurJwt = new JwtAuthenticationConverter();
        convertisseurJwt.setJwtGrantedAuthoritiesConverter(jwt -> {
            String profil = jwt.getClaimAsString("profil");
            if (profil == null) {
                return List.of();
            }

            String autorite = switch (profil) {
                case "admin" -> "ROLE_ADMIN";
                case "responsable" -> "ROLE_RESPONSABLE";
                case "charge" -> "ROLE_CHARGE";
                default -> null;
            };
            return autorite == null
                    ? List.of()
                    : List.of(new SimpleGrantedAuthority(autorite));
        });

        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST, "/api/auth/connexion").permitAll()
                        .requestMatchers("/", "/login", "/utilisateurs/", "/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/utilisateurs/login", "/logout").permitAll()
                        .requestMatchers("/utilisateurs/admin/**").hasRole("ADMIN")
                        .requestMatchers("/Prestataire/**", "/evaluation/**")
                        .hasAnyRole("ADMIN", "RESPONSABLE")
                        .requestMatchers("/equipements/**", "/Station/**", "/incident/**", "/dashboard", "/api/**")
                        .hasAnyRole("ADMIN", "RESPONSABLE", "CHARGE")
                        .anyRequest().hasAnyRole("ADMIN", "RESPONSABLE", "CHARGE"))
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .bearerTokenResolver(bearerTokenResolver)
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(convertisseurJwt)))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies(NOM_COOKIE_JWT))
                .build();
    }
}
