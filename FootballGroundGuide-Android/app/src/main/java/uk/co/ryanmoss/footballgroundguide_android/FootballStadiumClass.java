package uk.co.ryanmoss.footballgroundguide_android;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryanmoss on 11/10/2016.
 */

public class FootballStadiumClass {

    private String mID;
    private String mName;
    private String mFounded;
    private String mStadiumName;
    private String mDefaultImage;

    public FootballStadiumClass(JSONObject response)
    {
        try{
            JSONArray listArray = response.getJSONArray("stadium");

            JSONObject row = listArray.getJSONObject(0);

            mID = row.getString("team_id");
            mName = row.getString("team_name");
            mFounded = row.getString("founded");
            mStadiumName= row.getString("stadium_name");

        } catch (JSONException e)
        {

        }
    }

    public String getTeamID()
    {
        return mID;
    }

    public String getTeamName()
    {
        return mName;
    }

    public String getTeamFounded()
    {
        return mFounded;
    }

    public String getTeamStadiumName()
    {
        return mStadiumName;
    }



}
