<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;

class CountryController extends Controller
{
    public function index()
    {

        $countries = DB::table('countries')->get();

        $response = array();

        foreach($countries as $country) {
            $response["country"][] = $country;
        }

        echo json_encode ( $response );
    }

    public function leagues()
{
return $this->hasMany('App\League');
}
}
