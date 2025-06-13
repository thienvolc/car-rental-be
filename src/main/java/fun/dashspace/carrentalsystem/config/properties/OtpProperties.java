package fun.dashspace.carrentalsystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.otp")
public class OtpProperties {
    private int otpExpirationMinutes;
    private int optLength;
}