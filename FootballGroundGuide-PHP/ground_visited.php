<?php
header('Content-type: application/json');

require_once 'db_config.php';
$json = file_get_contents('php://input');
$data = json_decode($json);

$image= $data->image;
$name = $data->name;

 
 $actualpath = "stadium_images/$name.png" ;
 
 
 $test = file_put_contents($actualpath,base64_decode($image));
$response = array();

        $response["code"] = $image;
        




echo json_encode ( $response );


 

?>