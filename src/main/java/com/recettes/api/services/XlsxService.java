package com.recettes.api.services;

import com.recettes.api.entites.Recette;
import com.recettes.api.entites.RecetteIngredient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class XlsxService {

    public byte[] genererXlsx(Recette recette) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Recette");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font whiteFont = workbook.createFont();
            whiteFont.setBold(true);
            whiteFont.setColor(IndexedColors.WHITE.getIndex());
            whiteFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(whiteFont);

            CellStyle titreStyle = workbook.createCellStyle();
            Font titreFont = workbook.createFont();
            titreFont.setBold(true);
            titreFont.setFontHeightInPoints((short) 14);
            titreStyle.setFont(titreFont);

            int rowIdx = 0;

            Row titreRow = sheet.createRow(rowIdx++);
            Cell titreCell = titreRow.createCell(0);
            titreCell.setCellValue(recette.getNomDuPlat());
            titreCell.setCellStyle(titreStyle);

            rowIdx++;
            creerLigne(sheet, rowIdx++, "Auteur", recette.getAuteur().getPrenom() + " " + recette.getAuteur().getNom());

            if (recette.getDureePreparation() != null) {
                creerLigne(sheet, rowIdx++, "Durée de préparation", recette.getDureePreparation() + " min");
            }
            if (recette.getDureeCuisson() != null) {
                creerLigne(sheet, rowIdx++, "Durée de cuisson", recette.getDureeCuisson() + " min");
            }
            if (recette.getNombreCalorique() != null) {
                creerLigne(sheet, rowIdx++, "Nombre de calories", recette.getNombreCalorique() + " kcal");
            }

            if (!recette.getRecetteIngredients().isEmpty()) {
                rowIdx++;
                Row headerRow = sheet.createRow(rowIdx++);

                Cell h1 = headerRow.createCell(0);
                h1.setCellValue("Ingrédient");
                h1.setCellStyle(headerStyle);

                Cell h2 = headerRow.createCell(1);
                h2.setCellValue("Type");
                h2.setCellStyle(headerStyle);

                Cell h3 = headerRow.createCell(2);
                h3.setCellValue("Quantité");
                h3.setCellStyle(headerStyle);

                for (RecetteIngredient ri : recette.getRecetteIngredients()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(ri.getIngredient().getLibelle());
                    row.createCell(1).setCellValue(ri.getIngredient().getType().name());
                    row.createCell(2).setCellValue(ri.getQuantite());
                }
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du fichier XLSX", e);
        }
    }

    private void creerLigne(Sheet sheet, int rowIdx, String label, String value) {
        Row row = sheet.createRow(rowIdx);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
    }
}
