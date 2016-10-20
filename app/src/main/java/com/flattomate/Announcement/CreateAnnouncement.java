package com.flattomate.Announcement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.flattomate.ExpandableHeightGridView;

import butterknife.Bind;
import myapp.myapp.R;

public class CreateAnnouncement extends AppCompatActivity {

    @Bind(R.id.gridview_imgs)
    ExpandableHeightGridView gridview_imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

       // ImageAdapter adapter = new ImageAdapter(this);
        //gridview_extra_info.setExpanded(true);
        //gridview_extra_info.setAdapter(adapter);
    }
}
