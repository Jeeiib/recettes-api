package com.recettes.api.dtos;

import com.recettes.api.entites.TypeIngredient;

public record IngredientResponse(
        Long id,
        String libelle,
        TypeIngredient type,
        Integer nombreCalorie
) {
}
