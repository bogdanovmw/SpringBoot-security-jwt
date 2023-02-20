package ru.bogdanov.SpringBootsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bogdanov.SpringBootsecurityjwt.dto.response.MessageResponse;
import ru.bogdanov.SpringBootsecurityjwt.dto.response.UserInfoResponse;
import ru.bogdanov.SpringBootsecurityjwt.exception.TokenRefreshException;
import ru.bogdanov.SpringBootsecurityjwt.model.Role;
import ru.bogdanov.SpringBootsecurityjwt.security.jwt.JwtUtils;
import ru.bogdanov.SpringBootsecurityjwt.security.service.RefreshTokenService;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class CurrentUserController {
    private static final String PATTERN_FORMAT = "dd.MM.yyyy HH:mm:ss";
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.systemDefault());
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(user -> {
                        List<String> roles = new ArrayList<>();
                        user.getUser().getRoles().forEach(s -> roles.add(s.getName().name()));

                        return ResponseEntity.ok()
                                .body(new UserInfoResponse(
                                        user.getUser().getId(),
                                        user.getUser().getUsername(),
                                        user.getUser().getEmail(),
                                        roles,
                                        formatter.format(user.getCreatedDate()),
                                        formatter.format(user.getExpiryDate()))
                                );
                    })
                    .orElseThrow(() -> new TokenRefreshException("User is not is not in database!"));
        }

        return ResponseEntity.status(403).body(new MessageResponse("User is not authorized!"));
    }
}
