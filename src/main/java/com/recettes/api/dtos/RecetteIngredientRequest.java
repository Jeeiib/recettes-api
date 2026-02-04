package com.recettes.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecetteIngredientRequest(
        @NotNull(message = "L'identifiant de l'ingrédient est obligatoire")
        Long ingredientId,

        @NotBlank(message = "La quantité est obligatoire")
        String quantite
) {
}
