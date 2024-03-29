package ru.bogdanov.SpringBootsecurityjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bogdanov.SpringBootsecurityjwt.model.ERole;
import ru.bogdanov.SpringBootsecurityjwt.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
