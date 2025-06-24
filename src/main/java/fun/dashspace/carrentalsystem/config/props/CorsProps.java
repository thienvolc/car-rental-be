package fun.dashspace.carrentalsystem.config.props;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security.cors")
public class CorsProps {
    private String allowedOrigins;

    private List<String> allowedOriginList;
    private List<String> allowedMethodList;
    private List<String> allowedHeaderList;
    private Boolean allowCredentials;

    @PostConstruct
    public void adaptProperties() {
        this.allowedOriginList = List.of(allowedOrigins.split(","));
        this.allowedMethodList = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        this.allowedHeaderList = List.of("*");
        this.allowCredentials = true;
    }
}
