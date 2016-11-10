package com.flattomate.Announcement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flattomate.CustomGrid;
import com.flattomate.DateValidator;
import com.flattomate.ExpandableHeightGridView;
import com.flattomate.ImageController;
import com.flattomate.Model.Accommodation;
import com.flattomate.Model.Announcement;
import com.flattomate.Model.Image;
import com.flattomate.Model.Service;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.flattomate.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

public class CreateAnnouncementActivity extends AppCompatActivity {

    //images
    @Bind(R.id.img_1)
    ImageView img_1;
    @Bind(R.id.img_2)
    ImageView img_2;
    @Bind(R.id.img_3)
    ImageView img_3;
    @Bind(R.id.img_4)
    ImageView img_4;

    //info
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.et_description)
    EditText etDescription;
    @Bind(R.id.et_availability)
    TextView etAvailability;

    @Bind(R.id.rent_spinner)
    Spinner spinner;
    @Bind(R.id.et_min_stay)
    EditText etMinStay;
    @Bind(R.id.et_max_stay)
    EditText etMaxStay;

    //extra info
    @Bind(R.id.gridview_extra_info)
    ExpandableHeightGridView gridview_extra_info;

    Calendar myCalendar = Calendar.getInstance();

    int idUser;
    SharedPreferences manager;
    FlattomateService api;
    ArrayList<Uri> images = new ArrayList<>();
    Accommodation accommodation;
    Announcement announcement;
    ImageController imageController;
    ArrayList<Service> services;

    final int REQUEST_CAMERA = 0;
    final int SELECT_FILE = 1;
    final int REQUEST_TAKE_PHOTO = 2;
    String userChoosenTask;
    Context context = this;
    Intent intent = new Intent(ACTION_IMAGE_CAPTURE);


    boolean[] servicesPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.create_announcement_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getResources().getString(R.string.create_announcement_title));
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setShowHideAnimationEnabled(true);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);

        accommodation = new Accommodation();
        announcement = new Announcement();
        idUser = manager.getInt("idUser",0);

        /*View.OnClickListener listnr=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent("AnotherActivity");
                startActivity(i);
            }
        };*/

        //first init with one imageview and later we'll change
        imageController = new ImageController(this, this, idUser, img_1);

        setListenersOnImages();
        getServices();
        populateSpinner();
        initCalendar();

        etAvailability.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasfocus) {
                if(hasfocus) {
                    DatePickerDialog dialog = new DatePickerDialog(CreateAnnouncementActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis()); //TODO set maximum date a bunch of months, not today
                    dialog.show();
                }
            }
        });
    }

    void initCalendar(){
        Date date; // your date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etAvailability.setText(sdf.format(myCalendar.getTime()));
    }

    private void setListenersOnImages() {

        Picasso.with(getApplicationContext())
                .load(getApplicationContext()
                        .getResources()
                        .getIdentifier("default_image","drawable", getPackageName()))
                .into(img_1);

        Picasso.with(getApplicationContext())
                .load(getApplicationContext()
                        .getResources()
                        .getIdentifier("default_image","drawable", getPackageName()))
                .into(img_2);

        Picasso.with(getApplicationContext())
                .load(getApplicationContext()
                        .getResources()
                        .getIdentifier("default_image","drawable", getPackageName()))
                .into(img_3);

        Picasso.with(getApplicationContext())
                .load(getApplicationContext()
                        .getResources()
                        .getIdentifier("default_image","drawable", getPackageName()))
                .into(img_4);

        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageController.setImageView(img_1);
                selectImage();
            }
        });

        img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageController.setImageView(img_2);
                selectImage();
            }
        });

        img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageController.setImageView(img_3);
                selectImage();
            }
        });

        img_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageController.setImageView(img_4);
                selectImage();
            }
        });

    }

    private void populateSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rent_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    private void fillUI(){

        ArrayList<Integer> servicesIcons = new ArrayList<>();
        ArrayList<String> servicesDescription = new ArrayList<>();

        //get icons and description of services
        for (Service service: services) {
            servicesIcons.add(service.getId());
            servicesDescription.add(service.getName());
        }
        CustomGrid adapter = new CustomGrid(CreateAnnouncementActivity.this, servicesDescription, servicesIcons);
        gridview_extra_info.setExpanded(true);
        gridview_extra_info.setAdapter(adapter);

        //for now just 7 services, we need to change that if new services are included
        servicesPressed = new boolean[7];

        gridview_extra_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v,
                                    int position, long id) {
                Log.e("index", String.valueOf(position));
                //Service ON/OFF
                if(servicesPressed[position]) {
                    servicesPressed[position] =  false;
                    v.setBackgroundColor(Color.WHITE);
                }else {
                    servicesPressed[position] =  true;
                    v.setBackgroundColor(Color.GRAY);
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            //bed
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

            //room
            case R.id.radio_single_room:
                if (checked)
                    // private room
                    announcement.setIsSharedRoom(0);
                break;
            case R.id.radio_shared_room:
                if (checked)
                    // shared room
                    announcement.setIsSharedRoom(1);
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode){
            case Activity.RESULT_OK:

                try {
                    if (requestCode == SELECT_FILE)
                        imageController.onSelectFromGalleryResult(data);
                    else if (requestCode == REQUEST_CAMERA) { //the photo is returned and positioned

                    }else if(requestCode == REQUEST_TAKE_PHOTO){
                        Log.d("extra", "ALL OK, putting image on imageview");
                        Uri image = Uri.fromFile(imageController.destination);
                        //Uri thumbnail = data.getData();
                        imageController.putImage(image);
                        images.add(image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_announcement, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_changes:
                if(checkFields())
                    saveData();
                else
                    Toast.makeText(context, getResources().getString(R.string.any_fields_error),
                                    Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //establishes the font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void saveData() {

        String title = etTitle.getText().toString();
        announcement.setTitle(title);
        Double price = 0.0;
        price = Double.parseDouble(etPrice.getText().toString());
        announcement.setPrice(price);
        String description = etDescription.getText().toString();
        announcement.setDescription(description);
        String availability = etAvailability.getText().toString();
        announcement.setAvailability(new Date(availability));
        //TODO get kind of availability (day, weeks, months..)
        String selectedSpinner = spinner.getSelectedItem().toString();
        String min_Stay = etMinStay.getText().toString();
        announcement.setMinStay(Integer.parseInt(min_Stay));
        String max_Stay = etMaxStay.getText().toString();
        announcement.setMaxStay(Integer.parseInt(max_Stay));
        //Extras.
        // First we create a new arraylist
        // with the selected services and after we update the database
        ArrayList<Service> services = new ArrayList<>();
        if(servicesPressed != null){
            for(int i = 0; i < servicesPressed.length; i++){
                if (servicesPressed[i]){
                    String serviceName = this.services.get(i).getName();
                    services.add(new Service(i, serviceName));
                }
            }
        }

        if(idUser != 0) {
            announcement.setIdUser(idUser);
            accommodation.setIdUser(idUser);
        }
        String address = etAddress.getText().toString();
        accommodation.setLocation(address);

        //ACCOMMODATION AND ANNOUNCEMENT
        Call<Accommodation> accommodationCall = api.createAccommodation(accommodation);
        accommodationCall.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if(response.code() == 200)
                    accommodation = response.body();
            }
            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) { call.cancel(); }
        });

        announcement.setIdAccommodation(accommodation.getId());

        //create announcement
        Call<Announcement> announcementCall = api.createAnnouncement(announcement);
        announcementCall.enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                if(response.code() == 200)
                    announcement = response.body();
            }
            @Override
            public void onFailure(Call<Announcement> call, Throwable t) { call.cancel(); }
        });

        //update accommodation announcement id
        accommodation.setIdAnnouncement(announcement.getId());
        Call<ResponseBody> accommodationUpdateCall = api.updateAccommodation(accommodation, accommodation.getId());
        accommodationUpdateCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                    Log.d("Accommodation update", response.body().toString());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { call.cancel(); }
        });

        boolean is_main = true;
        //images
        for (Uri imagePath : images) {
            File f = new File("" + imagePath);
            Image image = new Image(f.getName(), announcement.getId());
            uploadFile(new File(imagePath.getPath()), image);
        }

        //SERVICES
        Call<ResponseBody> servicesCall = api.setServices(services);
        servicesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                    Log.d("Accommodation services", response.body().toString());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { call.cancel(); }
        });

    }

    //check fields if are correct
    public boolean checkFields(){
        boolean result = true;

        DateValidator validator = new DateValidator();

        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String address = etAddress.getText().toString();
        String description = etDescription.getText().toString();
        String availability = etAvailability.getText().toString();
        String min_stay = etMinStay.getText().toString();
        String max_stay = etMaxStay.getText().toString();

        //if any field is empty we return false
        if(title.equals("") || price.equals("") || address.equals("") ||
                description.equals("") || availability.equals("") ||
                !validator.validate(availability) || min_stay.equals("") ||
                max_stay.equals(""))
            result = false;

        //now check if a specific field is empty, we alert of that
        if(title.equals(""))
            etTitle.setError(getResources().getString(R.string.error_title));
        if(price.equals(""))
            etPrice.setError(getResources().getString(R.string.error_price));
        if(address.equals(""))
            etAddress.setError(getResources().getString(R.string.error_address));
        if(description.equals(""))
            etDescription.setError(getResources().getString(R.string.error_description));

        if(availability.equals(""))
            etAvailability.setError(getResources().getString(R.string.error_availability));
        //now we transform to USA date and check it
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("date", sdf.format(myCalendar.getTime()));
        if(!validator.validate(sdf.format(myCalendar.getTime())))
            etAvailability.setError(getResources().getString(R.string.error_availability_date));

        if(min_stay.equals(""))
            etMinStay.setError(getResources().getString(R.string.error_min_stay));
        if(max_stay.equals(""))
            etMaxStay.setError(getResources().getString(R.string.error_max_stay));

        Log.d("CheckFields", String.valueOf(result));
        return result;
    }

    private void uploadFile(File file, Image image) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), file.getName());

        // finally, execute the request uploading the file
        Call<ResponseBody> call = api.uploadImageAnnouncement(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.e("rest code: ",  String.valueOf(response.code()));
                if(response.code() == 200){
                    Log.v("Upload", "success");
                }else if(response.code() == 204)
                    Log.v("Upload", "failed");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

        //now create a new Image object on database associating with the announcement
        Call<Image> callImage = api.createImage(image);
        callImage.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call,
                                   Response<Image> response) {
                if(response.code() == 200)
                    Log.e("Image",  String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) { call.cancel(); }
        });
    }

    public void selectImage() {
        final CharSequence[] items = { "Hacer foto con la cámara", "Elegir de la galería",
                "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Add Photo!");
        final Activity activity = this;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(context);

                //checks if user has permission to read/write storage
                if (items[item].equals("Hacer foto con la cámara")) {
                    userChoosenTask="Hacer foto con la cámara";
                    result = Utility.checkWriteExternalStorage(activity);
                    if(result){
                        dispatchTakePictureIntent();
                    }

                } else if (items[item].equals("Elegir de la galería")) {
                    userChoosenTask="Elegir de la galería";
                    if(result)
                        imageController.galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void dispatchTakePictureIntent() {

        //first check permission for write on storage
        if(Utility.checkWriteExternalStorage(this)){
            Intent takePictureIntent = new Intent(ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    imageController.destination = createImageFile();

                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (imageController.destination != null) {

                    Uri photoURI = FileProvider.getUriForFile(context,
                            "com.flattomate.fileprovider",
                            imageController.destination);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        Calendar cal = Calendar.getInstance();
        String imageFileName =  ""+ cal.getTimeInMillis();
        Log.d("storagedir", Environment.getExternalStorageDirectory() + "/flattomate");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                Log.d("permissions", "checking");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Hacer foto con la cámara"))
                        startActivityForResult(intent, REQUEST_CAMERA); //cameraIntent();
                    else if(userChoosenTask.equals("Elegir de la galería"))
                        imageController.galleryIntent();
                } else {
                    //code for deny
                    Toast.makeText(context, "Debes de dar permisos a la aplicación para poder insertar fotos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}