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

 
        $actualpath = public_path() . "/stadium_images/" . $name . ".png";
        
        
 
 
        $test = file_put_contents($actualpath,base64_decode($image));
        $response = array();

        $response["code"] = $image;
        
        echo json_encode ( $response );
    }
    
}
