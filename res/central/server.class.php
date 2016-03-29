<?php
    include_once 'dbconnection.class.php';

    class JavaCommunication{
        public static function getUserByLoginAndPass($login,$pass){
            $connection = new dbconnection() ;
            $sql = "select * from player where pseudo='".$login."' and password='".sha1($pass)."'" ;
            $res = $connection->doQuery( $sql );

            if($res === false)
              return false ;

            return $res ;
        }
        public function addNewGame(){
            $parse_new_game = explode("|",$_POST['new_game']);
            $ip_host = $parse_new_game[0];
            $nb_player = $parse_new_game[1];
            $connection = new dbconnection();
            $serverExist = JavaCommunication::ifServerExist($ip_host);
            if($serverExist){
                echo "La partie existe déjà";
            }
            else{
                $sql = "INSERT INTO parties (ip_host, nb_player,available_place) VALUES('$ip_host','$nb_player','$nb_player')";
                $res = $connection->doExec($sql);
                if($res === false)
                    return false ;
                return $res ;
            }
            return false;
        }
        public function deleteGame(){
            $ip_host = $_POST['delete_game'];
            
            $connection = new dbconnection();
            $sql = "DELETE FROM parties WHERE ip_host = '$ip_host'";
            $res = $connection->doExec($sql);
            if($res === false)
                return false ;
            return $res ;
        }
        public function ifServerExist($ip){
            $bdd = new PDO('pgsql:host=localhost dbname=etd user=uapv1402577 password=jenYv1');
            $ifExist = $bdd -> query("SELECT * FROM parties WHERE ip_host = '$ip'")->fetchColumn();
            if($ifExist === false)
                return false ;
            return true;  
        }
        public function updateGame(){
            $parse_update_game = explode("|",$_POST['new_nb_player']);
            $ip_host = $parse_update_game[0];
            $nb_local_player = $parse_update_game[1];
            
            $connection = new dbconnection();
            $sql = "SELECT available_place from parties where ip_host='$ip_host'";
            
            $actual_available_place = $connection->doQuery($sql); // Récupération du nombre de places disponible actuelle
            echo "<br> Nombre de joueur actuel :";
            echo $actual_available_place[0]['available_place'];
            echo "<br> Nombre de joueur à enlever :$nb_local_player";
            $new_nb_player = $actual_available_place[0]['available_place'] - $nb_local_player;
            echo "<br> Nouveau nombre de joueurs :$new_nb_player";
            $sql1 = "UPDATE parties set available_place = '$new_nb_player' where ip_host = '$ip_host'";
            $res = $connection->doExec($sql1);
            if($res === false)
                return false ;
            return $res ;
        }
        public function searchGame(){
            $parse_search_game = explode("|",$_POST['search_game']);
            $login = $parse_search_game[0];
            $pass = $parse_search_game[1];
            if(JavaCommunication::getUserByLoginAndPass($login,$pass)){
                $connection = new dbconnection();
                $sql = "SELECT ip_host FROM parties where available_place > 0 LIMIT 1";
                $available_game = $connection->doQuery($sql);
                echo $available_game[0]['ip_host'];   
            }
            else{
                echo "false";
            }
        }
        public function searchGameListJson(){
            $connection = new dbconnection();
            $sql = "SELECT ip_host,available_place FROM parties where available_place > 0";
            $available_game = $connection->doQuery($sql);
            $listServers = fopen('listServers.json', 'r+');
            fputs($listServers, json_encode($available_game));        
        }
        public function resetGame(){
            $ip_host = $_POST['reset_game'];
            $connection = new dbconnection();
            $sql1 = "SELECT nb_player FROM parties where ip_host='$ip_host'";
            $exec = $connection->doQuery($sql1);
            $max_place = $exec[0]['nb_player'];
            $sql = "UPDATE parties set available_place='$max_place' where ip_host = '$ip_host'";
            $res = $connection->doExec($sql);
            if($res === false)
                return false ;
            return $res ;
        }
        public function modifPlayer(){
            $parse_modif_player = explode("|",$_POST['modif_player']);
            $ip_host = $parse_modif_player[0];
            $nb_player = $parse_modif_player[1];
            
            $connection = new dbconnection();
            $sql = "SELECT available_place from parties where ip_host='$ip_host'";
            $actual_available_place = $connection->doQuery($sql); // Récupération du nombre de places disponible actuelle
            $new_nb_player = $actual_available_place[0]['available_place'] + $nb_player;
            $sql = "UPDATE parties set available_place = '$new_nb_player' where ip_host = '$ip_host'";
            $res = $connection->doExec($sql);
            if($res === false)
                return false ;
            return $res;
        }

        public function addPlayer(){
            $parse_add_player = explode("|",$_POST['add_player']);
            $pseudo = $parse_add_player[0];
            $country = $parse_add_player[1];
            $password = sha1($parse_add_player[2]);
            $connection = new dbconnection();

            //d'abord on voit si le pseudo existe deja
            $res = $connection->doQuery("SELECT count(pseudo) from player where pseudo='$pseudo'");
            
            if ($res[0]["count"] > 0)
            {
                echo -1;
            }
            else
            {
                $sql = "INSERT INTO player (pseudo, country,password) VALUES('$pseudo','$country','$password')";
                $res = $connection->doExec($sql);
                $sql1 = "SELECT id FROM player WHERE pseudo = '$pseudo'";
                $res1 = $connection->doQuery($sql1);
                $id_joueur = $res1[0]['id'];
                $sql2 = "INSERT INTO stat_joueur (id) VALUES ('$id_joueur')";
                $res2 = $connection->doExec($sql2);
                $date = date('d-m-Y, H:i:s');
                $sql3 = "INSERT INTO stat_elo (id,date_) VALUES ('$id_joueur','$date')";
                $res3 = $connection->doExec($sql3);

                echo "$id_joueur"; 
            }

        }

        public function deletePlayer()
        {
            $id = $_POST["delete_player"];
            $connection = new dbconnection();

            /*
            $sql = "INSERT INTO player (pseudo, country,password) VALUES('$pseudo','$country','$password')";

            $sql2 = "INSERT INTO stat_joueur (id) VALUES ('$id_joueur')";

            $sql3 = "INSERT INTO stat_elo (id,date_) VALUES ('$id_joueur','$date')";
            */

            $sql1 = "DELETE FROM player WHERE id=$id";
            $connection-> doQuery($sql1);
            $sql2 = "DELETE FROM stat_joueur WHERE id=$id";
            $connection-> doQuery($sql2);
            $sql3 = "DELETE FROM stat_elo WHERE id=$id";
            $connection-> doQuery($sql3);

            echo "delete player lancé";


        }


        public function getPlayer(){
            $connection = new dbconnection();
            $sql = "SELECT id FROM player";
            $res = $connection -> doQuery($sql);
            $array_player = array();
            $i = 0;
            foreach($res as $player){
                $id = $player["id"];
                $sql1 = "SELECT id,score_elo,pseudo,nb_partie,nb_partie_premier, nb_manche, nb_manche_premier, nb_points, mort_bord, mort_autre, mort_soi_meme, date_
                    FROM player NATURAL JOIN stat_joueur NATURAL JOIN stat_elo  
                    WHERE id='$id' AND date_ >= ALL (SELECT date_ FROM stat_elo WHERE id='$id')";
                $res1 = $connection->doQuery($sql1);
                $array_player[$i] = $res1;
                $i++;
            }
            echo json_encode($array_player);            
        } 
        public function addElo(){
            $parse_add_elo = explode("|",$_POST['add_elo']);
            $id_joueur = $parse_add_elo[0];
            $elo_joueur = $parse_add_elo[1];
            $connection = new dbconnection();
            $date = date('d-m-Y, H:i:s');
            $sql = "INSERT INTO stat_elo (id,date_,score_elo) VALUES('$id_joueur','$date','$elo_joueur')";
            $res = $connection->doExec($sql);
        }

        public function getElo()
        {
            $id = $_POST['get_elo'];

            $connection = new dbconnection();
            $sql = "SELECT score_elo, date_ FROM stat_elo where id=$id order by date_ asc";
            $res = $connection->doQuery($sql);

            //res est un tableau de tableau associatif
            //on va le mettre dans un tableau classique

            /*
            foreach ($res as $i => $ligne) {
                $tab[] = $ligne["score_elo"];
            }*/

            echo json_encode($res); 
        }

        public function getPseudo()
        {
            $id = $_POST['get_pseudo'];

            $connection = new dbconnection();
            $sql = "SELECT pseudo FROM player where id=$id";
            $res = $connection->doQuery($sql);

            echo $res[0]['pseudo'];
        }

        
        public function updateManche(){
               $parse_update_manche = explode("|",$_POST['update_manche']);
       		   $id = $parse_update_manche[0];
            	$score = $parse_update_manche[1];
            	$raison_mort = $parse_update_manche[3];
            	
            	
            	$connection = new dbconnection();
            	
            	if ($raison_mort == "en vie"){
            	echo "en vie";
            		$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_manche_premier = nb_manche_premier + 1 , nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS float)/(nb_manche + 1) WHERE id=$id";
            	}
            	
            	else{
            		if($raison_mort == "bord"){
            		echo "bord";
            			$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS float)/(nb_manche + 1), mort_bord = mort_bord + 1 WHERE id=$id";
            		}
            		
            		else if ($raison_mort = "lui meme"){
            		echo "lui meme";
            			$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS float)/(nb_manche + 1), mort_soi_meme = mort_soi_meme + 1 WHERE id=$id";
            		}
            		
            		else{
            		echo "autre";
            			$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS float)/(nb_manche + 1), mort_autre = mort_autre + 1 WHERE id=$id";
            		}
            	
            	}
               echo "sql : $sql";
               $res = $connection->doExec($sql);
               //echo " id = var_dump($id), score = var_dump($score), gagne = var_dump($gagne), raison = var_dump($raison_mort)";
               
        }
        
        

        public function update_won_match(){
            $id_player =$_POST['update_won_match'];
            
            $connection = new dbconnection();
            
            $sql = "UPDATE stat_joueur SET nb_partie_premier = nb_partie_premier + 1 WHERE id = '$id_player'";
            
            //$sql = "UPDATE stat_joueur SET nb_partie_premier = nb_partie_premier + 1, moy_points_partie = nb_points/nb_partie WHERE id = '$id_player'";
            
            $res = $connection->doExec($sql);
        }

        //met a jour les elos a l'issue d'une partie
        //issue partie est le classement de la partie
        //[0] -> premier [1] -> second, ...
        //le tableau contient les id des joueurs
        public function majElo($issuePartie)
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
            foreach ($issuePartie as $key => $idJoueur) {
                $sql = 
                 "SELECT id, score_ELO 
                  from player natural join stat_elo 
                  where id=$idJoueur AND date_ >= ALL (SELECT date_ FROM stat_elo WHERE id=$idJoueur)";

                $res = $connection->doQuery($sql);
                var_dump($res);
                $elo[ $res[0] ] = $res[1];
            }
            

        //calcule du coefficient de reussite
            foreach ($issuePartie as $i => $idJoueur)
            {
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

            foreach ($elo as $id => $elo) {
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

        //FIN ELO

    }
