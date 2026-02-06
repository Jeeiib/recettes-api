package com.recettes.api.services;

import com.recettes.api.dtos.UserResponse;
import com.recettes.api.dtos.UserUpdateRequest;
import com.recettes.api.entites.User;
import com.recettes.api.exceptions.EmailDejaExistantException;
import com.recettes.api.exceptions.ResourceNotFoundException;
import com.recettes.api.mappers.UserMapper;
import com.recettes.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        return userMapper.toResponse(user);
    }

    public UserResponse findByMail(String mail) {
        User user = userRepository.findByMail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + mail));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));

        if (request.mail() != null && !request.mail().equals(user.getMail())) {
            if (userRepository.existsByMail(request.mail())) {
                throw new EmailDejaExistantException("Un compte existe déjà avec l'email : " + request.mail());
            }
            user.setMail(request.mail());
        }

        if (request.nom() != null) user.setNom(request.nom());
        if (request.prenom() != null) user.setPrenom(request.prenom());
        if (request.telephone() != null) user.setTelephone(request.telephone());
        if (request.password() != null) user.setPassword(passwordEncoder.encode(request.password()));

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }
}
