package uk.co.ryanmoss.footballgroundguide_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;



public class UserHomeActivity extends AppCompatActivity implements FragmentController {

    private Toolbar userHomeToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String TAG = "UserHomeActivity";
    private static final String PREFS_NAME = "UserDetails";
    private String SAVED_LEAGUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        userHomeToolbar = (Toolbar) findViewById(R.id.userHomeTabView);
        setSupportActionBar(userHomeToolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", null);
        editor.putString("password", null);
        editor.putString("firstName", null);
        editor.putString("lastName", null);
        editor.putString("email", null);

        editor.putString("uid", null);

        editor.commit();
        Intent homeIntent = new Intent(this, LoginActivity.class);
        startActivity(homeIntent);
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("league", SAVED_LEAGUE);


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }




    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserProfileFragment().newInstance(), "Home");
        adapter.addFragment(new RootFragment(), "Grounds");
        adapter.addFragment(new UserSettingsFragment(), "Friends");

        viewPager.setAdapter(adapter);
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void league(String country) {
        Log.d(TAG, country);
        LeagueListFragment leagueFrag = (LeagueListFragment)
                getSupportFragmentManager().findFragmentById(R.id.leagueFrag);



        LeagueListFragment newFragment = new LeagueListFragment();
        Bundle args = new Bundle();
        args.putSerializable("country", country);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations( R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        transaction.replace(R.id.root_frame, newFragment);
        transaction.addToBackStack(null);


        transaction.commit();

    }


    @Override
    public void ground(String league) {
        GroundListFragment groundFrag = (GroundListFragment)
                getSupportFragmentManager().findFragmentById(R.id.groundFrag);
        SAVED_LEAGUE = league;

        GroundListFragment newFragment = new GroundListFragment();
        Bundle args = new Bundle();
        args.putSerializable("league", league);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations( R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        transaction.replace(R.id.root_frame, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
