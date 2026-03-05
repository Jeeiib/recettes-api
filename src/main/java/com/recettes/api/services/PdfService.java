package com.recettes.api.services;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.recettes.api.entites.Recette;
import com.recettes.api.entites.RecetteIngredient;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private static final Font TITRE_FONT = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(44, 62, 80));
    private static final Font SOUS_TITRE_FONT = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(52, 73, 94));
    private static final Font NORMAL_FONT = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

    public byte[] genererPdf(Recette recette) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph(recette.getNomDuPlat(), TITRE_FONT));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Informations", SOUS_TITRE_FONT));
            document.add(new Paragraph("Auteur : " + recette.getAuteur().getPrenom() + " " + recette.getAuteur().getNom(), NORMAL_FONT));

            if (recette.getDureePreparation() != null) {
                document.add(new Paragraph("Durée de préparation : " + recette.getDureePreparation() + " min", NORMAL_FONT));
            }
            if (recette.getDureeCuisson() != null) {
                document.add(new Paragraph("Durée de cuisson : " + recette.getDureeCuisson() + " min", NORMAL_FONT));
            }
            if (recette.getNombreCalorique() != null) {
                document.add(new Paragraph("Nombre de calories : " + recette.getNombreCalorique() + " kcal", NORMAL_FONT));
            }

            document.add(new Paragraph(" "));

            if (!recette.getRecetteIngredients().isEmpty()) {
                document.add(new Paragraph("Ingrédients", SOUS_TITRE_FONT));
                document.add(new Paragraph(" "));

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{3, 2, 2});

                ajouterCelluleHeader(table, "Ingrédient");
                ajouterCelluleHeader(table, "Type");
                ajouterCelluleHeader(table, "Quantité");

                for (RecetteIngredient ri : recette.getRecetteIngredients()) {
                    table.addCell(new PdfPCell(new Phrase(ri.getIngredient().getLibelle(), NORMAL_FONT)));
                    table.addCell(new PdfPCell(new Phrase(ri.getIngredient().getType().name(), NORMAL_FONT)));
                    table.addCell(new PdfPCell(new Phrase(ri.getQuantite(), NORMAL_FONT)));
                }

                document.add(table);
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private void ajouterCelluleHeader(PdfPTable table, String texte) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, HEADER_FONT));
        cell.setBackgroundColor(new Color(44, 62, 80));
        cell.setPadding(8);
        table.addCell(cell);
    }
}
