package com.recettes.api.services;

import com.recettes.api.dtos.RecetteIngredientRequest;
import com.recettes.api.dtos.RecetteRequest;
import com.recettes.api.dtos.RecetteResponse;
import com.recettes.api.entites.*;
import com.recettes.api.exceptions.AccesInterditException;
import com.recettes.api.exceptions.ResourceNotFoundException;
import com.recettes.api.mappers.RecetteMapper;
import com.recettes.api.repositories.IngredientRepository;
import com.recettes.api.repositories.RecetteRepository;
import com.recettes.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetteService {

    private final RecetteRepository recetteRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final RecetteMapper recetteMapper;

    public List<RecetteResponse> findAll(Authentication authentication) {
        User user = getUser(authentication);

        List<Recette> recettes;
        if (user.getRole() == Role.ADMIN) {
            recettes = recetteRepository.findAll();
        } else {
            recettes = recetteRepository.findByAuteurOrPartageTrue(user);
        }

        return recettes.stream().map(recetteMapper::toResponse).toList();
    }

    public List<RecetteResponse> findMesRecettes(Authentication authentication) {
        User user = getUser(authentication);
        return recetteRepository.findByAuteur(user).stream()
                .map(recetteMapper::toResponse)
                .toList();
    }

    public RecetteResponse findById(Long id, Authentication authentication) {
        User user = getUser(authentication);
        Recette recette = getRecette(id);

        verifierAccesLecture(recette, user);

        return recetteMapper.toResponse(recette);
    }

    @Transactional
    public RecetteResponse create(RecetteRequest request, Authentication authentication) {
        User user = getUser(authentication);

        Recette recette = Recette.builder()
                .nomDuPlat(request.nomDuPlat())
                .dureePreparation(request.dureePreparation())
                .dureeCuisson(request.dureeCuisson())
                .nombreCalorique(request.nombreCalorique())
                .partage(request.partage() != null ? request.partage() : false)
                .auteur(user)
                .build();

        ajouterIngredients(recette, request.ingredients());

        return recetteMapper.toResponse(recetteRepository.save(recette));
    }

    @Transactional
    public RecetteResponse update(Long id, RecetteRequest request, Authentication authentication) {
        User user = getUser(authentication);
        Recette recette = getRecette(id);

        verifierAccesModification(recette, user);

        recette.setNomDuPlat(request.nomDuPlat());
        recette.setDureePreparation(request.dureePreparation());
        recette.setDureeCuisson(request.dureeCuisson());
        recette.setNombreCalorique(request.nombreCalorique());
        recette.setPartage(request.partage() != null ? request.partage() : false);

        recette.getRecetteIngredients().clear();
        ajouterIngredients(recette, request.ingredients());

        return recetteMapper.toResponse(recetteRepository.save(recette));
    }

    @Transactional
    public void delete(Long id, Authentication authentication) {
        User user = getUser(authentication);
        Recette recette = getRecette(id);

        verifierAccesModification(recette, user);

        recetteRepository.delete(recette);
    }

    @Transactional
    public void ajouterFavori(Long recetteId, Authentication authentication) {
        User user = getUser(authentication);
        Recette recette = getRecette(recetteId);

        verifierAccesLecture(recette, user);

        if (!user.getRecettesFavorites().contains(recette)) {
            user.getRecettesFavorites().add(recette);
            userRepository.save(user);
        }
    }

    @Transactional
    public void supprimerFavori(Long recetteId, Authentication authentication) {
        User user = getUser(authentication);
        Recette recette = getRecette(recetteId);

        user.getRecettesFavorites().remove(recette);
        userRepository.save(user);
    }

    public List<RecetteResponse> findFavoris(Authentication authentication) {
        User user = getUser(authentication);
        return user.getRecettesFavorites().stream()
                .map(recetteMapper::toResponse)
                .toList();
    }

    public Recette getRecetteEntity(Long id, Authentication authentication) {
        User user = getUser(authentication);
        Recette recette = getRecette(id);
        verifierAccesLecture(recette, user);
        return recette;
    }

    private User getUser(Authentication authentication) {
        return userRepository.findByMail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    }

    private Recette getRecette(Long id) {
        return recetteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recette non trouvée avec l'id : " + id));
    }

    private void verifierAccesLecture(Recette recette, User user) {
        if (user.getRole() == Role.ADMIN) return;
        if (recette.getAuteur().getId().equals(user.getId())) return;
        if (Boolean.TRUE.equals(recette.getPartage())) return;

        throw new AccesInterditException("Vous n'avez pas accès à cette recette");
    }

    private void verifierAccesModification(Recette recette, User user) {
        if (user.getRole() == Role.ADMIN) return;
        if (recette.getAuteur().getId().equals(user.getId())) return;

        throw new AccesInterditException("Vous n'avez pas le droit de modifier cette recette");
    }

    private void ajouterIngredients(Recette recette, List<RecetteIngredientRequest> ingredients) {
        if (ingredients == null) return;

        for (RecetteIngredientRequest req : ingredients) {
            Ingredient ingredient = ingredientRepository.findById(req.ingredientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Ingrédient non trouvé avec l'id : " + req.ingredientId()));

            RecetteIngredient ri = RecetteIngredient.builder()
                    .recette(recette)
                    .ingredient(ingredient)
                    .quantite(req.quantite())
                    .build();

            recette.getRecetteIngredients().add(ri);
        }
    }
}
