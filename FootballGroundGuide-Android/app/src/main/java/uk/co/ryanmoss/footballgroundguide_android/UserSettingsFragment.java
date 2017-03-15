package uk.co.ryanmoss.footballgroundguide_android;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class UserSettingsFragment extends Fragment {

    private String FIND_USER_URL = "http://178.62.121.73/users/";
    private String FOLLOWERS_URL = "http://178.62.121.73/users/followers/";
    private static final String TAG = "FriendFragment";
    private ArrayAdapter<String> followingAdapter;
    private  ArrayList<String> following = new ArrayList<>();
    private static final String PREFS_NAME = "UserDetails";


    private SearchView userSearch;
    private ListView mListView;


    private String uid;

    public UserSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_user_settings, container, false);

        return mView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userSearch = (SearchView) getView().findViewById(R.id.friendSearchBar);
        mListView = (ListView) getView().findViewById(R.id.friendsListView);

        followingAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                following);


        mListView.setAdapter(followingAdapter);

        getFollowers();

        userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                findUser(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedFromList =(String) (mListView.getItemAtPosition(position));
                Intent friendActivity = new Intent(view.getContext(), FriendProfileActivity.class);
                friendActivity.putExtra("user", selectedFromList);
                startActivity(friendActivity);
            }
        });


    }

    private void findUser(final String username) {

        String URL = FIND_USER_URL + username;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        UserDetailsClass user = new UserDetailsClass(response);
                        if(user.getUsername() != null) {

                            Intent friendActivity = new Intent(getContext(), FriendProfileActivity.class);
                            friendActivity.putExtra("user", user.getUsername());
                            startActivity(friendActivity);
                        } else {
                            Toasty.error(getActivity(), "No user found with the username " + username, Toast.LENGTH_LONG, true).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
    }

    private void getFollowers() {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        uid = settings.getString("uid", null);

        String URL = FOLLOWERS_URL + uid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        FollowingClass followingList = new FollowingClass(response);
                        ArrayList<String> followingTemp = followingList.getFollowing();
                        following.clear();
                        for(int i =0; i < followingTemp.size(); i++) {
                            following.add(followingTemp.get(i));
                        }
                        Log.d(TAG, following.toString());
                        followingAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        VolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
    }
}

