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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecetteServiceTest {

    @Mock
    private RecetteRepository recetteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecetteMapper recetteMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RecetteService recetteService;

    private User user;
    private User admin;
    private User autreUser;
    private Recette recette;
    private Recette recettePartagee;
    private RecetteResponse recetteResponse;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).nom("Dupont").prenom("Jean")
                .mail("jean@test.com").role(Role.USER)
                .recettesFavorites(new ArrayList<>())
                .build();

        admin = User.builder()
                .id(2L).nom("Admin").prenom("Super")
                .mail("admin@test.com").role(Role.ADMIN)
                .build();

        autreUser = User.builder()
                .id(3L).nom("Martin").prenom("Paul")
                .mail("paul@test.com").role(Role.USER)
                .build();

        recette = Recette.builder()
                .id(1L).nomDuPlat("Poulet rôti")
                .dureePreparation(15).dureeCuisson(60)
                .nombreCalorique(450).partage(false)
                .auteur(user).recetteIngredients(new ArrayList<>())
                .build();

        recettePartagee = Recette.builder()
                .id(2L).nomDuPlat("Salade César")
                .partage(true).auteur(autreUser)
                .recetteIngredients(new ArrayList<>())
                .build();

        recetteResponse = new RecetteResponse(1L, "Poulet rôti", 15, 60, 450, false, null, List.of());

        ingredient = Ingredient.builder()
                .id(1L).libelle("Poulet").type(TypeIngredient.VIANDE).nombreCalorie(200)
                .build();
    }

    // --- findAll ---

    @Test
    void findAll_enTantQueUser_retourneRecettesVisibles() {
        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findByAuteurOrPartageTrue(user)).thenReturn(List.of(recette, recettePartagee));
        when(recetteMapper.toResponse(any())).thenReturn(recetteResponse);

        List<RecetteResponse> result = recetteService.findAll(authentication);

        assertEquals(2, result.size());
        verify(recetteRepository).findByAuteurOrPartageTrue(user);
    }

    @Test
    void findAll_enTantQueAdmin_retourneToutesLesRecettes() {
        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByMail("admin@test.com")).thenReturn(Optional.of(admin));
        when(recetteRepository.findAll()).thenReturn(List.of(recette, recettePartagee));
        when(recetteMapper.toResponse(any())).thenReturn(recetteResponse);

        List<RecetteResponse> result = recetteService.findAll(authentication);

        assertEquals(2, result.size());
        verify(recetteRepository).findAll();
    }

    // --- findMesRecettes ---

    @Test
    void findMesRecettes_retourneUniquementMesRecettes() {
        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findByAuteur(user)).thenReturn(List.of(recette));
        when(recetteMapper.toResponse(recette)).thenReturn(recetteResponse);

        List<RecetteResponse> result = recetteService.findMesRecettes(authentication);

        assertEquals(1, result.size());
        verify(recetteRepository).findByAuteur(user);
    }

    // --- findById ---

    @Test
    void findById_auteurAccede_retourneRecette() {
        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));
        when(recetteMapper.toResponse(recette)).thenReturn(recetteResponse);

        RecetteResponse result = recetteService.findById(1L, authentication);

        assertNotNull(result);
        assertEquals("Poulet rôti", result.nomDuPlat());
    }

    @Test
    void findById_recettePartagee_autreUserAccede() {
        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(2L)).thenReturn(Optional.of(recettePartagee));
        when(recetteMapper.toResponse(recettePartagee)).thenReturn(recetteResponse);

        RecetteResponse result = recetteService.findById(2L, authentication);

        assertNotNull(result);
    }

    @Test
    void findById_recettePrivee_autreUserRefuse() {
        when(authentication.getName()).thenReturn("paul@test.com");
        when(userRepository.findByMail("paul@test.com")).thenReturn(Optional.of(autreUser));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));

        assertThrows(AccesInterditException.class, () ->
                recetteService.findById(1L, authentication));
    }

    @Test
    void findById_adminAccedeATout() {
        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByMail("admin@test.com")).thenReturn(Optional.of(admin));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));
        when(recetteMapper.toResponse(recette)).thenReturn(recetteResponse);

        RecetteResponse result = recetteService.findById(1L, authentication);

        assertNotNull(result);
    }

    @Test
    void findById_recetteInexistante_lanceException() {
        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                recetteService.findById(99L, authentication));
    }

    // --- create ---

    @Test
    void create_sansIngredients_creeLaRecette() {
        RecetteRequest request = new RecetteRequest("Pâtes", 10, 15, 300, false, null);

        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.save(any(Recette.class))).thenReturn(recette);
        when(recetteMapper.toResponse(recette)).thenReturn(recetteResponse);

        RecetteResponse result = recetteService.create(request, authentication);

        assertNotNull(result);
        verify(recetteRepository).save(any(Recette.class));
    }

    @Test
    void create_avecIngredients_creeLaRecetteEtLesAssociations() {
        List<RecetteIngredientRequest> ingredients = List.of(
                new RecetteIngredientRequest(1L, "500g")
        );
        RecetteRequest request = new RecetteRequest("Poulet rôti", 15, 60, 450, false, ingredients);

        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(recetteRepository.save(any(Recette.class))).thenReturn(recette);
        when(recetteMapper.toResponse(recette)).thenReturn(recetteResponse);

        RecetteResponse result = recetteService.create(request, authentication);

        assertNotNull(result);
        verify(ingredientRepository).findById(1L);
    }

    @Test
    void create_ingredientInexistant_lanceException() {
        List<RecetteIngredientRequest> ingredients = List.of(
                new RecetteIngredientRequest(99L, "100g")
        );
        RecetteRequest request = new RecetteRequest("Test", 10, 10, 100, false, ingredients);

        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                recetteService.create(request, authentication));
    }

    // --- update ---

    @Test
    void update_parAuteur_modifieLaRecette() {
        RecetteRequest request = new RecetteRequest("Poulet grillé", 20, 45, 400, true, null);

        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));
        when(recetteRepository.save(any(Recette.class))).thenReturn(recette);
        when(recetteMapper.toResponse(recette)).thenReturn(recetteResponse);

        RecetteResponse result = recetteService.update(1L, request, authentication);

        assertNotNull(result);
        verify(recetteRepository).save(recette);
    }

    @Test
    void update_parAdmin_modifieLaRecette() {
        RecetteRequest request = new RecetteRequest("Poulet grillé", 20, 45, 400, true, null);

        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByMail("admin@test.com")).thenReturn(Optional.of(admin));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));
        when(recetteRepository.save(any(Recette.class))).thenReturn(recette);
        when(recetteMapper.toResponse(recette)).thenReturn(recetteResponse);

        RecetteResponse result = recetteService.update(1L, request, authentication);

        assertNotNull(result);
    }

    @Test
    void update_parAutreUser_lanceException() {
        RecetteRequest request = new RecetteRequest("Hack", 0, 0, 0, false, null);

        when(authentication.getName()).thenReturn("paul@test.com");
        when(userRepository.findByMail("paul@test.com")).thenReturn(Optional.of(autreUser));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));

        assertThrows(AccesInterditException.class, () ->
                recetteService.update(1L, request, authentication));
    }

    // --- delete ---

    @Test
    void delete_parAuteur_supprimeLaRecette() {
        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));

        recetteService.delete(1L, authentication);

        verify(recetteRepository).delete(recette);
    }

    @Test
    void delete_parAutreUser_lanceException() {
        when(authentication.getName()).thenReturn("paul@test.com");
        when(userRepository.findByMail("paul@test.com")).thenReturn(Optional.of(autreUser));
        when(recetteRepository.findById(1L)).thenReturn(Optional.of(recette));

        assertThrows(AccesInterditException.class, () ->
                recetteService.delete(1L, authentication));
    }

    // --- favoris ---

    @Test
    void ajouterFavori_recettePartagee_ajouteAuxFavoris() {
        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(2L)).thenReturn(Optional.of(recettePartagee));

        recetteService.ajouterFavori(2L, authentication);

        assertTrue(user.getRecettesFavorites().contains(recettePartagee));
        verify(userRepository).save(user);
    }

    @Test
    void ajouterFavori_dejaEnFavori_nAjoutePasEnDouble() {
        user.getRecettesFavorites().add(recettePartagee);

        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(2L)).thenReturn(Optional.of(recettePartagee));

        recetteService.ajouterFavori(2L, authentication);

        assertEquals(1, user.getRecettesFavorites().size());
    }

    @Test
    void supprimerFavori_retireDesFavoris() {
        user.getRecettesFavorites().add(recettePartagee);

        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteRepository.findById(2L)).thenReturn(Optional.of(recettePartagee));

        recetteService.supprimerFavori(2L, authentication);

        assertFalse(user.getRecettesFavorites().contains(recettePartagee));
        verify(userRepository).save(user);
    }

    @Test
    void findFavoris_retourneLaListeDesFavoris() {
        user.getRecettesFavorites().add(recettePartagee);

        when(authentication.getName()).thenReturn("jean@test.com");
        when(userRepository.findByMail("jean@test.com")).thenReturn(Optional.of(user));
        when(recetteMapper.toResponse(recettePartagee)).thenReturn(recetteResponse);

        List<RecetteResponse> result = recetteService.findFavoris(authentication);

        assertEquals(1, result.size());
    }
}
