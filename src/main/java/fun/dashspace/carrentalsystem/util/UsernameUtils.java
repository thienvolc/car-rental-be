package fun.dashspace.carrentalsystem.util;

import java.security.SecureRandom;

public class UsernameUtils {
    private UsernameUtils() {}

    private static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateFromEmail(String email) {
        String base = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "_");
        return base + "_" + randomSuffix(6);
    }

    private static String randomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++)
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));

        return sb.toString();
    }
}
