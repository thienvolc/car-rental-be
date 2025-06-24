package fun.dashspace.carrentalsystem.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryProps {
    private String cloudName;
    private String apiKey;
    private String apiSecret;
}
