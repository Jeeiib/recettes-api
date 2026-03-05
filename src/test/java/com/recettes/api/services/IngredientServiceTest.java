package com.recettes.api.services;

import com.recettes.api.dtos.IngredientRequest;
import com.recettes.api.dtos.IngredientResponse;
import com.recettes.api.entites.Ingredient;
import com.recettes.api.entites.TypeIngredient;
import com.recettes.api.exceptions.ResourceNotFoundException;
import com.recettes.api.mappers.IngredientMapper;
import com.recettes.api.repositories.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientService ingredientService;

    private Ingredient ingredient;
    private IngredientResponse ingredientResponse;
    private IngredientRequest ingredientRequest;

    @BeforeEach
    void setUp() {
        ingredient = Ingredient.builder()
                .id(1L).libelle("Tomate").type(TypeIngredient.LEGUME).nombreCalorie(18)
                .build();

        ingredientResponse = new IngredientResponse(1L, "Tomate", TypeIngredient.LEGUME, 18);
        ingredientRequest = new IngredientRequest("Tomate", TypeIngredient.LEGUME, 18);
    }

    @Test
    void findAll_retourneTousLesIngredients() {
        when(ingredientRepository.findAll()).thenReturn(List.of(ingredient));
        when(ingredientMapper.toResponse(ingredient)).thenReturn(ingredientResponse);

        List<IngredientResponse> result = ingredientService.findAll();

        assertEquals(1, result.size());
        assertEquals("Tomate", result.getFirst().libelle());
    }

    @Test
    void findById_existant_retourneIngredient() {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toResponse(ingredient)).thenReturn(ingredientResponse);

        IngredientResponse result = ingredientService.findById(1L);

        assertEquals("Tomate", result.libelle());
    }

    @Test
    void findById_inexistant_lanceException() {
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                ingredientService.findById(99L));
    }

    @Test
    void create_creeUnNouvelIngredient() {
        when(ingredientMapper.toEntity(ingredientRequest)).thenReturn(ingredient);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(ingredientMapper.toResponse(ingredient)).thenReturn(ingredientResponse);

        IngredientResponse result = ingredientService.create(ingredientRequest);

        assertEquals("Tomate", result.libelle());
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void update_existant_modifieIngredient() {
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(ingredientMapper.toResponse(ingredient)).thenReturn(ingredientResponse);

        IngredientResponse result = ingredientService.update(1L, ingredientRequest);

        assertNotNull(result);
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void update_inexistant_lanceException() {
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                ingredientService.update(99L, ingredientRequest));
    }

    @Test
    void delete_existant_supprime() {
        when(ingredientRepository.existsById(1L)).thenReturn(true);

        ingredientService.delete(1L);

        verify(ingredientRepository).deleteById(1L);
    }

    @Test
    void delete_inexistant_lanceException() {
        when(ingredientRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                ingredientService.delete(99L));
    }
}
