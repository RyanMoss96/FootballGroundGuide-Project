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
   
}
