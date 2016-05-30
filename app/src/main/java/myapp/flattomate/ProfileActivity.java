package myapp.flattomate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;
import myapp.myapp.R;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager manager = new SessionManager();
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.imgAvatar) ImageView ivProfilePhoto;
    //private Context context;

    TextView tvName;
    TextView tvLocation;
    TextView tvBio;
    ImageView ivAvatar;

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

        String sName = manager.getPreferences(getBaseContext(), "name");
        String sLocation = manager.getPreferences(getBaseContext(), "location");
        String sBio = manager.getPreferences(getBaseContext(), "bio");
        String sAvatar = manager.getPreferences(getBaseContext(), "avatar");

        tvName = (TextView)findViewById(R.id.txt_name);
        tvLocation = (TextView)findViewById(R.id.txt_location);
        tvBio = (TextView)findViewById(R.id.txt_bio);
        ivAvatar = (ImageView) findViewById(R.id.imgAvatar);

        //personalizing view of Profile
        if(sName != null || sName != ""){
            tvName.setText("¡Hola, soy "+sName+"!");
            tvLocation.setText(sLocation);
            tvBio.setText(sBio);

            Log.d("debug", "cargando imagen");

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(3)
                    .cornerRadiusDp(80)
                    .oval(false)
                    .build();

            Picasso.with(getApplicationContext())
                    .load("http://i.imgur.com/DvpvklR.png")
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
                Snackbar.make(coordinatorLayout, R.string.action_edit, Snackbar.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
