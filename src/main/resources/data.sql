-- Jeu de données initial pour recettes-api
-- Mot de passe : Password123! (hashé BCrypt)

-- Utilisateurs
INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Admin', 'Super', 'admin@recettes.com', '$2b$12$iMj7LNdJo0lDzmSsk8kvxuP/1PrRR5QeAiQO/FoQjcHhgXEW9bcpG', '0600000001', 'ADMIN')
ON CONFLICT (mail) DO NOTHING;

INSERT INTO users (nom, prenom, mail, password, telephone, role)
VALUES ('Dupont', 'Jean', 'user@recettes.com', '$2b$12$iMj7LNdJo0lDzmSsk8kvxuP/1PrRR5QeAiQO/FoQjcHhgXEW9bcpG', '0600000002', 'USER')
ON CONFLICT (mail) DO NOTHING;

-- Ingrédients
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Poulet', 'VIANDE', 165) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Saumon', 'POISSON', 208) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Tomate', 'LEGUME', 18) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Pomme', 'FRUIT', 52) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Crème fraîche', 'PRODUIT_LAITIER', 292) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Riz', 'CEREALE', 130) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Curcuma', 'EPICE', 312) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Pâtes', 'CEREALE', 131) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Oignon', 'LEGUME', 40) ON CONFLICT DO NOTHING;
INSERT INTO ingredients (libelle, type, nombre_calorie) VALUES ('Ail', 'EPICE', 149) ON CONFLICT DO NOTHING;
