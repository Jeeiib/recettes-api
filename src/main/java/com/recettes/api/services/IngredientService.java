package com.recettes.api.services;

import com.recettes.api.dtos.IngredientRequest;
import com.recettes.api.dtos.IngredientResponse;
import com.recettes.api.entites.Ingredient;
import com.recettes.api.exceptions.ResourceNotFoundException;
import com.recettes.api.mappers.IngredientMapper;
import com.recettes.api.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public List<IngredientResponse> findAll() {
        return ingredientRepository.findAll().stream()
                .map(ingredientMapper::toResponse)
                .toList();
    }

    public IngredientResponse findById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrédient non trouvé avec l'id : " + id));
        return ingredientMapper.toResponse(ingredient);
    }

    @Transactional
    public IngredientResponse create(IngredientRequest request) {
        Ingredient ingredient = ingredientMapper.toEntity(request);
        return ingredientMapper.toResponse(ingredientRepository.save(ingredient));
    }

    @Transactional
    public IngredientResponse update(Long id, IngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrédient non trouvé avec l'id : " + id));

        ingredient.setLibelle(request.libelle());
        ingredient.setType(request.type());
        ingredient.setNombreCalorie(request.nombreCalorie());

        return ingredientMapper.toResponse(ingredientRepository.save(ingredient));
    }

    @Transactional
    public void delete(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ingrédient non trouvé avec l'id : " + id);
        }
        ingredientRepository.deleteById(id);
    }
}
