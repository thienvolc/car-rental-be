package fun.dashspace.carrentalsystem.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class VietnamInstantUtils {
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public static Instant now() {
        return ZonedDateTime.now(VIETNAM_ZONE).toInstant();
    }
}
