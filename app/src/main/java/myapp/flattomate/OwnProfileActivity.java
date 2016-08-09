package myapp.flattomate;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;
import myapp.flattomate.Model.User;
import myapp.flattomate.REST.FlattomateService;
import myapp.flattomate.REST.restAPI;
import myapp.myapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnProfileActivity extends AppCompatActivity {

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

    SharedPreferences manager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //obtain data for the user to see the profile
        //Bundle b = getIntent().getExtras();
        int idUser = manager.getInt("id", 0);
        Log.d("ID USER ", String.valueOf(idUser));
        if(idUser != 0) {
            FlattomateService api =
                    restAPI.createService(FlattomateService.class, "user", "secretpassword");
            Call<User> call = api.getUser(idUser);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200) {

                        if(response.isSuccessful()) {
                            user = response.body();
                            //user.setAges();
                            Log.d("AGES", String.valueOf(user.getAges()));
                            fillUserInfo();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getBaseContext(), "Error de conexión al servidor", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    void fillUserInfo(){
        String sName = user.getName();
        int iAges = user.getAges();
        String sLanguages = user.getLanguages();
        String sBio = user.getBio();
        String sAvatar = user.getAvatar();
        int iSociable = user.getSociable();
        int iTidy = user.getTidy();
        String sex = user.getSex();
        String activity = user.getActivity();
        int smoke = user.getSmoke();

        tvName.setText(sName);
        tvAge.setText(iAges+" años");
        tvLanguages.setText(sLanguages);
        tvBio.setText(sBio);
        tvSociable.setText(""+iSociable);
        tvTidy.setText(""+iTidy);

        //icons
        if(sex.equals("man")){
            ivSex.setImageResource(R.drawable.male_24);
            tvSex.setText("Hombre");
        }else {
            ivSex.setImageResource(R.drawable.female_24);
            tvSex.setText("Mujer");
        }

        if(activity.equals("work")){
            ivActivity.setImageResource(R.drawable.business_24);
            tvActivity.setText("Trabaja");
        }else{
            ivActivity.setImageResource(R.drawable.student_book_24);
            tvActivity.setText("Estudia");
        }

        if(smoke == 1){
            ivSmoke.setImageResource(R.drawable.smoking_24);
            int color = Color.parseColor("#FFFFFF");
            ivSmoke.setColorFilter(color);
            tvSmoke.setText("Fuma");
        }else{
            ivSmoke.setImageResource(R.drawable.no_smoking_24);
            tvSmoke.setText("No fuma");
        }



        //personalizing view of the user's profile
        if(sAvatar != null){

            Log.d("debug", "cargando imagen");

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(2)
                    .cornerRadiusDp(80)
                    .oval(false)
                    .build();

            Picasso.with(getApplicationContext())
                    .load("http://simpleicon.com/wp-content/uploads/user1.png")
                    .fit()
                    .transform(transformation)
                    .into(ivProfileImg);
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
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
