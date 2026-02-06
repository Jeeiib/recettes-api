package com.recettes.api.services;

import com.recettes.api.dtos.AuthResponse;
import com.recettes.api.dtos.LoginRequest;
import com.recettes.api.dtos.RefreshTokenRequest;
import com.recettes.api.dtos.RegisterRequest;
import com.recettes.api.entites.RefreshToken;
import com.recettes.api.entites.Role;
import com.recettes.api.entites.User;
import com.recettes.api.exceptions.EmailDejaExistantException;
import com.recettes.api.exceptions.RefreshTokenException;
import com.recettes.api.mappers.UserMapper;
import com.recettes.api.repositories.RefreshTokenRepository;
import com.recettes.api.repositories.UserRepository;
import com.recettes.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByMail(request.mail())) {
            throw new EmailDejaExistantException("Un compte existe déjà avec l'email : " + request.mail());
        }

        User user = User.builder()
                .nom(request.nom())
                .prenom(request.prenom())
                .mail(request.mail())
                .password(passwordEncoder.encode(request.password()))
                .telephone(request.telephone())
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.mail(), request.password())
        );

        User user = userRepository.findByMail(request.mail())
                .orElseThrow();

        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new RefreshTokenException("Refresh token invalide"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token expiré, veuillez vous reconnecter");
        }

        User user = refreshToken.getUser();
        return generateAuthResponse(user);
    }

    private AuthResponse generateAuthResponse(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getMail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken, userMapper.toResponse(user));
    }

    private String createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }
}
