package com.recettes.api.mappers;

import com.recettes.api.dtos.UserResponse;
import com.recettes.api.entites.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getMail(),
                user.getTelephone(),
                user.getRole()
        );
    }
}
