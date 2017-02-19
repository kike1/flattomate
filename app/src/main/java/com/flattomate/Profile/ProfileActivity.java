package com.flattomate.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.flattomate.Constants;
import com.flattomate.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    int idUser, idAnnouncement;
    private static final String ID_USER = "id";
    SharedPreferences manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setToolbar();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            idUser = savedInstanceState.getInt(Constants.ID_USER);
            idAnnouncement = savedInstanceState.getInt(Constants.ID_ANNOUNCEMENT);
        } else {
            idUser = getIntent().getExtras().getInt(Constants.ID_USER);
            idAnnouncement = getIntent().getExtras().getInt(Constants.ID_ANNOUNCEMENT);
        }

        if(idUser != 0){
            ProfileFragment fragment = new ProfileFragment();
            Bundle args = new Bundle();
            args.putInt(Constants.ID_USER, idUser);
            args.putInt(Constants.ID_ANNOUNCEMENT, idAnnouncement);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            manager = getSharedPreferences("settings", Context.MODE_PRIVATE);
        }

    }

    /**
     * Establece la toolbar como action bar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        idUser = savedInstanceState.getInt(ID_USER);
    }*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(Constants.ID_USER, idUser);
        savedInstanceState.putInt(Constants.ID_ANNOUNCEMENT, idAnnouncement);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    //establish the font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
