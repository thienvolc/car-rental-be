package fun.dashspace.carrentalsystem.security;

public class SecurityPaths {
    private SecurityPaths() {}

    public static final String[] PUBLIC_PATHS = {
            "/locations/**",

            "/auth/login",
            "/auth/token/refresh",
            "/auth/registration/email",
            "/auth/registration/email/verify",
            "/auth/renter/register",
            "/auth/password/forgot",
            "/auth/password/forgot/otp/verify",
            "/auth/password/reset",
            "/auth/otp/resend",

            "/cars/search",

            "/swagger/**",
            "/v3/api-docs/**",
    };

    public static final String[] USER_PATHS = {
            "/auth/logout",
            "/auth/logout/all",
            "/users/account/**",
            "/users/bookings/**",
            "/profile/**",
    };

    public static final String[] HOST_PATHS = {
            "/portal/cars/**",
            "/portal/bookings/**",
            "/portal/datacenter/**",
    };

    public static final String[] ADMIN_PATHS = {
            "/admin/**",
            "/users/all",
            "/reports/**",
            "/datacenter/**",
    };
}