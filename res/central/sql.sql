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
nb_partie_premier INT DEFAULT 0
);

CREATE TABLE stat_elo
(
id SERIAL,
date_ TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
score_ELO INT DEFAULT 2000
);

insert into player (pseudo, country, password) values ('charlie', 'france', 'mdp');
insert into stat_joueur(id, nb_partie, nb_partie_premier) values (1, 10,1);
insert into stat_elo (id, score_ELO) values (1,2000);
insert into stat_elo (id, score_ELO) values (1,2100);

insert into player (pseudo, country, password) values ('sabri', 'algerie', 'mdp');
insert into stat_joueur(id, nb_partie, nb_partie_premier) values (2, 10, 0);
insert into stat_elo (id, score_ELO) values (2,2000);
insert into stat_elo (id, score_ELO) values (2,1900);


insert into player (pseudo, country, password) values ('alex', 'syrie', 'mdp');
insert into stat_joueur(id, nb_partie, nb_partie_premier) values (3, 10, 5);
insert into stat_elo (id, score_ELO) values (3,2000);
insert into stat_elo (id, score_ELO) values (3,1800);

insert into player (pseudo, country, password) values ('nathan', 'français', 'mdp');
insert into stat_joueur(id, nb_partie, nb_partie_premier) values (4, 10, 5);
insert into stat_elo (id, score_ELO) values (4,2000);
insert into stat_elo (id, score_ELO) values (4,1700);

