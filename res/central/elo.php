<?php


//met a jour les elos a l'issue d'une partie
//issue partie est le classement de la partie
//[0] -> premier [1] -> second, ...
//le tableau contient les id des joueurs
function majElo($issuePartie)
{

/*
	//recuperation des elo dans la bdd
	$elo = array(
		101 => 2300,
		102 => 2300,
		103 => 1900,
		);*/

	
	//recupereation des donnes elo de la bdd
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
	
	var_dump($elo);
	

//calcule du coefficient de reussite
	for ($i = 0; $i < count($issuePartie); $i++)
	{
		$idJoueur = $issuePartie[$i];

		//echo "id : $i  idjoueur : $idJoueur elo : $elo[$idJoueur]"; 
		//echo "\n";
		//pour chaque joueurs
		$proba_succes = proba_succes_multi($elo, $idJoueur);
		//echo "proba succes : $proba_succes";
		//echo "\n";
		$coef_reussite = coef_reussite(count($issuePartie), $i+1);
		//echo "coef reussite : $coef_reussite";
		//echo "\n";
		$ancien_elo = $elo[$idJoueur];
		//echo "ancien elo : $ancien_elo";
		//echo "\n";
		$elo[$idJoueur] = newElo($ancien_elo, $coef_reussite, $proba_succes);
	}

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
