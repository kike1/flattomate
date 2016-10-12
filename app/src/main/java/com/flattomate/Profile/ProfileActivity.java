package com.flattomate.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.flattomate.Model.User;

import myapp.myapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    Context context = this;
    //private SessionManager manager = new SessionManager(context);

    SharedPreferences manager;
    User user;

    @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.imgAvatar) ImageView ivProfilePhoto;
    @Bind(R.id.txt_name) TextView tvName;
    @Bind(R.id.txt_bio) TextView tvBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);
        user = new User();

        //obtain data for the user to see the profile
        //Bundle b = getIntent().getExtras();
        int idUser = manager.getInt("id", 0);

        if(idUser != 0) {
            FlattomateService api =
                    restAPI.createService(FlattomateService.class, "user", "secretpassword");
            Call<User> call = api.getUser(idUser);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                   if(response.code() == 200) {

                        if(response.isSuccessful())
                            user = response.body();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getBaseContext(), "Error de conexión al servidor", Toast.LENGTH_LONG).show();
                }
            });

        }

        String sName = user.getName();
        String sBio = user.getBio();
        String sAvatar = user.getId()+".jpg";
        /*String sLocation = manager.getPreferences(getBaseContext(), "location");
        String sBio = manager.getPreferences(getBaseContext(), "bio");
        String sAvatar = manager.getPreferences(getBaseContext(), "avatar");*/


        //personalizing view of the user's profile
        if(sName != null){
            tvName.setText("¡Hola, soy "+sName+"!");
            tvBio.setText(sBio);

            Log.d("debug", "cargando imagen");

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(3)
                    .cornerRadiusDp(80)
                    .oval(false)
                    .build();

            Picasso.with(getApplicationContext())
                    .load(sAvatar)
                    .fit()
                    .transform(transformation)
                    .into(ivProfilePhoto);

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
                //Snackbar.make(coordinatorLayout, R.string.action_edit, Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
