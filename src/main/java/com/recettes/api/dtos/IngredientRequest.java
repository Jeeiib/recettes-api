package com.recettes.api.dtos;

import com.recettes.api.entites.TypeIngredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientRequest(
        @NotBlank(message = "Le libellé est obligatoire")
        String libelle,

        @NotNull(message = "Le type d'ingrédient est obligatoire")
        TypeIngredient type,

        Integer nombreCalorie
) {
}
