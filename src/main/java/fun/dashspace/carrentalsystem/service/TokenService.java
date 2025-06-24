package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.auth.TokenPair;
import fun.dashspace.carrentalsystem.security.CustomUserDetails;

public interface TokenService {
    TokenPair generateTokenPair(CustomUserDetails userDetails);
}
