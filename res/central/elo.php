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
	$
	for ($issuePartie as $idJoueur){
		$sql = 
		 "SELECT id, score_elo 
		  from player natural join stat_elo 
		  where id=$idJoueur AND date_ >= ALL (SELECT date_ FROM stat_elo WHERE id=$idJoueur)";
		
		$res = $connection->doQuery($sql);
		$elo[ $res[0]["id"] ] = $res[0]["score_elo"];
	}
	

//calcule du coefficient de reussite
	for ($i = 0; $i < count($issuePartie); $i++)
	{
		$idJoueur = $issuePartie[$i];

		echo "<p style='color:red'> id : $i  idjoueur : $idJoueur elo : $elo[$idJoueur]</p>"; 
		//pour chaque joueurs
		$proba_succes = proba_succes_multi($elo, $idJoueur);
		echo "<p> proba succes : $proba_succes</p>";
		$coef_reussite = coef_reussite(count($issuePartie), $i+1);
		echo "<p> coef reussite : $coef_reussite </p>";
		$ancien_elo = $elo[$idJoueur];
		echo "<p> ancien elo : $ancien_elo </p>";
		$elo[$idJoueur] = newElo($ancien_elo, $coef_reussite, $proba_succes);
	}

	for ($i=0; $i < count($elo); $i++)
	{
		$id = 
	//foreach ($elo as $id => $elo) {
	    $date = date('d-m-Y, H:i:s');
	    $sql = "INSERT INTO stat_elo (id,date_,score_elo) VALUES($id,$date,$elo)";
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
