package com.recettes.api.dtos;

public record RecetteIngredientResponse(
        Long id,
        IngredientResponse ingredient,
        String quantite
) {
}
