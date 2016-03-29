
----- SCRIPT DE CREATION DE LA BDD TABLES STATISTIQUES ----


--realisé par Alexandre et Charlie
drop table stat_joueur;
CREATE TABLE stat_joueur
(
id SERIAL ,
nb_partie INT DEFAULT 0,
nb_partie_premier INT DEFAULT 0,
nb_manche INT DEFAULT 0,
nb_manche_premier INT DEFAULT 0,
nb_points INT DEFAULT 0,
moy_points_partie FLOAT DEFAULT 0,
moy_points_manche FLOAT DEFAULT 0,
mort_bord INT DEFAULT 0,
mort_autre INT DEFAULT 0,
mort_soi_meme INT DEFAULT 0
);


--réalisé par Charlie
drop table stat_elo;
CREATE TABLE stat_elo
(
id SERIAL,
date_ TIMESTAMP  DEFAULT NULL,
score_ELO INT DEFAULT 2000
);

---- SCRIPT DE CREATION DE LA BDD pour les parties existantes ----
-- realisé par Nathan

drop table parties;

CREATE TABLE parties
(
id SERIAL PRIMARY KEY,
ip_host VARCHAR(50),
nb_player_max INT,
available_places INT
);


---- SCRIPT DE CREATION DE LA BDD pour les joueurs ----
-- realisé par Nathan
drop table player;


CREATE TABLE player
(
id SERIAL PRIMARY KEY,
pseudo VARCHAR(50),
country VARCHAR(50),
password VARCHAR(50)
);
