package uk.co.ryanmoss.footballgroundguide_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Rating;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.R.attr.bitmap;

public class GroundVisitedActivity extends AppCompatActivity {


    private ImageButton btnCamera;
    private EditText txtDesc;
    private Button btnUpload;
    private RatingBar ratings;

    private Bitmap bitmap;
    private Bitmap resized;

    private Toolbar groundVisitedToolbar;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = "GroundVisitedActivity";
    private String UPLOAD_URL ="http://178.62.121.73/grounds/visited";
    final Context ctx = this;
    private static final String PREFS_NAME = "UserDetails";

    private String groundID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_visited);

        groundVisitedToolbar = (Toolbar) findViewById(R.id.footballVisitedAppBar);
        setSupportActionBar(groundVisitedToolbar);

        btnCamera = (ImageButton) findViewById(R.id.btn_image);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        txtDesc = (EditText) findViewById(R.id.txt_description);
        ratings = (RatingBar) findViewById(R.id.ratingBar);

        ratings.setMax(5);

        groundID = getIntent().getExtras().getString("id");

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dispatchTakePictureIntent();

                showFileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dispatchTakePictureIntent();

                setVisited();
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.4), (int)(bitmap.getHeight()*0.4), true);
                //Setting the Bitmap to ImageView
                btnCamera.setImageBitmap(resized);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void upload(){

        JSONObject js = new JSONObject();
        String name = txtDesc.getText().toString();
        float rating = ratings.getRating();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String uid = settings.getString("uid", null);



        Toast.makeText(ctx, name, Toast.LENGTH_LONG).show();
        try{
            js.put("image", getStringImage(resized));
            js.put("name", name );
            js.put("rating", rating);
            js.put("ground", groundID);
            js.put("user", uid);

            Log.e("js",  getStringImage(resized));
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST,UPLOAD_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try{
                                String  strSuccess = response.getString("code");
                                Log.d(TAG, strSuccess);
                            } catch (JSONException e)
                            {
                                Log.d(TAG, e.toString());
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                    Toast.makeText(ctx, "Failed", Toast.LENGTH_LONG).show();

                }
            });

            VolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsonObjReq);
        } catch (JSONException e)
        {
            Log.e(TAG, e.toString());
        }
    }


    private void setVisited() {

        upload();
    }
}
