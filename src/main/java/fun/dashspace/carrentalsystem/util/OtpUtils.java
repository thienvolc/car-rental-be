package fun.dashspace.carrentalsystem.util;

import java.security.SecureRandom;

public class OtpUtils {
    private OtpUtils() {}

    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            otp.append(random.nextInt(10));
        return otp.toString();
    }
}
