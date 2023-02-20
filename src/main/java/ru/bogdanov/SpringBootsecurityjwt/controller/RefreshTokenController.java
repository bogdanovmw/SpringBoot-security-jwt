package ru.bogdanov.SpringBootsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bogdanov.SpringBootsecurityjwt.dto.response.MessageResponse;
import ru.bogdanov.SpringBootsecurityjwt.exception.TokenRefreshException;
import ru.bogdanov.SpringBootsecurityjwt.model.RefreshToken;
import ru.bogdanov.SpringBootsecurityjwt.security.jwt.JwtUtils;
import ru.bogdanov.SpringBootsecurityjwt.security.service.RefreshTokenService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String oldToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((oldToken != null) && (oldToken.length() > 0)) {
            return refreshTokenService.findByToken(oldToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        RefreshToken refreshToken = refreshTokenService.updateRefreshToken(oldToken);
                        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                                .body(new MessageResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(oldToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.status(403).body(new MessageResponse("Refresh Token is empty!"));
    }
}
