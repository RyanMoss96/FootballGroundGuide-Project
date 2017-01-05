<?php
header('Content-type: application/json');

require_once 'db_config.php';
$json = file_get_contents('php://input');
$data = json_decode( $json);
	
$ground_option = $data->country;


$query = $conn->query("SELECT leagues.league_name FROM leagues INNER JOIN countries on leagues.country_id = countries.country_id WHERE countries.country_name = '$ground_option' ");




$response = array();

while($row = mysqli_fetch_assoc($query))
{
            
    $response["leagues"][] = $row;
          
}


    
echo json_encode ( $response );

?>