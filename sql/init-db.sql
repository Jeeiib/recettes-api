-- =============================================
-- Script d'initialisation de la base de donnees
-- API Recettes - CDA 2025-2027
-- =============================================

-- 1. Creer la base de donnees (a executer en tant que superuser)
-- CREATE DATABASE recettes_db;

-- 2. Creer les tables (gerees automatiquement par Hibernate avec ddl-auto=update)
--    Lancer simplement l'application Spring Boot et les tables seront creees.

-- 3. Jeu de donnees initial (execute automatiquement au demarrage via data.sql)
--    Les donnees ci-dessous sont inserees automatiquement par Spring Boot.

-- =============================================
-- UTILISATEURS
-- Mot de passe : Test1234 (hashe BCrypt)
-- =============================================
INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Renart', 'Jayb', 'jb@jbrdevelopment.fr', '$2b$12$ViQ1Wq6Xxrkknw1F76aU.esg1p1tmtdNJJ/sIATFpJCc54BhyCvJa', '0612345678', 'ADMIN')
ON CONFLICT (mail) DO NOTHING;

INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Dupont', 'Marie', 'marie.dupont@mail.com', '$2b$12$ViQ1Wq6Xxrkknw1F76aU.esg1p1tmtdNJJ/sIATFpJCc54BhyCvJa', '0698765432', 'USER')
ON CONFLICT (mail) DO NOTHING;

INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Martin', 'Lucas', 'lucas.martin@mail.com', '$2b$12$ViQ1Wq6Xxrkknw1F76aU.esg1p1tmtdNJJ/sIATFpJCc54BhyCvJa', '0654321987', 'USER')
ON CONFLICT (mail) DO NOTHING;

-- =============================================
-- INGREDIENTS
-- =============================================
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Poulet', 'VIANDE', 165);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Boeuf hache', 'VIANDE', 254);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Lardons', 'VIANDE', 290);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Saumon', 'POISSON', 208);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Crevettes', 'POISSON', 99);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Tomate', 'LEGUME', 18);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Oignon', 'LEGUME', 40);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Courgette', 'LEGUME', 17);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Poivron', 'LEGUME', 31);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Champignon', 'LEGUME', 22);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Pomme', 'FRUIT', 52);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Citron', 'FRUIT', 29);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Creme fraiche', 'PRODUIT_LAITIER', 292);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Parmesan', 'PRODUIT_LAITIER', 431);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Mozzarella', 'PRODUIT_LAITIER', 280);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Riz', 'CEREALE', 130);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Pates', 'CEREALE', 131);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Farine', 'CEREALE', 364);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Ail', 'EPICE', 149);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Curcuma', 'EPICE', 312);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Basilic', 'EPICE', 23);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Huile d''olive', 'AUTRE', 884);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Oeuf', 'AUTRE', 155);
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Beurre', 'PRODUIT_LAITIER', 717);
