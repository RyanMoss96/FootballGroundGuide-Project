package uk.co.ryanmoss.footballgroundguide_android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public class RegisterActivity extends AppCompatActivity {

    TextView loginLabel;
    EditText txtUsername;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtPassword;
    EditText txtEmail;
    EditText txtConfirmPass;
    Button btnRegister;
    final Context ctx = this;
    //private String REGISTER_URL = "http://46.101.2.231/FootballGroundGuide/register_account.php";
    private String REGISTER_URL = "http://178.62.121.73/users/register";
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btn_register);
        loginLabel = (TextView) findViewById(R.id.lbl_login);
        txtUsername = (EditText) findViewById(R.id.txt_username);
        txtFirstName = (EditText) findViewById(R.id.txt_first_name);
        txtLastName = (EditText) findViewById(R.id.txt_last_name);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtConfirmPass = (EditText) findViewById(R.id.txt_confirm_password);


        
        loginLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginScreen();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPass.getText().toString();

                if(checkInternetConnection())
                {
                    if(!username.isEmpty()) {
                        if(!firstName.isEmpty()) {
                            if(!lastName.isEmpty()) {
                                if(!email.isEmpty()) {
                                    if(!password.isEmpty()) {
                                        if(!confirmPassword.isEmpty()) {
                                            if(password.length() >= 7)
                                            {
                                                if(password.equals(confirmPassword))
                                                {

                                                    JSONObject js = new JSONObject();

                                                    try{
                                                        js.put("username", username);
                                                        js.put("first_name", firstName);
                                                        js.put("last_name", lastName);
                                                        js.put("email", email);
                                                        js.put("password", password);
                                                        Log.e("js", js.toString());
                                                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                                                Request.Method.POST,REGISTER_URL, js,
                                                                new Response.Listener<JSONObject>() {
                                                                    @Override
                                                                    public void onResponse(JSONObject response) {
                                                                        Log.d(TAG, response.toString());

                                                                        //Toast toast = Toast.makeText(ctx, "Account created, you may now login", Toast.LENGTH_SHORT);
                                                                        //toast.show();

                                                                        Toasty.success(ctx, "Account Created!", Toast.LENGTH_SHORT, true).show();

                                                                        showLoginScreen();

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
                                                        Log.e(TAG, e.toString());
                                                    }
                                                }else {

                                                    txtConfirmPass.setError("Passwords do not match");
                                                    txtPassword.setError("Passwords do not match");

                                                }
                                            } else {
                                                txtPassword.setError("Password must be at least 7 characters long");
                                            }
                                        } else{
                                            txtConfirmPass.setError("Please re-enter your password.");
                                        }
                                    } else{
                                        txtPassword.setError("Please enter a password");
                                    }
                                } else{
                                    txtEmail.setError("Please enter an email address");
                                }
                            } else{
                                txtLastName.setError("Please enter a Last Name");
                            }
                        } else{
                            txtFirstName.setError("Please enter a First Name");
                        }
                    } else{
                        txtUsername.setError("Please enter a Username");
                    }
                } else {

                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

// 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage(R.string.dialog_internet_message)
                            .setTitle(R.string.dialog_internet_title);

// 3. Get the AlertDialog from create()
                    AlertDialog connectionDialog = builder.create();

                    connectionDialog.show();
                }

            }
        });
    }

    private boolean checkInternetConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }



    private void showLoginScreen()
    {
        Intent intent = new Intent(ctx, LoginActivity.class);
        startActivity(intent);
    }
}


