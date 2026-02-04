package com.recettes.api.repositories;

import com.recettes.api.entites.Recette;
import com.recettes.api.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {

    List<Recette> findByAuteur(User auteur);

    List<Recette> findByPartageTrue();

    List<Recette> findByAuteurOrPartageTrue(User auteur);
}
