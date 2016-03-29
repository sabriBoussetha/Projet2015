<?php

//Algorithmique et implémentation des méthodes réalisé par Charlie

//recuperation et envoi avec la base de donnée réalisé par Charlie et Alexandre
//une fois la base de donnée finalisé


//met a jour les elos a l'issue d'une partie
//issue partie est le classement de la partie
//[0] -> premier [1] -> second, ...
//le tableau contient les id des joueurs
function majElo($issuePartie)
{

	//recuperation des donnes elo de la bdd
	//dans un tablea associatif : id => derniere valeur elo
	$elo = array();

	$connection = new dbconnection();
	foreach ($issuePartie as $idJoueur){
		$sql = 
		 "SELECT id, score_elo 
		  from player natural join stat_elo 
		  where id=$idJoueur AND date_ >= ALL (SELECT date_ FROM stat_elo WHERE id=$idJoueur)";
		
		$res = $connection->doQuery($sql);
		$elo[ $res[0]["id"] ] = $res[0]["score_elo"];
	}
	
	//pour chaque joueurs
	for ($i = 0; $i < count($issuePartie); $i++)
	{
		$idJoueur = $issuePartie[$i];

		//calcul de sa probabilité de succès
		$proba_succes = proba_succes_multi($elo, $idJoueur);
		//calcul de son coeficient de reussite
		$coef_reussite = coef_reussite(count($issuePartie), $i+1);
		//recuperation de son ancien elo
		$ancien_elo = $elo[$idJoueur];
		//mise a jour de son elo dans le tableau
		$elo[$idJoueur] = newElo($ancien_elo, $coef_reussite, $proba_succes);
	}

	//enregistrement des nouveaux elo dans la base de donnée
	foreach ($elo as $id => $valueElo) {
	    $date = date('Y-m-d H:i:s');
	    echo "id : $id, date : $date, elo : $valueElo";
	    $sql = "INSERT INTO stat_elo VALUES($id,'$date',$valueElo)";
	    echo $sql;
	    $connection->doExec($sql);
	}

}

//calculer la probabilite moyenne de succes pour un joueur
//on a juste besoin des elo des joueurs
function proba_succes_multi($elo, $idJoueur)
{
	$proba_succes = 0;

	//on enumere les elos de tout les joueurs autres que idJoueur
	foreach ($elo as $id => $eloValue) {
		if ($idJoueur != $id)
		{
			//on fait la somme de la proba de succes du joueurs contre tout les autres
			$proba_succes += proba_succes_1vs1($eloValue, $elo[$idJoueur]);
		}
	}
	//le numerateur correspond au nombre totale de sous parties
	return $proba_succes / (count($elo) * ((count($elo) -1) / 2));
}

//calcule un nouveau classement elo a partir de son ancient, de son coeficient de reussite et de sa probabilité de succès
function newElo($lastElo, $coef_reussite, $proba_succes)
{
	return (int) ($lastElo + 30 * ($coef_reussite - $proba_succes));
}

//renvoie la probabilité de succès du joueur ayant un classement elo1
//quand il joue contre un joueur ayant un classement elo2
function proba_succes_1vs1($elo1, $elo2)
{
	return 1.0 / (1.0 + pow(10, ($elo1 - $elo2)/400));
}

//calcule du coefficient de reussite
function coef_reussite($nbJoueur, $classement)
{
	return ($nbJoueur - $classement) / (($nbJoueur * ($nbJoueur-1))/2);
}

?>
