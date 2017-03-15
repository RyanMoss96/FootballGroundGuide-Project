package uk.co.ryanmoss.footballgroundguide_android;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FriendProfileActivity extends AppCompatActivity {

    private Toolbar friendProfileToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        String user = getIntent().getExtras().getString("user");

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
