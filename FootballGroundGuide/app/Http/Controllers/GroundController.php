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
    
}
