package com.flattomate.Profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.flattomate.R;
import com.flattomate.REST.restAPI;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShowImageProfile extends AppCompatActivity {

    @Bind(R.id.user_image_fullscreen)
    ImageView ivProfileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_image_profile);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.show_image_profile);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String image = extras.getString("image");
            String username = extras.getString("username");

            String tempPicture = extras.getString("temppicture");
            if(tempPicture != null && !tempPicture.contains("content")) {
                tempPicture = "file://" + extras.getString("temppicture");
                Log.d("SHOWIMAGE", tempPicture);
            }

            actionBar.setTitle(username);

            //if we have a temp picture then load it
            if(tempPicture != null){
                Log.d("PICTURE", tempPicture);
                ivProfileImg.setImageURI(Uri.parse(tempPicture));
//                Picasso.with(getApplicationContext()).
//                        load(tempPicture).
//                        into(ivProfileImg);
            }else{//if not, load from server
                Picasso.with(getApplicationContext()).
                        load(restAPI.API_BASE_URL+"imgs/"+image).
                        into(ivProfileImg);
            }

        }
    }

}
