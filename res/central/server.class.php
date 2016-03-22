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
        public function resetGame(){
            $ip_host = $_POST['reset_game'];
            
            $connection = new dbconnection();
            $sql = "UPDATE parties set available_place=6 where ip_host = '$ip_host'";
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
            return $res ;
            
        }
    }
