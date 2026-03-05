# API Recettes

API REST de gestion de recettes de cuisine avec authentification JWT, gestion de roles et export PDF/XLSX.

## Stack technique

- **Java 21**
- **Spring Boot 4.0.2**
- **Spring Security + JWT** (Access Token + Refresh Token)
- **Spring Data JPA** (Hibernate)
- **PostgreSQL 17**
- **SpringDoc OpenAPI** (Swagger UI)
- **OpenPDF** (export PDF)
- **Apache POI** (export XLSX)
- **JUnit 5 + Mockito** (tests unitaires)

## Prerequis

- Java 21+
- PostgreSQL 17+
- Maven (wrapper inclus)

## Installation

### 1. Base de donnees

Creer la base de donnees PostgreSQL :

```bash
createdb recettes_db
```

La configuration par defaut utilise :

| Parametre | Valeur |
|-----------|--------|
| Host | localhost |
| Port | 5432 |
| Database | recettes_db |
| Username | postgres |
| Password | postgres |

> Ces valeurs sont configurables dans `src/main/resources/application.properties`.

Le schema complet de la base et le jeu de donnees initial sont disponibles dans `sql/init-db.sql`.
Les tables sont creees automatiquement par Hibernate au demarrage (`ddl-auto=update`) et les donnees sont inserees via `src/main/resources/data.sql`.

### 2. Lancer l'application

```bash
./mvnw spring-boot:run
```

Au premier demarrage, Hibernate cree automatiquement les tables (`ddl-auto=update`) et le fichier `data.sql` insere le jeu de donnees initial (utilisateurs + ingredients).

### 3. Acceder a Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## Comptes de test

Tous les mots de passe sont : `Test1234`

| Email | Role | Description |
|-------|------|-------------|
| `jb@jbrdevelopment.fr` | ADMIN | Acces complet (CRUD tous, gestion users) |
| `marie.dupont@mail.com` | USER | Utilisateur standard |
| `lucas.martin@mail.com` | USER | Second utilisateur (pour tester la visibilite) |

## Comment tester

### Etape 1 : S'authentifier

Appeler `POST /api/auth/login` avec :

```json
{
  "mail": "marie.dupont@mail.com",
  "password": "Test1234"
}
```

La reponse contient un `accessToken`. Cliquer sur le bouton **Authorize** en haut de Swagger et entrer :

```
Bearer <votre_accessToken>
```

### Etape 2 : Tester les endpoints

#### Recettes (CRUD)

| Methode | URL | Description | Acces |
|---------|-----|-------------|-------|
| `GET` | `/api/recettes` | Lister les recettes visibles | Authentifie |
| `GET` | `/api/recettes/{id}` | Detail d'une recette | Authentifie (si visible) |
| `GET` | `/api/recettes/mes-recettes` | Mes recettes uniquement | Authentifie |
| `POST` | `/api/recettes` | Creer une recette | Authentifie |
| `PUT` | `/api/recettes/{id}` | Modifier une recette | Auteur ou ADMIN |
| `DELETE` | `/api/recettes/{id}` | Supprimer une recette | Auteur ou ADMIN |

Exemple de creation :

```json
{
  "nomDuPlat": "Poulet roti",
  "dureePreparation": 15,
  "dureeCuisson": 60,
  "nombreCalorique": 450,
  "partage": true,
  "ingredients": [
    { "ingredientId": 1, "quantite": "500g" },
    { "ingredientId": 7, "quantite": "2 cuilleres" }
  ]
}
```

#### Favoris

| Methode | URL | Description |
|---------|-----|-------------|
| `POST` | `/api/recettes/{id}/favoris` | Ajouter une recette en favori |
| `DELETE` | `/api/recettes/{id}/favoris` | Retirer une recette des favoris |
| `GET` | `/api/recettes/favoris` | Lister mes recettes favorites |

#### Export

| Methode | URL | Description |
|---------|-----|-------------|
| `GET` | `/api/recettes/{id}/pdf` | Telecharger la recette en PDF |
| `GET` | `/api/recettes/{id}/xlsx` | Telecharger la recette en XLSX |

