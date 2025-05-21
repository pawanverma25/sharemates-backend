package dev.pawan.sharemate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.pawan.sharemate.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    User findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT('%', :username, '%') OR u.email LIKE CONCAT('%', :email, '%')")
    List<User> findAllByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

}
