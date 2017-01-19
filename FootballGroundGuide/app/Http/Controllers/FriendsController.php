<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class FriendsController extends Controller
{
    public function show($username) {
         $users = DB::table('users')->where('username', $username)->first();
         return response()->json($users);
    }
}
