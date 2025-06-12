package fun.dashspace.carrentalsystem.util;

public class GenerateUsernameFromEmail {
    public static String generate(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "_");
    }
}
