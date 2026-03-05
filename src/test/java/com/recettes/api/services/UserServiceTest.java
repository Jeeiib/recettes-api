package com.recettes.api.services;

import com.recettes.api.dtos.UserResponse;
import com.recettes.api.dtos.UserUpdateRequest;
import com.recettes.api.entites.Role;
import com.recettes.api.entites.User;
import com.recettes.api.exceptions.EmailDejaExistantException;
import com.recettes.api.exceptions.ResourceNotFoundException;
import com.recettes.api.mappers.UserMapper;
import com.recettes.api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).nom("Dupont").prenom("Jean")
                .mail("jean@test.com").password("encoded")
                .telephone("0612345678").role(Role.USER)
                .build();

        userResponse = new UserResponse(1L, "Dupont", "Jean", "jean@test.com", "0612345678", Role.USER);
    }

    @Test
    void findAll_retourneTousLesUtilisateurs() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        List<UserResponse> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("Dupont", result.getFirst().nom());
    }

    @Test
    void findById_existant_retourneUtilisateur() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.findById(1L);

        assertEquals("jean@test.com", result.mail());
    }

    @Test
    void findById_inexistant_lanceException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.findById(99L));
    }

    @Test
    void findByMail_existant_retourneUtilisateur() {
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.findByMail("jean@test.com");

        assertEquals("Dupont", result.nom());
    }

    @Test
    void findByMail_inexistant_lanceException() {
        when(userRepository.findByMail("inconnu@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                userService.findByMail("inconnu@test.com"));
    }

    @Test
    void update_modifieLesProprietes() {
        UserUpdateRequest request = new UserUpdateRequest("Martin", "Paul", null, null, "0699999999");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.update(1L, request);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void update_avecNouvelEmail_verifieUnicite() {
        UserUpdateRequest request = new UserUpdateRequest(null, null, "nouveau@test.com", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByMail("nouveau@test.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.update(1L, request);

        assertNotNull(result);
        assertEquals("nouveau@test.com", user.getMail());
    }

    @Test
    void update_emailDejaExistant_lanceException() {
        UserUpdateRequest request = new UserUpdateRequest(null, null, "existant@test.com", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByMail("existant@test.com")).thenReturn(true);

        assertThrows(EmailDejaExistantException.class, () ->
                userService.update(1L, request));
    }

    @Test
    void delete_existant_supprime() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_inexistant_lanceException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                userService.delete(99L));
    }
}
