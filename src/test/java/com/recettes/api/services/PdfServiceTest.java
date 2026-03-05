package com.recettes.api.services;

import com.recettes.api.entites.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PdfServiceTest {

    private final PdfService pdfService = new PdfService();

    @Test
    void genererPdf_recetteComplete_retournePdfValide() {
        Recette recette = creerRecetteComplete();

        byte[] pdf = pdfService.genererPdf(recette);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        // Les fichiers PDF commencent par %PDF
        assertEquals('%', (char) pdf[0]);
        assertEquals('P', (char) pdf[1]);
        assertEquals('D', (char) pdf[2]);
        assertEquals('F', (char) pdf[3]);
    }

    @Test
    void genererPdf_recetteSansIngredients_retournePdfValide() {
        Recette recette = creerRecetteSansIngredients();

        byte[] pdf = pdfService.genererPdf(recette);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void genererPdf_recetteChampsFacultatifs_retournePdfValide() {
        User auteur = User.builder().id(1L).nom("Dupont").prenom("Jean").build();
        Recette recette = Recette.builder()
                .id(1L).nomDuPlat("Salade simple")
                .auteur(auteur).recetteIngredients(new ArrayList<>())
                .build();

        byte[] pdf = pdfService.genererPdf(recette);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    private Recette creerRecetteComplete() {
        User auteur = User.builder().id(1L).nom("Dupont").prenom("Jean").build();
        Ingredient poulet = Ingredient.builder().id(1L).libelle("Poulet").type(TypeIngredient.VIANDE).build();

        Recette recette = Recette.builder()
                .id(1L).nomDuPlat("Poulet rôti")
                .dureePreparation(15).dureeCuisson(60).nombreCalorique(450)
                .auteur(auteur).recetteIngredients(new ArrayList<>())
                .build();

        RecetteIngredient ri = RecetteIngredient.builder()
                .id(1L).recette(recette).ingredient(poulet).quantite("500g")
                .build();
        recette.getRecetteIngredients().add(ri);

        return recette;
    }

    private Recette creerRecetteSansIngredients() {
        User auteur = User.builder().id(1L).nom("Dupont").prenom("Jean").build();
        return Recette.builder()
                .id(1L).nomDuPlat("Test")
                .dureePreparation(10).dureeCuisson(20).nombreCalorique(200)
                .auteur(auteur).recetteIngredients(new ArrayList<>())
                .build();
    }
}
