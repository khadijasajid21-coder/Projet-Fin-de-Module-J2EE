-- ============================================================
-- DONNÉES DE DÉMONSTRATION - CANTINE SCOLAIRE
-- Mots de passe : admin123 | agent123 | etudiant123
-- ============================================================

INSERT IGNORE INTO roles (id, nom) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_AGENT'), (3, 'ROLE_ETUDIANT');

INSERT IGNORE INTO utilisateurs (id, nom, prenom, email, password, telephone, actif) VALUES
    (1, 'Admin',     'Super',   'admin@cantine.com',            '$2b$10$WsEbmiy.zqjNmjJtzrNBJusKunan2JwxOtQqGb8ZGt1JTUCVLyqVW', '0612345678', TRUE),
    (2, 'Sajid',    'khadija',   'agent@cantine.com',            '$2b$10$BZl4.ut4k1quYBQDNqBSNuX2eRgW.0A78dJh41qcWrxzPJX4u7v6u', '0623456789', TRUE),
    (3, 'Tsouli',    'Malak',    'malak@etudiant.ma',      '$2b$10$yuNEGf2Ddo7G0xEHwmpztO.n24.R6HwlAbJNFYR72TaEkbi734evu', '0634567890', TRUE),
    (4, 'Tsouli',  'Youssef', 'youssef@etudiant.ma',  '$2b$10$yuNEGf2Ddo7G0xEHwmpztO.n24.R6HwlAbJNFYR72TaEkbi734evu', '0645678901', TRUE),
    (5, 'Alami',    'Ali',   'ali@etudiant.ma',     '$2b$10$yuNEGf2Ddo7G0xEHwmpztO.n24.R6HwlAbJNFYR72TaEkbi734evu', '0656789012', TRUE),
    (6, 'Ilhami',   'Marwa',    'marwa@etudiant.ma',     '$2b$10$yuNEGf2Ddo7G0xEHwmpztO.n24.R6HwlAbJNFYR72TaEkbi734evu', '0667890123', TRUE),
    (7, 'Chakir', 'Meryem',  'meryem@etudiant.ma', '$2b$10$yuNEGf2Ddo7G0xEHwmpztO.n24.R6HwlAbJNFYR72TaEkbi734evu', '0678901234', TRUE);

INSERT IGNORE INTO utilisateur_roles (utilisateur_id, role_id) VALUES
(1,1),(2,2),(3,3),(4,3),(5,3),(6,3),(7,3);

INSERT IGNORE INTO etudiants (id, matricule, filiere, niveau, utilisateur_id) VALUES
    (1,'123','Informatique','L3',3),
    (2,'456','Génie Civil','L2',4),
    (3,'789','Économie','M1',5),
    (4,'1011124','Biologie','L1',6),
    (5,'131415','Informatique','M2',7);

-- Plats avec images Unsplash (cuisine marocaine)
INSERT IGNORE INTO plats (id, nom, description, prix, disponible, image) VALUES
(1,  'Couscous au poulet',  'Couscous traditionnel avec poulet grillé et légumes de saison',      25.00, TRUE,  'https://images.unsplash.com/photo-1541518763669-27fef04b14ea?w=500&h=350&fit=crop'),
(2,  'Tajine d''agneau',    'Tajine marocain à l''agneau avec pruneaux et amandes',               35.00, TRUE,  'https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=500&h=350&fit=crop'),
(3,  'Harira soupe',        'Soupe marocaine aux tomates, lentilles et pois chiches',             10.00, TRUE,  'https://images.unsplash.com/photo-1547592180-85f173990554?w=500&h=350&fit=crop'),
(4,  'Poisson grillé',      'Filet de poisson grillé avec légumes vapeur et citron',              30.00, TRUE,  'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=500&h=350&fit=crop'),
(5,  'Salade marocaine',    'Salade fraîche tomates, concombres, olives et huile d''argan',       12.00, TRUE,  'https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500&h=350&fit=crop'),
(6,  'Riz au poulet',       'Riz parfumé au safran avec escalope de poulet',                      22.00, TRUE,  'https://images.unsplash.com/photo-1512058564366-18510be2db19?w=500&h=350&fit=crop'),
(7,  'Msemen',              'Crêpe marocaine feuilletée au beurre et miel',                        8.00, TRUE,  'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=500&h=350&fit=crop'),
(8,  'Thé à la menthe',     'Thé vert à la menthe fraîche (service complet)',                      5.00, TRUE,  'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=500&h=350&fit=crop'),
(9,  'Jus d''orange',       'Jus d''orange fraîchement pressé',                                    8.00, TRUE,  'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=500&h=350&fit=crop'),
(10, 'Pastilla au poulet',  'Pastilla feuilletée au poulet, amandes et cannelle',                 28.00, TRUE,  'https://images.unsplash.com/photo-1574484284002-952d92456975?w=500&h=350&fit=crop');

