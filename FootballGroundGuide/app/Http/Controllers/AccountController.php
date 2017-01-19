<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class AccountController extends Controller
{
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

   
    public function show()
    {
       $data = $request->all();

        $username = $data['username'];
        $password = $data['password'];

    }  
}
