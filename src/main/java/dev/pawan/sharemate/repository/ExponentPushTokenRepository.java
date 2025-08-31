package dev.pawan.sharemate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.pawan.sharemate.model.ExponentPushToken;

public interface ExponentPushTokenRepository extends JpaRepository<ExponentPushToken, Long> {
	Optional<ExponentPushToken> findByUserId(Integer userId);

}
