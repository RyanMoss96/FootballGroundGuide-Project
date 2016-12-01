<?php
header('Content-type: application/json');

require_once 'db_config.php';
$json = file_get_contents('php://input');
$data = json_decode($json);

$favourite = $data->favourite;


$query = $conn->query("SELECT * FROM users WHERE username = '$username'");



if($result)
{
    if(password_verify($password, $hashedPassword))
    {
        $response["code"] = 1;
        $response["message"] = "Login Success";

        $response["uid"] = $uid;
        $response["first_name"] = $firstName;
        $response["last_name"] = $lastName;
        $response["username"] = $userName;
        $response["email"] = $email;
        $response["encrypted_password"] = $hashedPassword;
        $response["user_role"] = $userRole;

    } else {
        $response["code"] = 0;
        $response["message"] = "Incorrect Password.";
    }

} else {
 $response["code"] = 2;
$response["message"] = "Incorrect Username";

}



echo json_encode ( $response );

?>
