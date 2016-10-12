package com.flattomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import myapp.myapp.R;

public class HomeActivity extends AppCompatActivity {

    //public final Context context = this;
    //SessionManager manager = new SessionManager(context);
    SharedPreferences manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tx = (TextView)findViewById(R.id.nameApp);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lobster-Regular.ttf");
        tx.setTypeface(custom_font);
        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);

        //if user is logged, redirect to dashboard
        String email = manager.getString("email", null);
        if(email != null && !email.equals("")){

            Log.d("debug", email);
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loginView(View view) {

        Intent inent = new Intent(this, LoginActivity.class);
        startActivity(inent);
    }

    public void signupView(View view) {

        Intent inent = new Intent(this, RegisterActivity.class);
        startActivity(inent);
    }
}
