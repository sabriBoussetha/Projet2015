<?php
    include_once 'dbconnection.class.php';
    class JavaCommunication{
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
            $connection = new dbconnection();
            $sql = "SELECT ip_host FROM parties where available_place > 0 LIMIT 1";
            $available_game = $connection->doQuery($sql);
            echo $available_game[0]['ip_host'];
        }
        public function searchGameListJson(){
            $connection = new dbconnection();
            $sql = "SELECT ip_host,available_place FROM parties where available_place > 0";
            $available_game = $connection->doQuery($sql);
            echo json_encode($available_game);
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
        }
        public function getPlayer(){
            $connection = new dbconnection();
            $sql = "SELECT id FROM player";
            $res = $connection -> doQuery($sql);
            $array_player = array();
            $i = 0;
            foreach($res as $player){
                $id = $player["id"];
                $sql1 = "SELECT id,score_elo,pseudo,nb_partie,nb_partie_premier , date_
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
        
        
        //fonction à appeller après une manche non-gagné
        public function update_round(){
        	$parse_update_round = explode("|", $_POST['update_round']);
        	$id_player = $parse_update_round[0];
        	$player_round_points = $parse_update_round[1];
        	
        	$connection = new dbconnection();
        	
        	$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_points = nb_Points + '$player_round_points', moy_points_manche = (nb_points + '$player_round_points')/(nb_manche + 1) WHERE id ='$id_player'";
        	
        	$res = $connection->doExec($sql);
        }
        
        
        //fonction a appelle à la fin d'une manche gagné
        public function update_won_round(){
        	$parse_update_won_round = explode("|", $_POST['update_won_round']);
        	$id_player = $parse_update_won_round[0];
        	$player_round_points = $parse_update_won_round[1];
        	
        	$connection = new dbconnection();
        	
        	$sql = "UPDATE stat_joueur SET nb_manche = nb_manche + 1, nb_manche_premier = nb_manche_premier + 1, nb_points = nb_Points + '$player_round_points', moy_points_manche = (nb_points + '$player_round_points')/(nb_manche + 1) WHERE id ='$id_player'";
        	
        	$res = $connection->doExec($sql);
        }
        
        
        //fonction a appeller au début d'une partie
        public function update_match(){
        	$parse_update_match = explode("|", $_POST['update_match']);
        	$id_player = $parse_update_match[0];
        	
        	$connection = new dbconnection();
        	
        	$sql = "UPDATE stat_joueur SET nb_partie = nb_partie + 1, moy_points_partie = nb_Points/(nb_partie + 1) WHERE id = '$id_player'";
        	
        	$res = $connection->doExec($sql);
        }
        
        //on l'appelle toujours après la fonction update round si le joueur a gagné car (update round) met à jour le nb_points
        public function update_won_match(){
        	$parse_update_won_match = explode("|", $_POST['update_won_match']);
        	$id_player = $parse_update_won_match[0];
        	
        	$connection = new dbconnection();
        	
        	$sql = "UPDATE stat_joueur SET nb_partie_premier = nb_partie_premier + 1, moy_points_partie = nb_points/nb_partie WHERE id = '$id_player'";
        	
        	$res = $connection->doExec($sql);
        }
        
        

    }
