package com.recettes.api.controllers;

import com.recettes.api.dtos.RecetteRequest;
import com.recettes.api.dtos.RecetteResponse;
import com.recettes.api.entites.Recette;
import com.recettes.api.services.PdfService;
import com.recettes.api.services.RecetteService;
import com.recettes.api.services.XlsxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recettes")
@RequiredArgsConstructor
public class RecetteController {

    private final RecetteService recetteService;
    private final PdfService pdfService;
    private final XlsxService xlsxService;

    @GetMapping
    public ResponseEntity<List<RecetteResponse>> findAll(Authentication authentication) {
        return ResponseEntity.ok(recetteService.findAll(authentication));
    }

    @GetMapping("/mes-recettes")
    public ResponseEntity<List<RecetteResponse>> findMesRecettes(Authentication authentication) {
        return ResponseEntity.ok(recetteService.findMesRecettes(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetteResponse> findById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(recetteService.findById(id, authentication));
    }

    @PostMapping
    public ResponseEntity<RecetteResponse> create(
            @Valid @RequestBody RecetteRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recetteService.create(request, authentication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RecetteRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(recetteService.update(id, request, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        recetteService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favoris")
    public ResponseEntity<Void> ajouterFavori(@PathVariable Long id, Authentication authentication) {
        recetteService.ajouterFavori(id, authentication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favoris")
    public ResponseEntity<Void> supprimerFavori(@PathVariable Long id, Authentication authentication) {
        recetteService.supprimerFavori(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favoris")
    public ResponseEntity<List<RecetteResponse>> findFavoris(Authentication authentication) {
        return ResponseEntity.ok(recetteService.findFavoris(authentication));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id, Authentication authentication) {
        Recette recette = recetteService.getRecetteEntity(id, authentication);
        byte[] pdf = pdfService.genererPdf(recette);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recette-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}/xlsx")
    public ResponseEntity<byte[]> exportXlsx(@PathVariable Long id, Authentication authentication) {
        Recette recette = recetteService.getRecetteEntity(id, authentication);
        byte[] xlsx = xlsxService.genererXlsx(recette);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recette-" + id + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(xlsx);
    }
}