#### Ingredients (CRUD)

| Methode | URL | Description | Acces |
|---------|-----|-------------|-------|
| `GET` | `/api/ingredients` | Lister tous les ingredients | Authentifie |
| `GET` | `/api/ingredients/{id}` | Detail d'un ingredient | Authentifie |
| `POST` | `/api/ingredients` | Creer un ingredient | Authentifie |
| `PUT` | `/api/ingredients/{id}` | Modifier un ingredient | ADMIN |
| `DELETE` | `/api/ingredients/{id}` | Supprimer un ingredient | ADMIN |

#### Utilisateurs

| Methode | URL | Description | Acces |
|---------|-----|-------------|-------|
| `GET` | `/api/users` | Lister tous les utilisateurs | ADMIN |
| `GET` | `/api/users/{id}` | Detail d'un utilisateur | ADMIN |
| `GET` | `/api/users/me` | Mon profil | Authentifie |
| `PUT` | `/api/users/me` | Modifier mon profil | Authentifie |
| `PUT` | `/api/users/{id}` | Modifier un utilisateur | ADMIN |
| `DELETE` | `/api/users/{id}` | Supprimer un utilisateur | ADMIN |

#### Authentification

| Methode | URL | Description | Acces |
|---------|-----|-------------|-------|
| `POST` | `/api/auth/register` | Creer un compte | Public |
| `POST` | `/api/auth/login` | Se connecter | Public |
| `POST` | `/api/auth/refresh` | Rafraichir le token | Public |

## Visibilite des recettes

- `partage = true` : visible par tous les utilisateurs authentifies
- `partage = false` : visible uniquement par l'auteur
- Les **ADMIN** voient toutes les recettes

## Roles

| Role | Permissions |
|------|-------------|
| **ADMIN** | CRUD sur toutes les recettes, tous les ingredients, tous les utilisateurs |
| **USER** | CRUD sur ses propres recettes, creer des ingredients, gerer ses favoris, modifier son profil |

## Architecture

```
com.recettes.api
├── config/          # SecurityConfig, OpenApiConfig, CorsConfig
├── controllers/     # AuthController, UserController, IngredientController, RecetteController
├── dtos/            # Records Java (Request/Response)
├── entites/         # User, Recette, Ingredient, RecetteIngredient, RefreshToken
├── exceptions/      # GlobalExceptionHandler, exceptions metier
├── mappers/         # Conversion Entity <-> DTO
├── repositories/    # Spring Data JPA
├── security/        # JwtService, JwtAuthenticationFilter, CustomUserDetailsService
└── services/        # RecetteService, UserService, IngredientService, AuthService, PdfService, XlsxService
```

## Tests

```bash
# Lancer tous les tests (44 tests unitaires)
./mvnw test
```

Les tests couvrent :
- **RecetteService** (20 tests) : CRUD, droits d'acces, favoris
- **UserService** (10 tests) : CRUD, unicite email
- **IngredientService** (8 tests) : CRUD complet
- **PdfService** (3 tests) : generation PDF valide
- **XlsxService** (3 tests) : generation XLSX valide

## Base de donnees

Le script SQL d'initialisation est disponible dans `sql/init-db.sql`.

Schema des tables :

```
users (id, nom, prenom, mail, password, telephone, role)
recettes (id, nom_du_plat, duree_preparation, duree_cuisson, nombre_calorique, partage, auteur_id)
ingredients (id, libelle, type, nombre_calorie)
recette_ingredients (id, recette_id, ingredient_id, quantite)
user_favoris (user_id, recette_id)
refresh_tokens (id, token, expiry_date, user_id)
```

## Codes HTTP

| Code | Signification |
|------|---------------|
| 200 | OK |
| 201 | Created |
| 204 | No Content (suppression) |
| 400 | Bad Request (validation) |
| 401 | Unauthorized (pas de token / token invalide) |
| 403 | Forbidden (pas les droits) |
| 404 | Not Found |
| 409 | Conflict (email deja existant) |
| 500 | Internal Server Error |
