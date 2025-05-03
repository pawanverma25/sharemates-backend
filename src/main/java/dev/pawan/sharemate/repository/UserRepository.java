package dev.pawan.sharemate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String email);
}
