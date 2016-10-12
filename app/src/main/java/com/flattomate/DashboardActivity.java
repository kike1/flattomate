package com.flattomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import myapp.myapp.R;

public class DashboardActivity extends AppCompatActivity {

    private Context context = this;
    //private SessionManager manager = new SessionManager(context);

    SharedPreferences manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);

        String email = manager.getString("email", null);

        //TESTING iteration 1, delete after testing
        if(email != null){
            View parentLayout = findViewById(R.id.dashboard);
            Snackbar.make(parentLayout, "Sesión iniciada como "+email, Snackbar.LENGTH_LONG).show();
            //Log.d("debug", email);

            //now we redirect to user profile following iteration 1

            Intent intent = new Intent(context, AnnouncementActivity.class);
            intent.putExtra("idAnnouncement", 3);
            startActivity(intent);

        }else{
            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //Snackbar.make(, R.string.action_edit, Snackbar.LENGTH_LONG).show();
                Toast.makeText(this, "prueba", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
