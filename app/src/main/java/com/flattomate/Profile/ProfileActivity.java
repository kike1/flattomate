package com.flattomate.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flattomate.HomeActivity;
import com.flattomate.Model.Language;
import com.flattomate.Model.User;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.img_ownprofile)
    ImageView ivProfileImg;
    @Bind(R.id.txt_name)
    TextView tvName;
    @Bind(R.id.txt_age)
    TextView tvAge;

    @Bind(R.id.txt_languages)
    TextView tvLanguages;

    @Bind(R.id.txt_bio)
    TextView tvBio;

    @Bind(R.id.img_sex)
    ImageView ivSex;
    @Bind(R.id.txt_sex)
    TextView tvSex;
    @Bind(R.id.img_activity)
    ImageView ivActivity;
    @Bind(R.id.txt_activity)
    TextView tvActivity;
    @Bind(R.id.img_smoke)
    ImageView ivSmoke;
    @Bind(R.id.txt_smoke)
    TextView tvSmoke;

    @Bind(R.id.txt_sociable)
    TextView tvSociable;
    @Bind(R.id.txt_tidy)
    TextView tvTidy;

    @Bind(R.id.progressbarSociable)
    ProgressBar progressBarSociable;
    @Bind(R.id.progressbarTidy)
    ProgressBar progressBarTidy;

    @Bind(R.id.btn_logout)
    Button btn_logout;

    SharedPreferences manager;
    User user;
    FlattomateService api;
    int idUser;
    static final String ID_USER = "idUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            idUser = savedInstanceState.getInt(ID_USER);
        } else {
            idUser = getIntent().getExtras().getInt("idUser");
        }

        if(idUser == 0){
            //TODO dialog error of activity
            Log.e("ERROR","NOT USER");
        }else{
            setContentView(R.layout.activity_own_profile);

            ButterKnife.bind(this);

            //hide the button until user is the same than profile
            btn_logout.setVisibility(View.GONE);

            Toolbar myToolbar = (Toolbar) findViewById(R.id.ownprofile_toolbar);
            setSupportActionBar(myToolbar);
            // Get a support ActionBar corresponding to this toolbar
            ActionBar ab = getSupportActionBar();
            ab.setTitle("Perfil");
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);

            manager = getSharedPreferences("settings", Context.MODE_PRIVATE);
            api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

            //obtain data for the user to see the profile
            //idUser = manager.getInt("id", 0);
            //fillUserInfo();
            Call<User> call = api.getUser(idUser);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200) {

                        if(response.isSuccessful()) {
                            user = response.body();
                            //user.setAges();
                            if(user != null) {
                                btn_logout.setVisibility(View.VISIBLE);
                                fillUserInfo();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getBaseContext(), "Error de conexión al servidor", Toast.LENGTH_LONG).show();
                }
            });

            //Shows profile img in fullscreen
            ivProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ShowImageProfile.class);
                    intent.putExtra("image", user.getId()+".jpg");
                    intent.putExtra("username", user.getName());
                    startActivity(intent);
                }
            });

            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //clear sharedpreferences
                    SharedPreferences.Editor editor = manager.edit();
                    editor.clear();
                    editor.commit();
                    //redirect to HomeActivity
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    void fillUserInfo(){

        String undefined = String.valueOf(R.string.undefined);
        String sName = user.getName();
        int iAges = user.getAges();
        ArrayList<Language> sLanguages = user.getLanguages();
        String sBio = user.getBio();
        String sAvatar = user.getId()+".jpg";
        int iSociable = user.getSociable();
        int iTidy = user.getTidy();
        String sex = user.getSex();
        String activity = user.getActivity();
        int smoke = user.getSmoke();

        tvName.setText(sName);
        tvAge.setText(String.valueOf(iAges)+" años");

        if(sLanguages != null){
            String languagesFormatted = sLanguages.toString();
            languagesFormatted = formatLanguage(languagesFormatted);

            tvLanguages.setText(languagesFormatted);
        }

        tvBio.setText(sBio);
        tvSociable.setText(iSociable+"/10");
        tvTidy.setText(iTidy+"/10");

        //icons
        if(sex.equals("man")){
            ivSex.setImageResource(R.drawable.male);
            tvSex.setText("Hombre");
        }else {
            ivSex.setImageResource(R.drawable.female);
            tvSex.setText("Mujer");
        }
        int color = Color.parseColor("#0288D1");
        ivSex.setColorFilter(color);

        if(activity.equals("work")){
            ivActivity.setImageResource(R.drawable.business);
            tvActivity.setText("Trabaja");
        }else{
            ivActivity.setImageResource(R.drawable.student);
            tvActivity.setText("Estudia");
        }
        ivActivity.setColorFilter(color);

        if(smoke == 1){
            ivSmoke.setImageResource(R.drawable.smoking);
            tvSmoke.setText("Fuma");
        }else{
            ivSmoke.setImageResource(R.drawable.no_smoking);
            tvSmoke.setText("No fuma");
        }
        ivSmoke.setColorFilter(color);

        //setting progress of bars
        progressBarSociable.setMax(10);
        progressBarSociable.setProgress(iSociable);
        progressBarSociable.setBackgroundColor(Color.parseColor("#000000"));
        progressBarTidy.setMax(10);
        progressBarTidy.setProgress(iTidy);

        Log.d("avatar", restAPI.API_BASE_URL+"imgs/"+sAvatar);
        //personalizing view of the user's profile
        if(sAvatar != null){
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(1)
                    .cornerRadiusDp(60)
                    .oval(false)
                    .build();

            Picasso.with(getApplicationContext())
                    .load(restAPI.API_BASE_URL+"imgs/"+sAvatar)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .fit()
                    .centerCrop()
                    .transform(transformation)
                    .into(ivProfileImg);
        }
    }

    private String formatLanguage(String languagesFormatted) {
        languagesFormatted = languagesFormatted.replace("[","");
        languagesFormatted = languagesFormatted.replace("]","");
        languagesFormatted = languagesFormatted.replace(", "," - ");
        languagesFormatted = languagesFormatted.replace(", "," - ");

        return languagesFormatted;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String ids = String.valueOf(idUser) + ":" + String.valueOf(manager.getInt("id",0));
        Log.d("IDS", ids);

        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                //startActivityForResult(intent);
                startActivity(intent);
                //finish();
                //refreshActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
        savedInstanceState.putInt(ID_USER, idUser);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*MenuItem actionEditItem = menu.findItem(R.id.action_edit);
        MenuItem unfave = menu.findItem(R.id.unfavorite);

        fave.setVisible(!isFave);
        unfave.setVisible(isFave);
        return true;*/

        MenuItem actionEditItem = menu.findItem(R.id.action_edit);

        //if user is logged user can edit profile
        if(idUser == manager.getInt("id",0))
            actionEditItem.setVisible(true);
        else
            actionEditItem.setVisible(false);

        return true;
    }

    //establish the font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
