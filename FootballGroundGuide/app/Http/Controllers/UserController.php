<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;

class UserController extends Controller
{

    
    public function index()
    {
        $users = DB::select('select * from users');
        return $users;
    }

    public function show($username) {
         $users = DB::table('users')->where('username', $username)->first();

         
         return response()->json($users);
    }

    public function register(Request $request) {
         $data = $request->all();

        $username = $data['username'];
        $firstname = $data['first_name'];
        $lastname = $data['last_name'];
        $email = $data['email'];
        $password = $data['password'];

        $hash = password_hash($password, PASSWORD_BCRYPT);

        DB::table('users')->insert(
            ['firstname' => $firstname, 'lastname' => $lastname, 'username' => $username, 'email' => $email, 'encrypted_password' => $hash]
        );

        $response = array();

        $response["code"] = "1";
        $response["message"] = "Account Created Successfully";
        
        echo json_encode ( $response );

    }

    public function login(Request $request) {
        $data = $request->all();

        $username = $data['username'];
        $password = $data['password'];

        $users = DB::table('users')->where('username', $username)->get();

        
        $response = array();

        if($users->count())
        {   
             foreach($users as $user) {
                $uid = $user->uid;
                $firstName = $user->firstname;
                $lastName = $user->lastname;
                $userName = $user->username;
                $email = $user->email;
                $hashedPassword = $user->encrypted_password;
            }

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
                

            } else {
                $response["code"] = 0;
                $response["message"] = "Incorrect Password.";
            }

        } else {

            $response["code"] = 2;
            $response["message"] = "Incorrect Username";
        }

        echo json_encode ( $response );
    }

    public function followers($id) {
 
    $followers = DB::table('users')
                    ->select('username')
                    ->join('friends', 'friends.user_two', '=', 'users.uid')
                    ->where('friends.user_one', $id)
                    ->get();

            $response = array();

        foreach($followers as $follower) {
            $response["follower"][] = $follower;
        }

        return response()->json($response);
    }

    public function favourite($user) {
        $teams = DB::table('teams')
                    ->select('team_name')
                    ->join('favourite_team', 'favourite_team.team_id', '=', 'teams.team_id')
                    ->where('favourite_team.uid', $user)
                    ->get();

            $response = array();

        
        foreach($teams as $team) {
            $response["favourite"][] = $team;
        }
        echo json_encode ( $response );
    }

    public function images($user) {
        $images = DB::table('images')->select('image_url')->where('user_id', $user)->get();


  $response = array();

        
        foreach($images as $image) {
            $response["images"][] = $image;
        }
        echo json_encode ( $response );
        
    }
   
}
