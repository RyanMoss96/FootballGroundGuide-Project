package uk.co.ryanmoss.footballgroundguide_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserProfileFragment extends Fragment {

    private static final String BUNDLE_VALUE_KEY = "user";
    private static final String TAG = "UserProfileFragment";
    private static final String PREFS_NAME = "UserDetails";

    private ImageView mProfilePic;
    private TextView mUsername;
    private TextView mName;
    private TextView mFavourite;

    private String USER_URL = "http://178.62.121.73/users/";
    private String FAVOURITE_URL = "http://178.62.121.73/users/favourite/";

    private UserDetailsClass userDetails;
    private ProgressDialog progress;
    private String user = null;
    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if(args != null){
           user = args.getString(BUNDLE_VALUE_KEY,null);

        } else {
            SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
            user = settings.getString("username", null);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mProfilePic = (ImageView) getView().findViewById(R.id.userProfileImage);
        mUsername = (TextView) getView().findViewById(R.id.txtUsername);
        mName = (TextView) getView().findViewById(R.id.txtName);
        mFavourite = (TextView) getView().findViewById(R.id.txtFavouriteTeam);

        if(user != null) {
            progress = new ProgressDialog(getActivity());

            progress.setMessage("Loading User Data");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();

            mUsername.setText(user);
            getUserData(user);



        }



        loadProfilePicture();

    }

    private void loadProfilePicture() {
        Picasso picasso = Picasso.with(getActivity());
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);

        picasso.load("http://46.101.2.231/FootballGroundGuide/stadium_images/deepdale.jpg")
                .placeholder(android.R.drawable.ic_dialog_email)
                .error(android.R.drawable.ic_media_play)
                .into(mProfilePic);
    }

    private void getUserData(String user) {
        String BASE_URL = USER_URL + user;
        Log.d(TAG, user);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,BASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        userDetails = new UserDetailsClass(response);
                        mName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
                        getFavouriteTeam();
                        Log.d(TAG, userDetails.getUID() + " " + userDetails.getLastName());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
    }

    private void getFavouriteTeam() {

        String BASE_URL = FAVOURITE_URL + userDetails.getUID();


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET,BASE_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                if(response.has("favourite")) {
                                    JSONArray listArray = response.getJSONArray("favourite");
                                    JSONObject row = listArray.getJSONObject(0);
                                    userDetails.setTeam(row.getString("team_name"));
                                    mFavourite.setText(userDetails.getTeam());
                                    Log.d(TAG, userDetails.getTeam());
                                }
                                progress.dismiss();
                            } catch(JSONException e) {
                                progress.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: " + error.getMessage());
                    progress.dismiss();
                }
            });

            VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
    }

    @NonNull
    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @NonNull
    public static UserProfileFragment newInstance(@NonNull String yourValue) {
        final UserProfileFragment instance = new UserProfileFragment();
        final Bundle args = new Bundle();
        args.putString(BUNDLE_VALUE_KEY,yourValue);
        instance.setArguments(args);
        return instance;
    }

}
