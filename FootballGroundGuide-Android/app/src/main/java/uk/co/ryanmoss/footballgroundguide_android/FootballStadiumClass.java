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
    private String mTrain;
    private String mCar;
    private String mCapacity;
    private String mDetails;
    private String mDefaultImage;
    private double mLatitude;
    private double mLongitude;

    public FootballStadiumClass(JSONObject response)
    {
        try{
            JSONArray listArray = response.getJSONArray("stadium");

            JSONObject row = listArray.getJSONObject(0);

            mID = row.getString("team_id");
            mName = row.getString("team_name");
            mFounded = row.getString("founded");
            mStadiumName= row.getString("stadium_name");
            mTrain = row.getString("by_train");
            mCar = row.getString("by_car");
            mDetails = row.getString("details");
            mCapacity = row.getString("capacity");
            mDefaultImage = row.getString("image");
            mLatitude = row.getDouble("latitude");
            mLongitude = row.getDouble("longitude");


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

    public String getTrainInfo() { return mTrain;}

    public String getCarInfo() { return mCar;}

    public String getCapacity() { return mCapacity; }

    public String getDetails() { return mDetails; }

    public String getImage() { return mDefaultImage; }

    public Double getLongitude() { return mLongitude; }

    public Double getLatitude() { return mLatitude; }
}
