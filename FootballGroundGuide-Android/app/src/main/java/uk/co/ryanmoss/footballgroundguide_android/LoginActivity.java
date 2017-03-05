package uk.co.ryanmoss.footballgroundguide_android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private TextView registerLabel;
    private TextView forgotPasswordLabel;
    private Button loginButton;
    private EditText txtUsername;
    private EditText txtPassword;
    private RadioButton rdbRememberMe;


    final Context ctx = this;
    //private String LOGIN_URL = "http://46.101.2.231/FootballGroundGuide/login.php";
    private String LOGIN_URL = "http://178.62.121.73/users/login";
    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "UserDetails";

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String emailAddress;
    private String userRole;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerLabel = (TextView) findViewById(R.id.lbl_register);
        forgotPasswordLabel = (TextView) findViewById(R.id.lbl_forgot_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        txtUsername = (EditText) findViewById(R.id.txt_username);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        rdbRememberMe = (RadioButton) findViewById(R.id.rdb_remember_me);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        username = settings.getString("username", null);
        password = settings.getString("password", null);
        firstname = settings.getString("firstName", null);

        if (username != null || password != null) {
            Log.d(TAG, username.toString());
            Log.d(TAG, password.toString());
            Log.d(TAG, firstname.toString());
            checkLogin(username, password);
        }

        registerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        forgotPasswordLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(v.getContext(), ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();

                checkLogin(username, password);

            }
        });
    }


    private void checkLogin(final String username, final String password) {
        JSONObject js = new JSONObject();

        try {
            js.put("username", username);
            js.put("password", password);

            Log.e("js", js.toString());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, LOGIN_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                String strSuccess = response.getString("code");
                                String strMessage = response.getString("message");

                                Log.d(TAG, strMessage.toString());
                                if (strSuccess.equals("1")) {
                                    firstname = response.getString("first_name");
                                    lastname = response.getString("last_name");
                                    emailAddress = response.getString("email");

                                    uid = response.getString("uid");


                                    Log.d(TAG, firstname.toString());

                                    if (rdbRememberMe.isChecked()) {
                                        Log.d(TAG, "RADIO CHECKED");

                                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("username", username);
                                        editor.putString("password", password);
                                        editor.putString("firstName", firstname);
                                        editor.putString("lastName", lastname);
                                        editor.putString("email", emailAddress);

                                        editor.putString("uid", uid);

                                        editor.commit();

                                    }

                                    Toasty.success(ctx, "Successful Login!", Toast.LENGTH_SHORT, true).show();

                                    Intent homeIntent = new Intent(ctx, UserHomeActivity.class);
                                    startActivity(homeIntent);


                                } else if (strSuccess.equals("0")) {
                                    txtPassword.setError(strMessage);
                                } else if (strSuccess.equals("2")) {
                                    txtUsername.setError(strMessage);
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                }
            });

            VolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsonObjReq);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }

    }
}