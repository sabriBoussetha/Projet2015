<?php
    include_once 'dbconnection.class.php';
    include('elo.php');
    class JavaCommunication{
        /******* Fonctions écrites par Nathan Cheval *******/
        
        /*
            Fonction permettant la vérification du login et du mot de passe local (dans le fichier profils.txt) 
            avec ceux stockés dans la base de données player            
        */
        public static function getUserByLoginAndPass($login,$pass){
            $connection = new dbconnection() ;
            $sql = "select * from player where pseudo='".$login."' and password='".sha1($pass)."'" ;
            $res = $connection->doQuery( $sql );

            if($res === false)
              return false ;

            return $res ;
        }
        /*
            Fonction permettant l'ajout d'une nouvelle partie dans la base de données parties
        */
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
        /*
            Fonction permettant la suppression d'une partie, dans la base de donnée parties,à la fin de celle-ci
        */
        public function deleteGame(){
            $ip_host = $_POST['delete_game'];
            
            $connection = new dbconnection();
            $sql = "DELETE FROM parties WHERE ip_host = '$ip_host'";
            $res = $connection->doExec($sql);
            if($res === false)
                return false ;
            return $res ;
        }
        /*
            Fonction qui vérifie l'existence d'un serveur afin d'éviter les doublons dans la base de données parties
        */
        public function ifServerExist($ip){
            $bdd = new PDO('pgsql:host=localhost dbname=etd user=uapv1402577 password=jenYv1');
            $ifExist = $bdd -> query("SELECT * FROM parties WHERE ip_host = '$ip'")->fetchColumn();
            if($ifExist === false)
                return false ;
            return true;  
        }
        /*
            
        */
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
            $parse_search_game = explode("|",$_POST['search_game_json']);
            $login = $parse_search_game[0];
            $pass = $parse_search_game[1];
            if(JavaCommunication::getUserByLoginAndPass($login,$pass)){
                $connection = new dbconnection();
                $sql = "SELECT ip_host,available_place FROM parties where available_place > 0";
                $available_game = $connection->doQuery($sql);
                $listServers = fopen('listServers.json', 'r+');
                fputs($listServers, json_encode($available_game)); 
            }
            else echo "false";

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
                echo $id_joueur;
            }

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

        
        /******* Fonctions écrites par Charlie Brugvin *******/
                
        //supprime le player de la bdd
        //en supprimant aussi ces valeurs statistiques
        public function deletePlayer()
        {
            $id = $_POST["delete_player"];
            $connection = new dbconnection();

            $sql1 = "DELETE FROM player WHERE id=$id";
            $connection-> doQuery($sql1);
            $sql2 = "DELETE FROM stat_joueur WHERE id=$id";
            $connection-> doQuery($sql2);
            $sql3 = "DELETE FROM stat_elo WHERE id=$id";
            $connection-> doQuery($sql3);


        }
        
        //renvoie les elos associé a leurs dates dans le format JSON
        //pour un joueur donné
        public function getElo()
        {
            $id = $_POST['get_elo'];

            $connection = new dbconnection();
            $sql = "SELECT score_elo, date_ FROM stat_elo where id=$id order by date_ asc";
            $res = $connection->doQuery($sql);

            echo json_encode($res); 
        }

        //retourne le pseudo d'un id passé en post
        public function getPseudo()
        {
            $id = $_POST['get_pseudo'];
            
            $connection = new dbconnection();
            $sql = "SELECT pseudo FROM player where id=$id";
            $res = $connection->doQuery($sql);

            echo $res[0]['pseudo'];
        }

        /******* Fonctions écrites par Alexandre Latif *******/
        
        public function updateManche(){
               	$parse_update_manche = explode("|",$_POST['update_manche']);
       			$id = $parse_update_manche[0];
            	$score = $parse_update_manche[1];
            	$raison_mort = $parse_update_manche[3];
            	
            	
            	$connection = new dbconnection();
            	
            	if ($raison_mort == "en vie"){
            		$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_manche_premier = nb_manche_premier + 1 , nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS FLOAT)/(nb_manche + 1) WHERE id=$id";
            	}
            	
            	else{
            		if($raison_mort == "bord"){
            			$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS FLOAT)/(nb_manche + 1), mort_bord = mort_bord + 1 WHERE id=$id";
            		}
            		
            		else if ($raison_mort = "lui meme"){
            			$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS FLOAT)/(nb_manche + 1), mort_soi_meme = mort_soi_meme + 1 WHERE id=$id";
            		}
            		
            		else{
            			$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_points = nb_points + $score, moy_points_manche = cast(nb_points + $score AS FLOAT)/(nb_manche + 1), mort_autre = mort_autre + 1 WHERE id=$id";
            		}
            	
            	}
               $res = $connection->doExec($sql);

        }
        
        

        public function updateMatch(){
			$parse_update_match = explode("|",$_POST['update_match']);
            $nb_joueur = (int)$parse_update_match[0];
            
            $connection = new dbconnection();
            
            $tab = array();
            for ($id=0; $id < $nb_joueur; $id++){
            	$tab[$id] = (int)$parse_update_match[$id+1];
            }
            
            for ($classement=0; $classement < $nb_joueur; $classement++){
            	
            	if ($classement == 0){
            		$sql = "UPDATE stat_joueur SET nb_partie = nb_partie + 1, nb_partie_premier = nb_partie_premier + 1, moy_points_partie = cast(nb_points AS FLOAT)/(nb_partie + 1) WHERE id=$tab[$classement]";
            		
            	}
            	else{
            		$sql = "UPDATE stat_joueur SET nb_partie = nb_partie + 1, moy_points_partie = cast(nb_points AS FLOAT)/(nb_partie + 1) WHERE id=$tab[$classement]";
            	}

           		$res = $connection->doExec($sql);
            }
            majElo($tab);
            
        }


    }
