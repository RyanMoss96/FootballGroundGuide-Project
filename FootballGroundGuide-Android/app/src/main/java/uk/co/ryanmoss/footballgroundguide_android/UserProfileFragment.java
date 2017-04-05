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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class UserProfileFragment extends Fragment {

    private static final String BUNDLE_VALUE_KEY = "user";
    private static final String TAG = "UserProfileFragment";
    private static final String PREFS_NAME = "UserDetails";

    private ImageView mProfilePic;
    private TextView mUsername;
    private TextView mName;
    private TextView mFavourite;
    private TextView mVisited;
    private GridView gridview;

    private String USER_URL = "http://178.62.121.73/users/";
    private String FAVOURITE_URL = "http://178.62.121.73/users/favourite/";
    private String IMAGES_URL = "http://178.62.121.73/users/images/";
    private String VISITED_URL = "http://178.62.121.73/friends/visited/";

    private UserDetailsClass userDetails;
    private ProgressDialog progress;
    private String user = null;
    private String[] images;
    private ArrayList<String> responseList = new ArrayList<>();;

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
        mVisited = (TextView) getView().findViewById(R.id.txtGroundsVisited);
        gridview = (GridView) getView().findViewById(R.id.gridview);



        if(user != null) {
            progress = new ProgressDialog(getActivity());

            progress.setMessage("Loading User Data");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();

            mUsername.setText(user);

            getUserData(user);


            if(responseList.size() != 0)
            {
                gridview.setAdapter(new ImageAdapter(getActivity(), responseList));
            }


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
                        getVisited();
                        getImages();

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
                    mFavourite.setVisibility(View.INVISIBLE);
                }
            });

            VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
    }


    private void getVisited() {

        String BASE_URL = VISITED_URL + userDetails.getUID();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        mVisited.setText("Stadiums Visited: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void getImages(){
        String BASE_URL = IMAGES_URL + userDetails.getUID();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,BASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{


                            JSONArray listArray = response.getJSONArray("images");
                            images = new String[listArray.length()];
                            for(int i = 0; i <= listArray.length();i++)
                            {
                                JSONObject row = listArray.getJSONObject(i);

                                String name = row.getString("image_url");

                                responseList.add(name);

                                Log.d(TAG, responseList.toString());

                            }
                            gridview.setAdapter(new ImageAdapter(getActivity(), responseList));


                        } catch (JSONException e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());


            }
        });

        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
    }

    private void parseImages(JSONObject response) {



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

