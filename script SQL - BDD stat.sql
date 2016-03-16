---- SCRIPT DE CREATION DE LA BDD pour les TABLES STATISTIQUES ----

--- on supprimer les bases si elles existent deja ---

drop table stat_joueur;
drop table stat_elo;

--- on les crées ---

CREATE TABLE stat_joueur
(
id_joueur SERIAL PRIMARY KEY,
pseudo VARCHAR(30),
nb_partie INT DEFAULT 0,
nb_partie_premier INT DEFAULT 0
);

CREATE TABLE stat_elo
(
id_joueur INT,
date_ TIMESTAMP  DEFAULT now(),
score_ELO INT DEFAULT 2000
);

--- on ajoute des valeurs brut pour le debug ---

-- on insere deux joueur dans la table stat_joueur ---
insert into stat_joueur (pseudo)
values ('waldo');

insert into stat_joueur (pseudo)
values ('perda');

insert into stat_joueur (pseudo)
values ('alex');

-- on insere des valeurs elo par defaut ---

insert into stat_elo (id_joueur)
values (1);

insert into stat_elo (id_joueur)
values (2);

insert into stat_elo (id_joueur)
values (3);

--- on insere des fausses valeurs elo pour le debug ---

insert into stat_elo (id_joueur, score_elo)
values (1, 2100);

insert into stat_elo (id_joueur, score_elo)
values (1, 2150);

insert into stat_elo (id_joueur, score_elo)
values (3, 1900);



---- commandes varies ----

--recuperer tout les classements elo d'un joueur a partir de son pseudo , ou id
SELECT date_, score_elo
FROM stat_elo NATURAL JOIN stat_joueur
WHERE pseudo = 'alex' -- id = ...
ORDER BY date_ DESC;

--recuperer le dernier classement ELO d'un joueur par son pseudo
SELECT score_elo
FROM stat_elo NATURAL JOIN stat_joueur
WHERE pseudo = 'alex'
AND date_ >= ALL (
       SELECT date_
       FROM stat_elo NATURAL JOIN stat_joueur
       WHERE pseudo = 'alex'
       )
;

---- SCRIPT DE CREATION DE LA BDD pour les parties existantes ----

drop table parties;


CREATE TABLE parties
(
id SERIAL PRIMARY KEY,
ip_host VARCHAR(50),
nb_player_max INT,
available_places INT
);
