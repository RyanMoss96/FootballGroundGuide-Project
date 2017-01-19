package uk.co.ryanmoss.footballgroundguide_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroundProfileActivity extends AppCompatActivity {

    private String STADIUM_URL = "http://46.101.2.231/FootballGroundGuide/get_stadium_data.php";
    private String FAVOURITE_URL = "http://46.101.2.231/FootballGroundGuide/set_favourite_team.php";
    private static final String TAG = "GroundProfile";
    final Context ctx = this;
    private FootballStadiumClass stadiumDetails;

    private TextView teamID;
    private TextView teamName;
    private TextView teamFounded;
    private TextView teamStadiumName;
    private ImageView mImageView;

    private String strTeamName;
    private ProgressDialog progress;



    private Toolbar groundProfileToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_profile);

        progress = new ProgressDialog(ctx);

        progress.setMessage("Stadium Data Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.show();


        groundProfileToolbar = (Toolbar) findViewById(R.id.footballGroundAppBar);
        setSupportActionBar(groundProfileToolbar);

        strTeamName = getIntent().getExtras().getString("ground");

        groundProfileToolbar.setTitle(strTeamName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        teamName = (TextView) findViewById(R.id.txtTeamName);
        teamFounded = (TextView) findViewById(R.id.txtTeamFounded);
        teamStadiumName = (TextView) findViewById(R.id.txtTeamStadiumName);
        mImageView = (ImageView) findViewById(R.id.imageView);
        getStadiumImage("http://46.101.2.231/FootballGroundGuide/stadium_images/deepdale1.jpg");
        getStadiumData(strTeamName);

        progress.dismiss();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ground_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.set_favourite_team:
                setFavTeamDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getStadiumData(String stadium) {

        Log.d(TAG, stadium);

        try{
            JSONObject js = new JSONObject();
            js.put("stadium", stadium);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,STADIUM_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            stadiumDetails = new FootballStadiumClass(response);
                            setGroundProfileTextInfo();
                            getStadiumImage("http://46.101.2.231/FootballGroundGuide/stadium_images/deepdale.jpg");
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());


                }
            });

            VolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsonObjReq);
        } catch (JSONException e)
        {
            Log.d(TAG, e.toString());
        }

    }


    private void setGroundProfileTextInfo()
    {

        teamName.setText(stadiumDetails.getTeamName());
        teamFounded.setText(stadiumDetails.getTeamFounded());
        teamStadiumName.setText(stadiumDetails.getTeamStadiumName());
    }

    private void getStadiumImage(String image_url)
    {
        Picasso picasso = Picasso.with(ctx);
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);

        picasso.load(image_url)
                .placeholder(android.R.drawable.ic_dialog_email)
                .error(android.R.drawable.ic_media_play)
                .into(mImageView);
    }

    private void setFavTeamDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(R.string.dialog_set_fav_team_message)
                .setTitle(R.string.dialog_set_fav_team_title);

        builder.setPositiveButton(R.string.string_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //setFavTeamConnection();
                Intent registerIntent = new Intent(ctx, GroundVisitedActivity.class);
                startActivity(registerIntent);


            }
        });
        builder.setNegativeButton(R.string.string_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Nothing to do but close the dialog.
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void setFavTeamConnection()
    {
        JSONObject js = new JSONObject();
        try{
            js.put("favourite", strTeamName);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,FAVOURITE_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                }
            });

            VolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsonObjReq);
        } catch (JSONException e)
        {

        }


    }

}

