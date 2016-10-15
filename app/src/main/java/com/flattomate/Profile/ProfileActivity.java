package com.flattomate.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flattomate.Model.User;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import myapp.myapp.R;
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

    SharedPreferences manager;
    User user;
    FlattomateService api;
    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idUser = getIntent().getExtras().getInt("idUser");

        if(idUser == 0){
            //TODO dialog error of activity
            Log.e("ERROR","NOT USER");
        }else{
            setContentView(R.layout.activity_own_profile);

            ButterKnife.bind(this);

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
                                Log.d("AGES", String.valueOf(user.getAges()));
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
        }

    }

    void fillUserInfo(){
        String undefined = String.valueOf(R.string.undefined);
        String sName = manager.getString("name", undefined);
        int iAges = manager.getInt("age", 18);
        Set<String> sLanguages = manager.getStringSet("languages", null);
        String sBio = manager.getString("bio", undefined);
        String sAvatar = user.getId()+".jpg";
        int iSociable = manager.getInt("sociable", 0);
        int iTidy = manager.getInt("tidy", 0);
        String sex = manager.getString("sex", undefined);
        String activity = manager.getString("activity", undefined);
        int smoke = manager.getInt("smoke", 0);

        tvName.setText(sName);
        tvAge.setText(iAges+" años");

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String ids = String.valueOf(idUser) + ":" + String.valueOf(manager.getInt("id",0));
        Log.d("IDS", ids);

        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
                finish();
                //refreshActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

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
