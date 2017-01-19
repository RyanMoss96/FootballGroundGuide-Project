<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;

class LeagueController extends Controller
{
    //REST GET, /leagues/
    public function index()
    {
        
    }

    //REST GET, /leagues/{country}, returns the leagues for that specified {country}
    public function show($country)
    {
        //Command for getting all the leagues corresponding to the passed in Country.
        $leagues = DB::table('leagues')
                    ->select('league_name')
                    ->join('countries', 'countries.country_id', '=', 'leagues.country_id')
                    ->where('countries.country_name', $country)
                    ->get();
        
        //Creates an array for storing the results.
        $response = array();

        //Storing each returned league in the array.
        foreach($leagues as $league) {
            $response["leagues"][] = $league;
        }

        //Return the array and encode it as JSON. API returns JSON, which my Android application expects to handle.
        return response()->json($response);

    }  
}
