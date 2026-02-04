package com.recettes.api.mappers;

import com.recettes.api.dtos.IngredientRequest;
import com.recettes.api.dtos.IngredientResponse;
import com.recettes.api.entites.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public IngredientResponse toResponse(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getLibelle(),
                ingredient.getType(),
                ingredient.getNombreCalorie()
        );
    }

    public Ingredient toEntity(IngredientRequest request) {
        return Ingredient.builder()
                .libelle(request.libelle())
                .type(request.type())
                .nombreCalorie(request.nombreCalorie())
                .build();
    }
}
