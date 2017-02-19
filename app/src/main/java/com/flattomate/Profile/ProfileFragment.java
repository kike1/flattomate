package com.flattomate.Profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flattomate.Constants;
import com.flattomate.HomeActivity;
import com.flattomate.Model.Language;
import com.flattomate.Model.Review;
import com.flattomate.Model.User;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

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

    @Bind(R.id.progressbarSociable)
    ProgressBar progressBarSociable;
    @Bind(R.id.progressbarTidy)
    ProgressBar progressBarTidy;

    @Bind(R.id.btn_logout)
    Button btn_logout;
    @Bind(R.id.btn_review)
    Button btn_review;

    @Bind(R.id.ratingBar)
    RatingBar ratingBarReviews;
    @Bind(R.id.user_rating_reviews)
    TextView user_rating_reviews;


    //dialog review items
    RatingBar ratingBar;
    TextView tvReviewDescription;

    User user;
    FlattomateService api;
    int idUser;
    int idAnnouncement;
    Boolean ownUser;
    public static SharedPreferences _manager;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID_USER = "id";

//    public ProfileFragment() {
//        // Required empty public constructor
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idUser
     * @param manager
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(int idUser, SharedPreferences manager) {

        _manager = manager;
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ID_USER, idUser);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

        idUser = getArguments().getInt(Constants.ID_USER);
        idAnnouncement = getArguments().getInt(Constants.ID_ANNOUNCEMENT);
        ownUser = false;
        Log.e("","ProfileFragment.onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.activity_own_profile, container, false);

        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);

        populateFragment();

        return rootView;
    }

    private void populateFragment() {
        //hide the button until user is the same than profile
        btn_logout.setVisibility(View.GONE);
        btn_review.setVisibility(View.GONE);

        if (getArguments() != null) {
            idUser = getArguments().getInt(ID_USER);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }else
            Log.e("ERROR","User is 0");

        //obtain data for the user to see the profile
        //fillUserInfo();
        Call<User> call = api.getUser(idUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200) {

                    if(response.isSuccessful()) {
                        user = response.body();
                        //user.setAges();
                        //user.setLanguages();
                        if(user != null) {
                            //hide logout if not own user
                            if(user.getId() == _manager.getInt(ID_USER,0)) {
                                btn_logout.setVisibility(View.VISIBLE);
                                ownUser = true;
                            }

                            if(getActivity() != null && !getActivity().getTitle().equals("Dashboard"))
                                getActivity().setTitle(user.getName());
                            fillUserInfo();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "Error de conexión al servidor", Toast.LENGTH_LONG).show();
            }
        });

        //Shows profile img in fullscreen
        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowImageProfile.class);
                intent.putExtra("image", user.getId()+".jpg");
                intent.putExtra("username", user.getName());
                startActivity(intent);
            }
        });

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch dialog with stars
                //Toast.makeText(getContext(), "REVIEW", Toast.LENGTH_SHORT).show();
                onCreateDialog().show();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear sharedpreferences
                SharedPreferences.Editor editor = _manager.edit();
                editor.clear();
                editor.commit();
                //redirect to HomeActivity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public Dialog onCreateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_review, null);
        ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
        ratingBar.setNumStars(5);
        ratingBar.setMax(5);
        tvReviewDescription = (TextView) v.findViewById(R.id.description);

        builder.setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String reviewDescription = ""+ tvReviewDescription.getText();
                        float rating = (Float) ratingBar.getRating();

                        //TODO api review
                        Log.d("review", reviewDescription);
                        Log.d("rating", String.valueOf(rating));

                        int myIdUser =_manager.getInt("id",0);
                        if(myIdUser != 0){
                            Review review = new Review(reviewDescription, myIdUser, idUser, idAnnouncement, rating);

                            Call<ResponseBody> requestReview = api.makeReview(review);
                            requestReview.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.code() == 200) {
                                        Toast.makeText(getContext(), getString(R.string.txt_review_ok), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) { call.cancel(); }
                            });
                        }


                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });

        return builder.create();
    }

    void fillUserInfo(){

        String undefined = getResources().getString(R.string.undefined);
        String sName = user.getName();
        int iAges = user.getAges();
        ArrayList<Language> sLanguages = user.getLanguages();
        String sBio = user.getBio();
        String sAvatar = user.getId()+".jpg";
        int iSociable = user.getSociable();
        int iTidy = user.getTidy();
        String sex = user.getSex();
        String activity = user.getActivity();
        int smoke = user.getSmoke();

        tvName.setText(sName);
        tvAge.setText(String.valueOf(iAges)+" años");

        if(sLanguages != null && sLanguages.size() > 0){
            StringBuilder languagesString = new StringBuilder();
            for (Language lang: sLanguages)
            {
                languagesString.append(lang.getName() + " - ");
            }
            String languagesFormatted = languagesString.substring(0, languagesString.length()-3);
            //formatLanguage(languagesFormatted.toString());
            tvLanguages.setText(languagesFormatted);
        }else{
            tvLanguages.setText(undefined);
        }

        if(sBio != null)
            tvBio.setText(sBio);
        else
            tvBio.setText(undefined);

        tvSociable.setText(iSociable+"/10");
        tvTidy.setText(iTidy+"/10");

        //icons
        if(sex != null && sex.equals("man")){
            ivSex.setImageResource(R.drawable.male);
            tvSex.setText("Hombre");
        }else if(sex != null && sex.equals("woman")){
            ivSex.setImageResource(R.drawable.female);
            tvSex.setText("Mujer");
        }else{
            ivSex.setImageResource(R.drawable.female);
            tvSex.setText(undefined);
        }
        int color = Color.parseColor("#0288D1");
        ivSex.setColorFilter(color);

        if(activity != null && activity.equals("work")){
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

        //setting progress of bars
        progressBarSociable.setMax(10);
        progressBarSociable.setProgress(iSociable);
        progressBarSociable.setBackgroundColor(Color.parseColor("#000000"));
        progressBarTidy.setMax(10);
        progressBarTidy.setProgress(iTidy);
        progressBarTidy.setBackgroundColor(Color.parseColor("#000000"));

        Log.d("avatar", restAPI.API_BASE_URL+"imgs/"+sAvatar);
        //personalizing view of the user's profile
        if(sAvatar != null){
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(1)
                    .cornerRadiusDp(60)
                    .oval(false)
                    .build();

            Picasso.with(getActivity())
                    .load(restAPI.API_BASE_URL+"imgs/"+sAvatar)
                    .placeholder(R.drawable.user_default)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .fit()
                    .centerCrop()
                    .transform(transformation)
                    .into(ivProfileImg);
        }

        //set ratingBar
        //if(!ownUser){
            ratingBarReviews.setNumStars(5);
            ratingBarReviews.setMax(5);
            ratingBarReviews.setIsIndicator(true);

            Call<ArrayList<Review>> requestReviews = api.getReviewsFromUser(idUser);

            requestReviews.enqueue(new Callback<ArrayList<Review>>() {
                @Override
                public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                    if(response.code() == 200) {
                        ArrayList<Review> reviews = response.body();
                        float ratingSum = 0;
                        float ratingAvg = 0;
                        Boolean userHasReviewed = false;

                        if(reviews != null && reviews.size() != 0){
                            int myId = _manager.getInt(Constants.ID_USER,0);
                            for(Review review : reviews){
                                ratingSum += review.getRating();

                                //did user review this ad?
                                if(myId != 0 &&
                                        review.getIdUserWrote() == myId &&
                                        review.getIdAnnouncement() == idAnnouncement){
                                    userHasReviewed = true;
                                }
                            }

                            ratingAvg = ratingSum / reviews.size();
                            ratingBarReviews.setRating(ratingAvg);
                            user_rating_reviews.setText("("+String.format("%.1f", ratingAvg)+") ");
                        }else{
                            ratingBarReviews.setRating(0);
                            user_rating_reviews.setText("("+0+") ");
                        }

                        //if I am the user of the profile,
                        if(ownUser || userHasReviewed)
                            btn_review.setVisibility(View.GONE);
                        else
                            btn_review.setVisibility(View.VISIBLE);
                    }else{
                        Log.e("REST-rating", "User " + idUser + "no tiene reviews, codigo de error:" + String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Review>> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getActivity(), "Error de conexión al servidor", Toast.LENGTH_LONG).show();
                }
            });

        //}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //if I am the user of profile show edit button
        if(idUser == _manager.getInt("id",0)) {
            inflater.inflate(R.menu.profile_menu, menu);
            for (int i = 0; i < menu.size(); i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(getActivity().getApplicationContext(), EditProfileActivity.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){

        }
    }
}
