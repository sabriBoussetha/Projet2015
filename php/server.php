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
    else if(isset($_POST['reser_game'])){
        JavaCommunication::resetGame();
    }
    
?>