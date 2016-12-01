<?php
header('Content-type: application/json');

require_once 'db_config.php';
$json = file_get_contents('php://input');
$data = json_decode( $json);
	
$ground_option = $data->dataOption;


$query = $conn->query("SELECT * FROM countries");


$response = array();

        while($row = mysqli_fetch_assoc($query))
        {
            
            $response["country"][] = $row;
          
        }


echo json_encode ( $response );

?>