package ru.bogdanov.SpringBootsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bogdanov.SpringBootsecurityjwt.dto.response.MessageResponse;
import ru.bogdanov.SpringBootsecurityjwt.security.jwt.JwtUtils;
import ru.bogdanov.SpringBootsecurityjwt.security.service.RefreshTokenService;
import ru.bogdanov.SpringBootsecurityjwt.security.service.UserDetailsImpl;

@RestController
@RequestMapping("/api/auth")
public class SingOutController {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principle.toString().equals("anonymousUser")) {
            Long userId = ((UserDetailsImpl) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
