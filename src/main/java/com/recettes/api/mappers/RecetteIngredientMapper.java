package com.recettes.api.mappers;

import com.recettes.api.dtos.RecetteIngredientResponse;
import com.recettes.api.entites.RecetteIngredient;
import org.springframework.stereotype.Component;

@Component
public class RecetteIngredientMapper {

    private final IngredientMapper ingredientMapper;

    public RecetteIngredientMapper(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public RecetteIngredientResponse toResponse(RecetteIngredient recetteIngredient) {
        return new RecetteIngredientResponse(
                recetteIngredient.getId(),
                ingredientMapper.toResponse(recetteIngredient.getIngredient()),
                recetteIngredient.getQuantite()
        );
    }
}
