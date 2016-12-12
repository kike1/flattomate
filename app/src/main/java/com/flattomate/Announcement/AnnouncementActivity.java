package com.flattomate.Announcement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.flattomate.CustomGrid;
import com.flattomate.ExpandableHeightGridView;
import com.flattomate.GoogleMaps.Map;
import com.flattomate.Model.Accommodation;
import com.flattomate.Model.Announcement;
import com.flattomate.Model.Image;
import com.flattomate.Model.Service;
import com.flattomate.Model.User;
import com.flattomate.Profile.ProfileActivity;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.GoogleMapsService;
import com.flattomate.REST.restAPI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.flattomate.R.drawable.calendar_all;
import static com.flattomate.R.drawable.calendar_days;
import static com.flattomate.R.id.map;
import static com.flattomate.REST.restAPI.API_BASE_URL;


public class AnnouncementActivity extends AppCompatActivity implements OnMapReadyCallback {

    //head
    @Bind(R.id.lbl_price)
    TextView lbl_price;
    @Bind(R.id.img_favorite)
    ImageView img_favorite;
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.txt_username)
    TextView txt_username;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.img_username)
    ImageView img_username;
    @Bind(R.id.lbl_reviews)
    TextView lbl_reviews;
    @Bind(R.id.btn_request)
    Button btn_request;

    @Bind(R.id.txt_description)
    TextView txt_description;

    //basic info
    @Bind(R.id.img_bed)
    ImageView img_bed;
    @Bind(R.id.txt_bed)
    TextView txt_bed;
    @Bind(R.id.img_shared_room)
    ImageView img_shared_room;
    @Bind(R.id.txt_shared_room)
    TextView txt_shared_room;
    @Bind(R.id.img_available)
    ImageView img_available;
    @Bind(R.id.txt_available)
    TextView txt_available;

    //address
    @Bind(R.id.lbl_address)
    TextView lbl_address;

    //stay
    @Bind(R.id.img_min_stay)
    ImageView img_min_stay;
    @Bind(R.id.txt_min_stay)
    TextView txt_min_stay;
    @Bind(R.id.lbl_min_stay)
    TextView lbl_min_stay;
    @Bind(R.id.img_max_stay)
    ImageView img_max_stay;
    @Bind(R.id.txt_max_stay)
    TextView txt_max_stay;
    @Bind(R.id.lbl_max_stay)
    TextView lbl_max_stay;

    //extra info
    @Bind(R.id.gridview_extra_info)
    ExpandableHeightGridView gridview_extra_info;


    SliderLayout sliderShow;
    DefaultSliderView sliderView;
    private GoogleMap mMap;

    String API_URL = API_BASE_URL;
    FlattomateService api;
    Announcement announcement;
    Accommodation accommodation;
    User user;
    int idUser;
    ArrayList<Service> services;
    ArrayList<Image> images;

    Transformation transformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_announcement);

        try{
            ButterKnife.bind(this);
        }catch (Exception e){
            Log.e("BUTTERKNIFE", e.getCause().toString());
        }


        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

        int idAnnouncement = getIntent().getExtras().getInt("idAnnouncement");

        idUser = getIntent().getExtras().getInt("idUser");

        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(60)
                .oval(false)
                .build();
        /*final Drawable upArrow = getResources().getDrawable(R.drawable.);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);*/

        if(idAnnouncement == 0){
            //TODO dialog error of activity
            Log.e("ERROR","NOT ANNOUNCEMENT");
        }else{

            //toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.announcement_toolbar);
            setSupportActionBar(myToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            /*
            AnnouncementController controller;
            try {
                controller = new AnnouncementController(idAnnouncement, announcement, user, services);
                controller.setData();
                announcement = controller.getAnnouncement();
                user = controller.getUser();
                services = controller.getServices();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

            //Getting Announcement
            Call<Announcement> callAnnouncement = api.getAnnouncement(idAnnouncement);
            callAnnouncement.enqueue(new Callback<Announcement>() {
                @Override
                public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                    if(response.code() == 200) {
                        announcement = response.body();
                        Log.e("ANNOUNCEMENT", announcement.getTitle());
                        if(announcement != null)
                            fillAnnouncementInfo();
                    }else
                        Log.e("ANNOUNCEMENT", "ERROR");
                }
                @Override
                public void onFailure(Call<Announcement> call, Throwable t) {
                    call.cancel();
                }
            });

            //Getting Accommodation
            Call<Accommodation> callAccommodation = api.getAnnouncementAccommodation(idAnnouncement);
            callAccommodation.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                    if(response.code() == 200) {
                        accommodation = response.body();
                        if(accommodation != null)
                            fillAccommodationInfo();
                    }
                }
                @Override
                public void onFailure(Call<Accommodation> call, Throwable t) {
                    call.cancel();
                }
            });

            sliderShow = (SliderLayout) findViewById(R.id.slider);
            sliderView = new DefaultSliderView(this);

            //Getting announcement's images
            Call<ArrayList<Image>> callImages = api.getAnnouncementImages(idAnnouncement);
            callImages.enqueue(new Callback<ArrayList<Image>>() {
                @Override
                public void onResponse(Call<ArrayList<Image>> call, Response<ArrayList<Image>> response) {
                    if(response.code() == 200) {
                        images = response.body();
                        if(images != null)
                            for (Image image: images) {
                                setImageOnSlideShow(image);
                            }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Image>> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getBaseContext(), "Error de conexión al servidor", Toast.LENGTH_LONG).show();
                }
            });

            sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderShow.setCustomAnimation(new DescriptionAnimation());
            sliderShow.setDuration(5000);

            //Getting announcement's services
            Call<ArrayList<Service>> callServices = api.getAnnouncementServices(idAnnouncement);
            callServices.enqueue(new Callback<ArrayList<Service>>() {
                @Override
                public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                    if(response.code() == 200) {
                        services = response.body();
                        if(services != null)
                            fillServicesInfo();
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                    call.cancel();
                }
            });

            //Getting Announcement's user creator
            Call<User> callUser = api.getAnnouncementUser(idAnnouncement);
            callUser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200) {
                        user = response.body();
                        if(user != null)
                            fillUserInfo();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                }
            });

            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(map);
            mapFragment.getMapAsync(this);

            img_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favorite();
                }
            });

            txt_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_username.callOnClick();
                }
            });
            img_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("idUser", user.getId());
                    startActivity(intent);
                }
            });

            //user grid a negotiation with ads user
            btn_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Call<ResponseBody> callRequest = api.requestNegotiation(idUser, user.getId(), announcement.getId());
                    callRequest.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200) {
                                btn_request.setBackground(getResources().getDrawable(R.drawable.roundedbuttonrequested));
                                btn_request.setText(R.string.requested);
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {call.cancel();}
                    });
                }
            });
        }
    }

    private void favorite() {

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("ONRESUME", "ONRESUME");

    }

    private void setImageOnSlideShow(Image image) {
        sliderView = new DefaultSliderView(this);
        sliderView.setScaleType(BaseSliderView.ScaleType.Fit)
                .image(API_URL+"announcements/"+image.getName());
        Log.d("im", API_URL+"announcements/"+image.getName());
        sliderShow.addSlider(sliderView);
    }

    void fillAnnouncementInfo(){

        //head
        lbl_price.setText(Html.fromHtml("<b>" + announcement.getPrice() +"€"+ "</b>") );
        img_favorite.setImageResource(R.drawable.heart_empty);
        txt_title.setText(Html.fromHtml("<b>" + announcement.getTitle()+ "</b>"));
        txt_description.setText(announcement.getDescription());

        //3 icons - bed, room, availability
        //room type
        img_shared_room.setImageResource(R.drawable.room);
        int id = 0;
        if(announcement.getIsSharedRoom()==1) { //shared room
            id = getResources().getIdentifier("shared_room", "string", getPackageName());
            if(id != 0)
                txt_shared_room.setText(getString(id)+"\n");
        }else{ //no shared room
            id = getResources().getIdentifier("no_shared_room", "string", getPackageName());
            if(id != 0)
                txt_shared_room.setText(getString(id)+"\n");
        }

        //availability
        img_available.setImageResource(calendar_days);

        // Format for output
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String availability_date_format = dateFormatter.format(announcement.getAvailability());
        txt_available.setText("Disponible: \n"+availability_date_format);

        //max and min stay
        img_min_stay.setImageResource(calendar_days);
        img_max_stay.setImageResource(calendar_all);
        txt_min_stay.setText(String.valueOf(announcement.getMinStay()));
        txt_max_stay.setText(String.valueOf(announcement.getMaxStay()));

    }

    void fillAccommodationInfo(){

        int idSingleBed = getResources().getIdentifier("single_bed", "string", getPackageName());
        int idDoubleBed = getResources().getIdentifier("double_bed", "string", getPackageName());

        //number of beds
        if (accommodation.getNBeds() == 1) {
            img_bed.setImageResource(R.drawable.one_bed);
            if(idSingleBed != 0){ //get the string
                txt_bed.setText(getString(idSingleBed)+"\n");
            }
        }else {
            img_bed.setImageResource(R.drawable.two_bed);
            if(idDoubleBed != 0){ //get the string
                txt_bed.setText(getString(idDoubleBed)+"\n");
            }
        }

        //address and map
        lbl_address.setText(accommodation.getLocation());
        String address = accommodation.getLocation();
        setMarkerOnMap(address);

    }

    void setMarkerOnMap(String address){
        String BASE_URL = "https://maps.googleapis.com";
        String API_KEY = "AIzaSyAupNlLb5UkpBIkGfwMXMlmMiSp1OiXGTI";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleMapsService service = retrofit.create(GoogleMapsService.class);
        Call<Map> callMap = service.getCityResults(address, API_KEY);

        callMap.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                Map map = response.body();

                Log.e("status", map.getStatus());
                if(map.getResults().size()<=0)
                    return;

                /*for (Result res: map.getResults()) {
                    Log.d("Result", String.valueOf(res.getGeometry().getLocation().getLat()));
                }*/

                // Add a marker in address and move the camera
                double lat = map.getResults().get(0).getGeometry().getLocation().getLat();
                double lng = map.getResults().get(0).getGeometry().getLocation().getLng();
                //Log.e("LAT/LONG", String.valueOf(lat)+","+String.valueOf(lng));
                LatLng acommodation_latlng = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                                .position(acommodation_latlng).
                                title(announcement.getTitle()).
                                flat(true));

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(acommodation_latlng));
                //mMap.moveCamera(CameraUpdateFactory.zoomTo(22));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(acommodation_latlng)// Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

               // mMap.animateCamera(CameraUpdateFactory.zoomTo(100), 2000, null);

            }
            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    void fillUserInfo(){
        txt_username.setText(user.getName());

        Picasso.with(getApplicationContext())
                .load(API_BASE_URL+"imgs/"+user.getId() + ".jpg")
                .fit()
                .centerCrop()
                .transform(transformation)
                .into(img_username);
    }

    void fillServicesInfo(){

        ArrayList<Integer> servicesIcons = new ArrayList<>();
        ArrayList<String> servicesDescription = new ArrayList<>();

        //get icons and description
        for (Service service: services) {
            Log.d("SERVICE", service.getName());
            servicesIcons.add(service.getId());
            servicesDescription.add(service.getName());
        }

        Log.d("SERVICES SIZE", String.valueOf(services.size()));
        CustomGrid adapter = new CustomGrid(AnnouncementActivity.this, servicesDescription, servicesIcons);
        gridview_extra_info.setExpanded(true);
        gridview_extra_info.setAdapter(adapter);

        /* gridview_extra_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    /* Callback Google Maps */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
