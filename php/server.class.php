<?php
    include_once 'dbconnection.class.php';
    class JavaCommunication{
        public function addNewGame(){
            if(isset($_POST['infos'])){
                $parse_infos = explode("|",$_POST['infos']);
                $ip_host = $parse_infos[0];
                $nb_player = $parse_infos[1];
                $available_place = $nb_player-1;
            }
            $connection = new dbconnection();
            $serverExist = JavaCommunication::ifServerExist($ip_host);
            if($serverExist){
                echo "La partie existe déjà";
            }
            else{
                $sql = "INSERT INTO parties (ip_host, nb_player,available_place) VALUES('$ip_host','$nb_player','$available_place')";
                $res = $connection->doExec($sql);
                if($res === false)
                    return false ;
                return $res ;
            }
            return false;
        }
        public function deleteGame($ip){
            $connection = new dbconnection();
            $sql = "DELETE FROM parties WHERE ip_host = '$ip'";
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
            $connection = new dbconnection();
            if(isset($_POST['nb_player_update'])){   // Modification des places restantes lors du choix des joueurs
                $new_nb_player = $_POST['nb_player_update'];
                $sql = "UPDATE parties set available_place= '$nb_local_player' where ip_host =$ip'";
                $res = $connection->doExec($sql);
                if($res === false)
                    return false ;
                return $res ;
            }
        }
    }