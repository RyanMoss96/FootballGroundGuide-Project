<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;

class LeagueController extends Controller
{
    public function show($country)
    {
       $leagues = DB::table('leagues')
                    ->select('league_name')
                    ->join('countries', 'countries.country_id', '=', 'leagues.country_id')
                    ->where('countries.country_name', $country)
                    ->get();

        $response = array();

        foreach($leagues as $league) {
            $response["leagues"][] = $league;
        }

        return response()->json($response);

    }  
}
