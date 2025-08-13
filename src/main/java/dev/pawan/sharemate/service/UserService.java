package dev.pawan.sharemate.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.pawan.sharemate.mapper.UserMapper;
import dev.pawan.sharemate.model.ExponentPushToken;
import dev.pawan.sharemate.model.Preference;
import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.repository.ExponentPushTokenRepository;
import dev.pawan.sharemate.repository.PreferenceRepository;
import dev.pawan.sharemate.repository.UserRepository;
import dev.pawan.sharemate.request.RegisterRequest;
import dev.pawan.sharemate.request.UserDTO;
import dev.pawan.sharemate.response.AuthResponseDTO;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	private final PreferenceRepository preferenceRepo;
	private final EmailVerificationService emailVerificationTokenService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder encoder;
	private final ExponentPushTokenRepository expoTokenRepo;

	public AuthResponseDTO registerUser(RegisterRequest request) throws Exception {
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		User savedUser = userRepo.save(user);

		emailVerificationTokenService.sendVerificationEmail(savedUser);

		Optional<String> token = verify(request.getEmail(), request.getPassword());

		if (!token.isPresent()) {
			userRepo.delete(savedUser);
			throw new JwtException("token is not generated");
		}

		AuthResponseDTO response = new AuthResponseDTO("Registration Successful", UserMapper.INSTANCE.toDto(savedUser),
				token.get(), 60000, savedUser.getEmailVerified());
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
		User savedUser = userRepo.findByEmail(email).orElse(null);
		return savedUser;
	}

	public User getUserDetails(Integer userId) {
		User savedUser = userRepo.findById(userId).orElse(null);
		return savedUser;
	}

	public Boolean updateUser(UserDTO user) {
		Optional<User> founduser = userRepo.findById(user.getId());
		if (founduser.isPresent()) {
			User existingUser = founduser.get();
			if (user.getName() != null)
				existingUser.setName(user.getName());
			if (user.getEmail() != null)
				existingUser.setEmail(user.getEmail());
			if (user.getUsername() != null)
				existingUser.setUsername(user.getUsername());
			userRepo.save(existingUser);
			return true;
		} else {
			return false;
		}
	}

	public Map<String, String> getUserPreferences(Integer userId) {
		Optional<Preference> preferences = preferenceRepo.findByUserId(userId);
		return preferences.map(Preference::getUserPreference).orElse(null);
	}

	public Map<String, String> updateUserPreferences(Integer userId, Map<String, String> preferences) {
		Optional<Preference> existingPreference = preferenceRepo.findByUserId(userId);
		if (existingPreference.isPresent()) {
			Preference preference = existingPreference.get();
			preference.setUserPreference(preferences);
			preferenceRepo.save(preference);
			return preference.getUserPreference();
		} else {
			Preference newPreference = new Preference();
			newPreference.setUserId(userId);
			newPreference.setUserPreference(preferences);
			preferenceRepo.save(newPreference);
			return newPreference.getUserPreference();
		}
	}

	public boolean updateExpoToken(ExponentPushToken request) {
	 return expoTokenRepo.findByUserId(request.getUserId())
				.map(existingToken -> {
					existingToken.setToken(request.getToken());
					expoTokenRepo.save(existingToken);
					return true;
				}).orElseGet(() -> {
					expoTokenRepo.save(request);
					return true;
				});
		
	}
}
