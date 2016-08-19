package myapp.flattomate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import myapp.flattomate.Model.User;
import myapp.flattomate.REST.FlattomateService;
import myapp.flattomate.REST.restAPI;
import myapp.myapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity{

    Context context = this;

    @Bind(R.id.img_ownprofile)
    ImageView ivProfileImg;
    @Bind(R.id.transparent_edit_image)
    ImageView ivTransparentEdit;
    @Bind(R.id.background_edit)
    ImageView ivBackgroundEdit;
    @Bind(R.id.et_edit_name)
    TextView etName;
    @Bind(R.id.et_edit_birthdate)
    TextView etBirthdate;

    @Bind(R.id.txt_languages)
    TextView tvLanguages;

    @Bind(R.id.txt_bio)
    TextView tvBio;

    @Bind(R.id.layout_sex)
    LinearLayout layout_sex;
    @Bind(R.id.img_sex)
    ImageView ivSex;
    @Bind(R.id.txt_sex)
    TextView tvSex;

    @Bind(R.id.layout_activity)
    LinearLayout layout_activity;
    @Bind(R.id.img_activity)
    ImageView ivActivity;
    @Bind(R.id.txt_activity)
    TextView tvActivity;

    @Bind(R.id.layout_smoke)
    LinearLayout layout_smoke;
    @Bind(R.id.img_smoke)
    ImageView ivSmoke;
    @Bind(R.id.txt_smoke)
    TextView tvSmoke;

    @Bind(R.id.btn_add_language)
    Button btnAddLanguage;

    @Bind(R.id.txt_sociable)
    TextView tvSociable;
    @Bind(R.id.txt_tidy)
    TextView tvTidy;

    @Bind(R.id.sliderSociable)
    SeekBar sliderSociable;
    @Bind(R.id.sliderTidy)
    SeekBar sliderTidy;

    SharedPreferences manager;
    User user;
    CalendarView calendar;
    int year_x, month, day;
    static final int DIALOG_ID = 0;
    Calendar myCalendar = Calendar.getInstance();
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Editar perfil");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        manager = getSharedPreferences("settings", Context.MODE_PRIVATE);

        initCalendar();

        //set focuslistener in birthdate field showing a calendar in a dialog
        etBirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasfocus) {
                if(hasfocus){
                    DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                    dialog.show();
                }else{
                    Toast.makeText(context, "Fecha de nacimiento actualizada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //obtain data for the user to see the profile
        int idUser = manager.getInt("id", 0);
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

        //Shows profile img in fullscreen
        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO REVISAR add popup requesting photo/gallery image
                Intent intent = new Intent(getApplicationContext(), ShowImageProfile.class);
                intent.putExtra("image", user.getAvatar());
                intent.putExtra("username", user.getName());
                startActivity(intent);
            }
        });

        //Set listeners to icons for change options
        layout_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivityDialog(0);
            }
        });
        //Set listeners to icons for change options
        layout_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivityDialog(1);
            }
        });
        //Set listeners to icons for change options
        layout_smoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivityDialog(2);
            }
        });

        btnAddLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sliderSociable.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSociable.setText(progress+"/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        sliderTidy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTidy.setText(progress+"/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
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

        etName.setText(sName);
        //tvAge.setText(iAges+" años");
        tvLanguages.setText(sLanguages);
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

        //setting progress of sliders
        sliderSociable.setProgress(user.getSociable());
        sliderTidy.setProgress(user.getTidy());

       // String colorTransparent = Color.parseColor(String.valueOf(((int) (this.getResources().getColor(R.color.primary)))));
        //personalizing view of the user's profile
        if(sAvatar != null){
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(1)
                    .cornerRadiusDp(60)
                    .oval(false)
                    .build();

            Picasso.with(getApplicationContext())
                    .load(restAPI.API_BASE_URL+"imgs/"+user.getAvatar())
                    .fit()
                    .centerCrop()
                    .transform(transformation)
                    .into(ivProfileImg);

            //edit icon resource
            int resourceEdit = R.mipmap.ic_edit_black_24dp;

            Transformation transformationEditIcon = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(1)
                    .cornerRadiusDp(60)
                    .oval(false)
                    .build();

            //background
            Picasso.with(getApplicationContext())
                    .load(R.color.accent)
                    .fit()
                    .centerCrop()
                    .transform(transformationEditIcon)
                    .into(ivBackgroundEdit);

            //icon over the background
            Picasso.with(getApplicationContext())
                    .load(resourceEdit)
                    .fit()
                    .centerCrop()
                    .transform(transformationEditIcon)
                    .into(ivTransparentEdit);

        }
    }

    //sets calendar to a date 18 ages later as max date
    void initCalendar(){
        year_x = myCalendar.get(Calendar.YEAR);
        myCalendar.set(Calendar.YEAR, year_x-18);
        myCalendar.set(Calendar.MONTH, 0);
        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
        //myCalendar.set(Calendar.DAY_OF_WEEK, 1);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etBirthdate.setText(sdf.format(myCalendar.getTime()));
    }

    //shows dialogs depend of the option chooosen (sex, activity, smoke)
    public void showActivityDialog(int section) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        String[] items = new String[2];
        String contextDialog = null;

        //load the corresponding dialog
        switch (section){
            case 0:
                builder.setTitle(R.string.sex_title);
                items = getResources().getStringArray(R.array.sex_array);
                contextDialog = "sex";
                break;

            case 1:
                builder.setTitle(R.string.activity_title);
                items = getResources().getStringArray(R.array.activity_array);
                contextDialog = "activity";
                break;

            case 2:
                builder.setTitle(R.string.smoke_title);
                items = getResources().getStringArray(R.array.smoke_array);
                contextDialog = "smoke";
                break;

        }

        final String finalContextDialog = contextDialog;
        //element list
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Which", String.valueOf(which));
                        if(finalContextDialog != null) {
                            //sex
                            if (finalContextDialog.equals("sex")) {
                                Log.d("WHICH", String.valueOf(which));
                                if (which != 1) {
                                    user.setSex("man");
                                    ivSex.setImageResource(R.drawable.male);
                                    tvSex.setText("Hombre");
                                } else {
                                    user.setSex("woman");
                                    ivSex.setImageResource(R.drawable.female);
                                    tvSex.setText("Mujer");
                                }
                            }
                            //activity
                            if (finalContextDialog.equals("activity"))
                                if(which!=1) {
                                    user.setActivity("work");
                                    ivActivity.setImageResource(R.drawable.business);
                                    tvActivity.setText("Trabaja");
                                }else{
                                    user.setActivity("study");
                                    ivActivity.setImageResource(R.drawable.student);
                                    tvActivity.setText("Estudia");
                                }
                            //smoke
                            if (finalContextDialog.equals("smoke"))
                                if(which!=1) {
                                    user.setSmoke(1);
                                    ivSmoke.setImageResource(R.drawable.smoking);
                                    tvSmoke.setText("Fuma");
                                }else {
                                    user.setSmoke(0);
                                    ivSmoke.setImageResource(R.drawable.no_smoking);
                                    tvSmoke.setText("No fuma");
                                }
                        }
                    }
                });

        //user press ok
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        //user press cancel
        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        dialog = builder.create();
        // display dialog
        dialog.show();
    }

    //shows dialogs depend of the option chooosen (sex, activity, smoke)
    public void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        String[] items = getResources().getStringArray(R.array.languages_array);
        String contextDialog = null;
        builder.setTitle(R.string.language_title);

        //element list
        final boolean[] selectedItemsArray = new boolean[5];
        builder.setMultiChoiceItems(items, selectedItemsArray,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Lógica de elemento seleccionado

                    }
                });

        //user press ok
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedItemsArray.length; i++){
                            if(selectedItemsArray[i]){
                                switch(i){
                                    case 0: user.setLanguages("Español");
                                        break;
                                    case 1: user.setLanguages("Inglés");
                                        break;
                                    case 2: user.setLanguages("Alemán");
                                        break;
                                    case 3: user.setLanguages("Italiano");
                                        break;
                                    case 4: user.setLanguages("Francés");
                                        break;

                                }
                            }
                        }

                    }
                });

        //user press cancel
        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_changes:
                //TODO save data and go back
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public Dialog onCreateDialog(Bundle savedInstaceState){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getApplicationContext(), dpickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month = monthOfYear + 1;
            day = dayOfMonth;

            etBirthdate.setText(day+"/"+month+"/"+year_x);
        }
    };*/

}
