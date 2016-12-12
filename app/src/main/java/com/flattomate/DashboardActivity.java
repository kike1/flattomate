package com.flattomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.flattomate.Model.Announcement;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    @Bind(R.id.dashboard_list_view)
    ListView list_view;

    private Context context = this;
    //private SessionManager manager = new SessionManager(context);
    FlattomateService api;
    SharedPreferences manager;

    ArrayList<Announcement> announcements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);
        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        String email = manager.getString("email", null);

        //TESTING iteration 1, delete after testing
        if(email != null){
            //View parentLayout = findViewById(R.id.dashboard);
           // Snackbar.make(parentLayout, "Sesión iniciada como "+email, Snackbar.LENGTH_LONG).show();
            //Log.d("debug", email);

            //now we redirect to user profile following iteration 1

            /*Intent intent = new Intent(context, CreateAnnouncementActivity.class);
            intent.putExtra("idUser", 1);
            intent.putExtra("idAnnouncement", 3);
            startActivity(intent);*/

            //Get announcements and create the adapter list
            //TODO retrieve all locations and see which are near of you
            populateAnnouncementList();

        }else{ //no logged, redirect to home
            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void populateAnnouncementList() {
        final Call<ArrayList<Announcement>> request = api.getAnnouncements();
        request.enqueue(new Callback<ArrayList<Announcement>>() {
            @Override
            public void onResponse(final Call<ArrayList<Announcement>> call,
                                   final Response<ArrayList<Announcement>> response) {
                announcements = response.body();

                if(announcements != null) {
                    AnnouncementAdapter adapter = new AnnouncementAdapter(context,
                            R.layout.announcement_adapter ,announcements);
                    list_view.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(final Call<ArrayList<Announcement>> call, final Throwable t) { call.cancel(); }
        });
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
