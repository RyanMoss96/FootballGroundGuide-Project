<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;

class FriendsController extends Controller
{
    public function show($username) {
         $users = DB::table('users')->where('username', $username)->first();
         return response()->json($users);
    }

    public function visited($user){
        $users = DB::table('visited_stadiums')->where('user_id', $user)->GET();
         

        $response = array();

       $counter = 0;
        foreach($users as $user) {
            $counter++;
        }
return $counter;
         
    }


    public function add(Request $data) {
         $user = $data->user;
         $uid = $data->uid;
         $users = DB::table('users')->where('username', $user)->first();

        
                $id = $users->uid;
                
            

         DB::table('friends')->insert(
            ['user_one' => $uid, 'user_two' => $id]
        );

    }
}
