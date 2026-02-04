package com.recettes.api.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recettes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomDuPlat;

    private Integer dureePreparation;

    private Integer dureeCuisson;

    private Integer nombreCalorique;

    @Builder.Default
    @Column(nullable = false)
    private Boolean partage = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private User auteur;

    @OneToMany(mappedBy = "recette", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecetteIngredient> recetteIngredients = new ArrayList<>();
}