INSERT IGNORE INTO menus (id, date_menu, titre, description, actif) VALUES
(1, CURDATE(),                          'Menu du Jour',       'Menu spécial de la journée avec produits frais', TRUE),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY),'Menu Demain',        'Menu du lendemain préparé avec soin',            TRUE),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY),'Menu du Mercredi',   'Spécialités du milieu de semaine',               TRUE),
(4, DATE_SUB(CURDATE(), INTERVAL 1 DAY),'Menu d''Hier',       'Menu de la veille (archivé)',                    TRUE),
(5, DATE_SUB(CURDATE(), INTERVAL 2 DAY),'Menu de Avant-Hier', 'Menu archivé',                                   TRUE);

INSERT IGNORE INTO menu_plats (menu_id, plat_id) VALUES
(1,1),(1,3),(1,5),(1,8),(1,9),
(2,2),(2,4),(2,5),(2,8),
(3,6),(3,7),(3,9),(3,10),
(4,1),(4,3),(4,8),
(5,2),(5,5),(5,9);

INSERT IGNORE INTO commandes (id, date_commande, statut, quantite_totale, montant_total, etudiant_id) VALUES
(1, DATE_SUB(NOW(), INTERVAL 5 HOUR), 'SERVIE',    3, 48.00, 1),
(2, DATE_SUB(NOW(), INTERVAL 4 HOUR), 'PREPAREE',  2, 40.00, 2),
(3, DATE_SUB(NOW(), INTERVAL 3 HOUR), 'VALIDEE',   2, 30.00, 3),
(4, DATE_SUB(NOW(), INTERVAL 2 HOUR), 'EN_ATTENTE',3, 62.00, 4),
(5, DATE_SUB(NOW(), INTERVAL 1 HOUR), 'EN_ATTENTE',1, 25.00, 5),
(6, DATE_SUB(NOW(), INTERVAL 6 HOUR), 'SERVIE',    4, 80.00, 1),
(7, DATE_SUB(NOW(), INTERVAL 1 DAY),  'SERVIE',    2, 33.00, 2),
(8, DATE_SUB(NOW(), INTERVAL 1 DAY),  'ANNULEE',   1, 22.00, 3),
(9, DATE_SUB(NOW(), INTERVAL 2 DAY),  'SERVIE',    3, 65.00, 1),
(10,DATE_SUB(NOW(), INTERVAL 2 DAY),  'SERVIE',    2, 45.00, 4);

INSERT IGNORE INTO lignes_commande (commande_id, plat_id, quantite, prix_unitaire) VALUES
(1,1,1,25.00),(1,3,1,10.00),(1,8,1,5.00),(1,9,1,8.00),
(2,2,1,35.00),(2,8,1,5.00),
(3,6,1,22.00),(3,8,1,5.00),
(4,1,1,25.00),(4,2,1,35.00),(4,9,1,8.00),
(5,1,1,25.00),
(6,4,2,30.00),(6,5,1,12.00),(6,9,1,8.00),
(7,6,1,22.00),(7,3,1,10.00),(7,8,1,5.00),
(8,6,1,22.00),
(9,2,1,35.00),(9,4,1,30.00),(9,5,1,12.00),
(10,10,1,28.00),(10,9,1,8.00);
