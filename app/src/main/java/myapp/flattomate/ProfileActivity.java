package myapp.flattomate;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import myapp.myapp.R;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager manager = new SessionManager();
    //private Context context;

    TextView tvName;
    TextView tvLocation;
    TextView tvBio;
    ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.toolbar), iconFont);

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
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }
}
