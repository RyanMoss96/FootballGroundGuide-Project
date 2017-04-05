<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| This file is where you may define all of the routes that are handled
| by your application. Just tell Laravel the URIs it should respond
| to using a Closure or controller method. Build something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});



Route::resource('countries', 'CountryController');
Route::resource('leagues', 'LeagueController');



Auth::routes();

Route::get('/home', 'HomeController@index');

Route::post('/users/register', 'UserController@register');
Route::post('/users/login', 'UserController@login');
Route::get('/users/{username}', 'UserController@show');
Route::get('/users/followers/{id}', 'UserController@followers');
Route::get('/users/favourite/{user}', 'UserController@favourite');
Route::get('/users/images/{user}', 'UserController@images');


Route::get('/grounds/{league}', 'GroundController@show');
Route::get('/grounds/data/{team}', 'GroundController@data');
Route::post('/grounds/visited/', 'GroundController@visited');
Route::post('/grounds/favourite/', 'GroundController@favourite');


Route::get('/friends/visited/{user}', 'FriendsController@visited');

