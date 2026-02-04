package com.recettes.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email doit être valide")
        String mail,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {
}
