package com.recettes.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        String nom,

        String prenom,

        @Email(message = "L'email doit être valide")
        String mail,

        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        String password,

        String telephone
) {
}
