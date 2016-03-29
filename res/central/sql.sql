drop table player;
drop table stat_joueur;
drop table stat_elo;

CREATE TABLE player
(
id SERIAL PRIMARY KEY,
pseudo VARCHAR(50),
country VARCHAR(50),
password VARCHAR(50)
);

CREATE TABLE stat_joueur
(
id SERIAL ,
nb_partie INT DEFAULT 0,
nb_partie_premier INT DEFAULT 0,
nb_manche INT DEFAULT 0,
nb_manche_premier INT DEFAULT 0,
nb_points INT DEFAULT 0,
moy_points_partie FLOAT DEFAULT 0,
moy_points_manche FLOAT DEFAULT 0
);

CREATE TABLE stat_elo
(
id SERIAL,
date_ TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
score_ELO INT DEFAULT 2000
);

