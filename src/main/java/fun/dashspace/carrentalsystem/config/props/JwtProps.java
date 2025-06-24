package fun.dashspace.carrentalsystem.config.props;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProps {
    private String secretKey;
    private long expiration;
    private long refreshExpiration;

    private SecretKey key;
    private Duration tokenExpiry;
    private Duration refreshTokenExpiry;

    @PostConstruct
    private void adaptProperties() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.tokenExpiry = Duration.ofSeconds(expiration);
        this.refreshTokenExpiry = Duration.ofSeconds(refreshExpiration);
    }
}