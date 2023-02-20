package ru.bogdanov.SpringBootsecurityjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.bogdanov.SpringBootsecurityjwt.model.RefreshToken;
import ru.bogdanov.SpringBootsecurityjwt.model.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findTop1ByUserOrderByIdDesc(User user);

    @Modifying
    int deleteByUser(User user);
}
