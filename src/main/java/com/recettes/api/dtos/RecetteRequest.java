package com.recettes.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecetteRequest(
        @NotBlank(message = "Le nom du plat est obligatoire")
        String nomDuPlat,

        Integer dureePreparation,

        Integer dureeCuisson,

        Integer nombreCalorique,

        Boolean partage,

        List<RecetteIngredientRequest> ingredients
) {
}
