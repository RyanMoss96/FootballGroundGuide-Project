package uk.co.ryanmoss.footballgroundguide_android;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ryanmoss on 05/03/2017.
 */

public class FollowingClass {

    private String mUsername;
    private ArrayList<String> following;


    public FollowingClass(JSONObject response)
    {


            ArrayList<String> responseList = new ArrayList<>();
            try{
                JSONArray listArray = response.getJSONArray("follower");

                for(int i = 0; i <= listArray.length();i++)
                {
                    JSONObject row = listArray.getJSONObject(i);

                    String name = row.getString("username");

                    responseList.add(name);

                    following = responseList;

                }
            } catch (JSONException e)
            {

            }



    }

    public ArrayList<String> getFollowing()
    {
        return following;
    }


}
