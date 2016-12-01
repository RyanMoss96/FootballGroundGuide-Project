<?php
header('Content-type: application/json');

require_once 'db_config.php';
$json = file_get_contents('php://input');
$data = json_decode( $json);
	
$league_option = $data->league;


$query = $conn->query("SELECT teams.team_name FROM teams INNER JOIN leagues on teams.league_id=leagues.league_id WHERE leagues.league_name = '$league_option' ");




$response = array();

while($row = mysqli_fetch_assoc($query))
{
            
    $response["grounds"][] = $row;
          
}


    
echo json_encode ( $response );

?>