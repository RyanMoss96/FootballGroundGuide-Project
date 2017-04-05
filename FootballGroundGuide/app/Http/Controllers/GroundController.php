<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;

class GroundController extends Controller
{
    public function show($league)
    {
       $teams = DB::table('teams')
                    ->select('team_name')
                    ->join('leagues', 'leagues.league_id', '=', 'teams.league_id')
                    ->where('leagues.league_name', $league)
                    ->get();

                    

        $response = array();

        foreach($teams as $team) {
            $response["teams"][] = $team;
        }

        return response()->json($response);
    }  

    public function data($team)
    {
        

    $data = DB::table('team_details')
                    ->select('*')
                    ->join('teams', 'teams.team_id', '=', 'team_details.team_id')
                    ->where('teams.team_name', $team)
                    ->get();

    $response = array();

        
        foreach($data as $team) {
            $response["stadium"][] = $team;
        }

        return response()->json($response);
    }

    public function visited(Request $data)
    {
        $image= $data->image;
        $name = $data->name;
        $rating = $data->rating;
        $ground = $data->ground;
        $user = $data->user;

        
 
        $actualpath = public_path() . "/stadium_images/" . $name . ".png";
        $path = "/stadium_images/" . $name . ".png";
        $test = file_put_contents($actualpath,base64_decode($image));

        $id = DB::table('reviews')->insertGetId(
            ['team_id' => $ground, 'user_id' => $user, 'overall_score' => $rating, 'user_review' => $name]
        );

        DB::table('visited_stadiums')->insert(
            ['stadium_id' => $ground, 'user_id' => $user]
        );

        DB::table('images')->insert(
          ['image_url' => $path, 'team_id' => $ground, 'review_id' => $id, 'user_id' => $user]
        );
        $response = array();

        $response["code"] = $image;
        
        echo json_encode ( $response );
    }

    public function favourite(Request $request) {
        $data = $request->all();

        $team = $data['favourite'];
        $user = $data['user'];

        $fav = DB::table('favourite_team')->where('uid', $user)->first();
        
        $response = array();
        if($fav != null){
            DB::table('favourite_team')
                ->where('uid', $user)
                ->update(['team_id' => $team]);
                $response["code"] = "updated";
        } else {
            DB::table('favourite_team')->insert(['uid' => $user, 'team_id' => $team]);
            $response["code"] = "set";

        }
        echo json_encode ( $response );
    }
    
}
