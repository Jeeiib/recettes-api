package com.recettes.api.dtos;

import com.recettes.api.entites.Role;

public record UserResponse(
        Long id,
        String nom,
        String prenom,
        String mail,
        String telephone,
        Role role
) {
}
