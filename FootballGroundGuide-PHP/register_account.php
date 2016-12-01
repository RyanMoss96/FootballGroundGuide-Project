<?php
header('Content-type: application/json');

require_once 'db_config.php';
$json = file_get_contents('php://input');
$data = json_decode( $json);
	
$username = $data->username;
$firstname = $data->first_name;
$lastname = $data->last_name;
$email = $data->email;
$password = $data->password;

$hash = password_hash($password, PASSWORD_DEFAULT);

$query = $conn->query("INSERT INTO users (username, firstname, lastname, email, encrypted_password)
VALUES ( '$username','$firstname', '$lastname' , '$email', '$hash')");


$response = array();
if($query)
{
    $response["success"] = 1;
    $response["message"] = "Account successfully created.";
} else {
    $response["success"] = 0;
    $response["message"] = "Error creating your account. Please try again.";
}

echo json_encode ( $response );

?>