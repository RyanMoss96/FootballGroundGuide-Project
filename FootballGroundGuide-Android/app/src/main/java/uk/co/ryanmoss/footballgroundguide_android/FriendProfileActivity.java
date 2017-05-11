package uk.co.ryanmoss.footballgroundguide_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class FriendProfileActivity extends AppCompatActivity {

    private Toolbar friendProfileToolbar;
    private String FRIEND_URL = "http://178.62.121.73/friends/add";
    private String user;
    private static final String PREFS_NAME = "UserDetails";
    final Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

         user = getIntent().getExtras().getString("user");

        friendProfileToolbar = (Toolbar) findViewById(R.id.friendAppBar);
        setSupportActionBar(friendProfileToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        friendProfileToolbar.setTitle(user);


        if (findViewById(R.id.fragment_container) != null) {


            if (savedInstanceState != null) {
                return;
            }

            UserProfileFragment firstFragment = new UserProfileFragment().newInstance(user);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.friend:
                addFriend();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void addFriend() {


        JSONObject js = new JSONObject();
        try{
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String uid = settings.getString("uid", null);

            js.put("user", user);
            js.put("uid", uid);


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,FRIEND_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toasty.success(ctx, "Friend Added", Toast.LENGTH_SHORT, true).show();


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            VolleyRequestQueue.getInstance(this).addToRequestQueue(jsonObjReq);
        } catch (JSONException e)
        {

        }
    }


}
