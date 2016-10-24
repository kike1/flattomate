package com.flattomate.Announcement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.flattomate.CustomGrid;
import com.flattomate.ExpandableHeightGridView;
import com.flattomate.ImageController;
import com.flattomate.Model.Accommodation;
import com.flattomate.Model.Announcement;
import com.flattomate.Model.Image;
import com.flattomate.Model.Service;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import myapp.myapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateAnnouncementActivity extends AppCompatActivity {

    //extra info
    @Bind(R.id.gridview_extra_info)
    ExpandableHeightGridView gridview_extra_info;

    @Bind(R.id.img_1)
    ImageView img_1;
    @Bind(R.id.img_2)
    ImageView img_2;
    @Bind(R.id.img_3)
    ImageView img_3;
    @Bind(R.id.img_4)
    ImageView img_4;

    int idUser;
    SharedPreferences manager;
    FlattomateService api;
    ArrayList<Image> images = new ArrayList<>();
    Accommodation accommodation;
    Announcement announcement;
    ImageController imageController;
    ArrayList<Service> services;

    final int REQUEST_CAMERA = 0;
    final int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);
        ButterKnife.bind(this);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);

        accommodation = new Accommodation();
        announcement = new Announcement();
        idUser = manager.getInt("idUser",0);

        //first init with one imageview and later we'll change
        imageController = new ImageController(this, idUser, img_1);

        setListenersOnImages();
        getServices();

        populateSpinner();


        if(images.size() <= 0){

        }
        //else
            //setAnnouncementImages();



        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i<gridview_extra_info.getChildCount(); i++){
            views.add(gridview_extra_info.getChildAt(i));
            views.get(i).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("VIEW", "Vista pulsada");
                        }
                    }
            );
        }




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                try {
                    imageController.onSelectFromGalleryResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA)
                imageController.onCaptureImageResult(data);
        }
    }

    private void setListenersOnImages() {
        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageController.selectImage();
            }
        });

        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageController.selectImage();
            }
        });

        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageController.selectImage();
            }
        });

        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageController.selectImage();
            }
        });

    }

    private void populateSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.rent_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rent_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Reapplying adapters
        /*Spinner spinner_min_stay = (Spinner) findViewById(R.id.spinner_min_stay);
        Spinner spinner_max_stay = (Spinner) findViewById(R.id.spinner_max_stay);
        spinner_min_stay.setAdapter(adapter);
        spinner_max_stay.setAdapter(adapter);*/


    }

    private void fillUI(){

        ArrayList<Integer> servicesIcons = new ArrayList<>();
        ArrayList<String> servicesDescription = new ArrayList<>();

        //get icons and description of services
        for (Service service: services) {
            Log.d("SERVICE", service.getName());
            servicesIcons.add(service.getId());
            servicesDescription.add(service.getName());
        }
        CustomGrid adapter = new CustomGrid(CreateAnnouncementActivity.this, servicesDescription, servicesIcons);
        gridview_extra_info.setExpanded(true);
        gridview_extra_info.setAdapter(adapter);

    }

    private void setAnnouncementImages() {
        ArrayList<Image> images = getAnnouncementImages();

        /*for (Service service: services) {
            Log.d("SERVICE", service.getName());
            servicesIcons.add(service.getId());
            servicesDescription.add(service.getName());
        }*/

       /* ImageAdapter adapter = new ImageAdapter(CreateAnnouncementActivity.this, images);
        gridview_imgs.setExpanded(true);
        gridview_imgs.setAdapter(adapter);*/
    }

    private ArrayList<Image> getAnnouncementImages() {

        return images;

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_single_bed:
                if (checked)
                    // Single bed
                    accommodation.setNBeds(1);
                    break;
            case R.id.radio_double_bed:
                if (checked)
                    // Double bed
                    accommodation.setNBeds(2);
                break;
        }
    }

    public void getServices() {
        //Getting Services
        Call<ArrayList<Service>> call = api.getServices();
        call.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if(response.code() == 200)
                    services = response.body();

                if(services != null)
                    fillUI();
            }
            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) { call.cancel(); }
        });
    }

    /*@OnClick(R.id.grid_image)
    @Nullable
    public void changeExtraColour(View view) {

        ImageView image = (ImageView) findViewById(R.id.grid_image);
        image.setColorFilter(ContextCompat.getColor(this,R.color.accent));

    }*/

    //establishes the font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
