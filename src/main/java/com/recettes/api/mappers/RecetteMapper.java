package com.recettes.api.mappers;

import com.recettes.api.dtos.RecetteResponse;
import com.recettes.api.entites.Recette;
import org.springframework.stereotype.Component;

@Component
public class RecetteMapper {

    private final UserMapper userMapper;
    private final RecetteIngredientMapper recetteIngredientMapper;

    public RecetteMapper(UserMapper userMapper, RecetteIngredientMapper recetteIngredientMapper) {
        this.userMapper = userMapper;
        this.recetteIngredientMapper = recetteIngredientMapper;
    }

    public RecetteResponse toResponse(Recette recette) {
        return new RecetteResponse(
                recette.getId(),
                recette.getNomDuPlat(),
                recette.getDureePreparation(),
                recette.getDureeCuisson(),
                recette.getNombreCalorique(),
                recette.getPartage(),
                userMapper.toResponse(recette.getAuteur()),
                recette.getRecetteIngredients().stream()
                        .map(recetteIngredientMapper::toResponse)
                        .toList()
        );
    }
}
