package ru.bogdanov.SpringBootsecurityjwt.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bogdanov.SpringBootsecurityjwt.exception.TokenRefreshException;
import ru.bogdanov.SpringBootsecurityjwt.model.RefreshToken;
import ru.bogdanov.SpringBootsecurityjwt.model.User;
import ru.bogdanov.SpringBootsecurityjwt.repository.RefreshTokenRepository;
import ru.bogdanov.SpringBootsecurityjwt.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${bogdanov.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());

        Instant now = Instant.now();
        ZoneId zone = ZoneId.of("Asia/Yekaterinburg");

        refreshToken.setCreatedDate(now.atZone(zone).toInstant());
        refreshToken.setExpiryDate(now.atZone(zone).toInstant().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken updateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(token));

        Instant now = Instant.now();
        ZoneId zone = ZoneId.of("Asia/Yekaterinburg");

        refreshToken.setCreatedDate(now.atZone(zone).toInstant());
        refreshToken.setExpiryDate(now.atZone(zone).toInstant().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public Optional<RefreshToken> findByUser(Long userId) {
        User user = userRepository.findById(userId).get();

        return refreshTokenRepository.findTop1ByUserOrderByIdDesc(user);
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
