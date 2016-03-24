<?php 
    include_once 'server.class.php';
    if(isset($_POST['new_game'])){
        echo "Ajout d'une nouvelle partie";
        JavaCommunication::addNewGame();  
    }
    else if(isset($_POST['new_nb_player'])){
        echo "Modification du nombre de joueurs locaux";
        JavaCommunication::updateGame();
    }
    else if(isset($_POST['search_game'])){
        JavaCommunication::searchGame();
    }
	else if(isset($_POST['search_game_json'])){
        JavaCommunication::searchGameListJson();
    }
    else if(isset($_POST['reset_game'])){
        echo "Remise à zero du nombre de places disponible";
        JavaCommunication::resetGame();
    }
    else if(isset($_POST['delete_game'])){
        echo "Suppression de la partie";
        JavaCommunication::deleteGame();
    }
    else if(isset($_POST['modif_player'])){
        echo "Modification du nombre joueurs dans la table";
        JavaCommunication::modifPlayer();
    } 
    else if(isset($_POST['add_player'])){
        echo "Ajout d'un joueurs dans la table";
        JavaCommunication::addPlayer();
    }
    
?>