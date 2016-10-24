package com.flattomate.Announcement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.flattomate.CustomGrid;
import com.flattomate.ExpandableHeightGridView;
import com.flattomate.Model.Image;
import com.flattomate.Model.Service;

import java.util.ArrayList;

import butterknife.Bind;
import myapp.myapp.R;

import static myapp.myapp.R.id.gridview_extra_info;

public class CreateAnnouncementActivity extends AppCompatActivity {

    @Bind(R.id.gridview_imgs)
    ExpandableHeightGridView gridview_imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        setAnnouncementImages();
       // ImageAdapter adapter = new ImageAdapter(this);
        //gridview_extra_info.setExpanded(true);
        //gridview_extra_info.setAdapter(adapter);
    }

    private void setAnnouncementImages() {
        ArrayList<Image> annImages = getAnnouncementImages();

        for (Service service: services) {
            Log.d("SERVICE", service.getName());
            servicesIcons.add(service.getId());
            servicesDescription.add(service.getName());
        }

        Log.d("SERVICES SIZE", String.valueOf(services.size()));
        CustomGrid adapter = new CustomGrid(CreateAnnouncementActivity.this, servicesDescription, servicesIcons);
        gridview_extra_info.setExpanded(true);
        gridview_extra_info.setAdapter(adapter);
    }

    private void getAnnouncementImages() {
        ArrayList<Image> images = new ArrayList<>();

    }
}
