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

    public function store(Request $request) {
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

    }
   
}
