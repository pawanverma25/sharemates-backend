package dev.pawan.sharemate.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.mapper.UserMapper;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.repository.UserRepository;
import dev.pawan.sharemate.request.RegisterRequest;
import dev.pawan.sharemate.response.AuthResponseDTO;
import io.jsonwebtoken.JwtException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    public AuthResponseDTO registerUser(RegisterRequest request) throws Exception {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        User savedUser = userRepo.save(user);

        Optional<String> token = verify(request.getEmail(), request.getPassword());

        if (!token.isPresent()) {
            userRepo.delete(savedUser);
            throw new JwtException("token is not generated");
        }

        AuthResponseDTO response = new AuthResponseDTO("Registration Successful",
                UserMapper.INSTANCE.toDto(savedUser), token.get(), 60000);
        return response;
    }

    public Optional<String> verify(String email, String password) {
        String token = null;
        encoder.encode(password);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (authentication.isAuthenticated()) {
            token = jwtService.generateToken(email);
        }
        return Optional.ofNullable(token);
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        return savedUser;
    }

    public Boolean checkEmailExistance(String email) {
        return userRepo.existsByEmail(email);
    }

    public Boolean checkUsernameExistance(String username) {
        return userRepo.existsByUsername(username);
    }

    public User getUserDetails(String email) {
        User savedUser = userRepo.findByEmail(email);
        if (savedUser != null)
            return savedUser;
        return null;
    }
}
