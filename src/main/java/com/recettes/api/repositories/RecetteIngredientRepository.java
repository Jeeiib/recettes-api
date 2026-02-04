package com.recettes.api.repositories;

import com.recettes.api.entites.RecetteIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetteIngredientRepository extends JpaRepository<RecetteIngredient, Long> {
}
