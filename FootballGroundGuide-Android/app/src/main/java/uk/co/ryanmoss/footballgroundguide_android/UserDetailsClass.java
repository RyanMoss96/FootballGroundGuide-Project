package uk.co.ryanmoss.footballgroundguide_android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryanmoss on 05/03/2017.
 */

public class UserDetailsClass {

    private String mUsername = null;
    private String mUID;
    private String mFirstname;
    private String mLastname;
    private String mTeam;

    public UserDetailsClass(JSONObject response) {
        try{

            mUID = response.getString("uid");
            mUsername = response.getString("username");
            mFirstname = response.getString("firstname");
            mLastname = response.getString("lastname");




        } catch (JSONException e)
        {

        }
    }

    public String getUID() { return mUID; }
    public String getUsername()
    {
        return mUsername;
    }
    public String getFirstName() { return mFirstname; }
    public String getLastName() { return mLastname; }
    public String getTeam() { return mTeam; }

    public void setTeam(String team) {
        mTeam = team;
    }
}
