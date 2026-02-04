package com.recettes.api.dtos;

import java.util.List;

public record RecetteResponse(
        Long id,
        String nomDuPlat,
        Integer dureePreparation,
        Integer dureeCuisson,
        Integer nombreCalorique,
        Boolean partage,
        UserResponse auteur,
        List<RecetteIngredientResponse> ingredients
) {
}
