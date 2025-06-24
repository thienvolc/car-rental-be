package fun.dashspace.carrentalsystem.config.props;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app.otp")
public class OtpProps {
    private int expiration;
    private int codeLength;

    private Duration requestExpiry;

    @PostConstruct
    public void adaptProperites() {
        this.requestExpiry = Duration.ofSeconds(this.expiration);
    }
}