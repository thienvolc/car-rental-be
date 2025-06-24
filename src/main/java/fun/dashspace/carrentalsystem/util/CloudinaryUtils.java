package fun.dashspace.carrentalsystem.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CloudinaryUtils {
    private CloudinaryUtils() {}

    private static final Pattern CLOUDINARY_PUBLIC_ID_PATTERN
            = Pattern.compile("/upload/(?:v\\d+/)?(.+?)\\.[a-zA-Z0-9]+$");

    // Example: https://res.cloudinary.com/cld-docs/image/upload/v1719309138/do8wnccnlzrfvwv1mqkq.jpg
    // Result: do8wnccnlzrfvwv1mqkq
    public static String extractPublicIdFromImageUrl(String url) throws IllegalArgumentException {
        Matcher matcher = CLOUDINARY_PUBLIC_ID_PATTERN.matcher(url);
        if (matcher.find())
            return matcher.group(1);
        throw new IllegalArgumentException("Invalid url: " + url);
    }
}
