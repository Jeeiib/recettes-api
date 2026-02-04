package com.recettes.api.repositories;

import com.recettes.api.entites.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    boolean existsByLibelle(String libelle);
}
