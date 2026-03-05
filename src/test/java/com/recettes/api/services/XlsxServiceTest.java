package com.recettes.api.services;

import com.recettes.api.entites.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class XlsxServiceTest {

    private final XlsxService xlsxService = new XlsxService();

    @Test
    void genererXlsx_recetteComplete_retourneXlsxValide() throws Exception {
        Recette recette = creerRecetteComplete();

        byte[] xlsx = xlsxService.genererXlsx(recette);

        assertNotNull(xlsx);
        assertTrue(xlsx.length > 0);

        // Vérifie que le fichier est un XLSX valide en le relisant
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(xlsx))) {
            assertEquals(1, workbook.getNumberOfSheets());
            assertEquals("Recette", workbook.getSheetAt(0).getSheetName());
        }
    }

    @Test
    void genererXlsx_recetteSansIngredients_retourneXlsxValide() throws Exception {
        Recette recette = creerRecetteSansIngredients();

        byte[] xlsx = xlsxService.genererXlsx(recette);

        assertNotNull(xlsx);
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(xlsx))) {
            assertEquals("Recette", workbook.getSheetAt(0).getSheetName());
        }
    }

    @Test
    void genererXlsx_recetteComplete_contientLesDonnees() throws Exception {
        Recette recette = creerRecetteComplete();

        byte[] xlsx = xlsxService.genererXlsx(recette);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(xlsx))) {
            var sheet = workbook.getSheetAt(0);
            // Première ligne = nom du plat
            assertEquals("Poulet rôti", sheet.getRow(0).getCell(0).getStringCellValue());
            // Ligne auteur
            assertEquals("Auteur", sheet.getRow(2).getCell(0).getStringCellValue());
        }
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
