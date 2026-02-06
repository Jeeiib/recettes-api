-- Jeu de données initial pour recettes-api
-- Mot de passe : Test1234 (hashé BCrypt)

-- Utilisateurs
INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Renart', 'Jayb', 'jb@jbrdevelopment.fr', '$2b$12$ViQ1Wq6Xxrkknw1F76aU.esg1p1tmtdNJJ/sIATFpJCc54BhyCvJa', '0612345678', 'ADMIN')
ON CONFLICT (mail) DO NOTHING;

INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Dupont', 'Marie', 'marie.dupont@mail.com', '$2b$12$ViQ1Wq6Xxrkknw1F76aU.esg1p1tmtdNJJ/sIATFpJCc54BhyCvJa', '0698765432', 'USER')
ON CONFLICT (mail) DO NOTHING;

INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Martin', 'Lucas', 'lucas.martin@mail.com', '$2b$12$ViQ1Wq6Xxrkknw1F76aU.esg1p1tmtdNJJ/sIATFpJCc54BhyCvJa', '0654321987', 'USER')
ON CONFLICT (mail) DO NOTHING;

-- Ingrédients
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Poulet', 'VIANDE', 165) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Boeuf haché', 'VIANDE', 254) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Lardons', 'VIANDE', 290) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Saumon', 'POISSON', 208) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Crevettes', 'POISSON', 99) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Tomate', 'LEGUME', 18) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Oignon', 'LEGUME', 40) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Courgette', 'LEGUME', 17) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Poivron', 'LEGUME', 31) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Champignon', 'LEGUME', 22) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Pomme', 'FRUIT', 52) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Citron', 'FRUIT', 29) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Crème fraîche', 'PRODUIT_LAITIER', 292) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Parmesan', 'PRODUIT_LAITIER', 431) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Mozzarella', 'PRODUIT_LAITIER', 280) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Riz', 'CEREALE', 130) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Pâtes', 'CEREALE', 131) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Farine', 'CEREALE', 364) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Ail', 'EPICE', 149) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Curcuma', 'EPICE', 312) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Basilic', 'EPICE', 23) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Huile d''olive', 'AUTRE', 884) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Oeuf', 'AUTRE', 155) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Beurre', 'PRODUIT_LAITIER', 717) ON CONFLICT DO NOTHING;
