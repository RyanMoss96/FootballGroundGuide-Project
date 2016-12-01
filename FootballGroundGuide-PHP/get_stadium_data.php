<?php
header('Content-type: application/json');

require_once 'db_config.php';
$json = file_get_contents('php://input');
$data = json_decode( $json);
	
$ground_option = $data->stadium;


$query = $conn->query("SELECT * FROM teams WHERE team_name = '$ground_option'");


$response = array();

        while($row = mysqli_fetch_assoc($query))
        {
            
            $response["stadium"][] = $row;
          
        }


echo json_encode ( $response );

?>