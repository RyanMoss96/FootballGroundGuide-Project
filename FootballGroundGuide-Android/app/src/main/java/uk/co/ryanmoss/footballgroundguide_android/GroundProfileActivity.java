package uk.co.ryanmoss.footballgroundguide_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class GroundProfileActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    //private String STADIUM_URL = "http://46.101.2.231/FootballGroundGuide/get_stadium_data.php";
    private String STADIUM_URL = "http://178.62.121.73/grounds/data/";
    private String FAVOURITE_URL = "http://178.62.121.73/grounds/favourite";
    private String VISITED_URL = "http://178.62.121.73/grounds/visited";
    private static final String TAG = "GroundProfile";
    final Context ctx = this;
    private FootballStadiumClass stadiumDetails;
    private static final String PREFS_NAME = "UserDetails";

    private TextView teamID;
    private TextView teamName;
    private TextView teamFounded;
    private TextView teamStadiumName;
    private TextView byTrain;
    private TextView byCar;
    private TextView capacity;
    private TextView details;
    private ImageView mImageView;
    private Button btnVisited;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

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
        byTrain = (TextView) findViewById(R.id.txtTrainInfo);
        byCar = (TextView) findViewById(R.id.txtCarInfo);
        capacity = (TextView) findViewById(R.id.txtCapacity);
        details = (TextView) findViewById(R.id.txtDetails);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnVisited = (Button) findViewById(R.id.VisitedButton);
        getStadiumImage("http://46.101.2.231/FootballGroundGuide/stadium_images/deepdale1.jpg");
        getStadiumData(strTeamName);

        progress.dismiss();

        btnVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groundVisited = new Intent(v.getContext(), GroundVisitedActivity.class);
                groundVisited.putExtra("ground_id", stadiumDetails.getTeamID());
                startActivity(groundVisited);
            }
        });


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
            case R.id.set_favourite:
                setFavTeamDialog();
                return true;
            case R.id.set_visited:
                visitedActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getStadiumData(String stadium) {

        Log.d(TAG, stadium);
            String URL = STADIUM_URL + stadium;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET,URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            stadiumDetails = new FootballStadiumClass(response);
                            setGroundProfileTextInfo();

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                }
            });
            VolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsonObjReq);
    }


    private void setGroundProfileTextInfo()
    {
        teamName.setText(stadiumDetails.getTeamName());
        teamFounded.setText(stadiumDetails.getTeamFounded());
        teamStadiumName.setText(stadiumDetails.getTeamStadiumName());
        byTrain.setText(stadiumDetails.getTrainInfo());
        byCar.setText(stadiumDetails.getCarInfo());
        capacity.setText(stadiumDetails.getCapacity());
        details.setText(stadiumDetails.getDetails());

        getStadiumImage(stadiumDetails.getImage());


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(stadiumDetails.getLatitude(), stadiumDetails.getLongitude()))
                .title(stadiumDetails.getTeamName()));

        //Build camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(stadiumDetails.getLatitude(), stadiumDetails.getLongitude()))
                .zoom(14).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

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
                setFavTeamConnection();



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

    private void visitedActivity() {
        Intent registerIntent = new Intent(ctx, GroundVisitedActivity.class);
        registerIntent.putExtra("id", stadiumDetails.getTeamID());
        startActivity(registerIntent);
    }

    private void setFavTeamConnection()
    {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String uid = settings.getString("uid", null);
        JSONObject js = new JSONObject();
        try{
            Log.d(TAG, uid);
            js.put("favourite", stadiumDetails.getTeamID());
            js.put("user", uid);
            Log.d(TAG, js.toString());

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
                    Log.d(TAG, "Error: " + error.getMessage());

                }
            });

            VolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsonObjReq);
        } catch (JSONException e)
        {

        }
    }

    private void markVisited() {

    }

}

