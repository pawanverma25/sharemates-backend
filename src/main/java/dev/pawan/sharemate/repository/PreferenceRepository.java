package dev.pawan.sharemate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.pawan.sharemate.model.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, Integer>{

	public Optional<Preference> findByUserId(Integer userId);
	
	
}
