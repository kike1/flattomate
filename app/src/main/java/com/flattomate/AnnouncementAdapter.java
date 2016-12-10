package com.flattomate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flattomate.Model.Announcement;
import com.flattomate.Model.Image;
import com.flattomate.Model.Review;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adapter that load a list of announcements and put dinamycally on views
 */

public class AnnouncementAdapter extends BaseAdapter{

    @Bind(R.id.ad_adapter_image)
    ImageView ad_adapter_image;
    @Bind(R.id.ad_adapter_price)
    TextView ad_adapter_price;
    @Bind(R.id.ad_adapter_title)
    TextView ad_adapter_title;
    @Bind(R.id.ad_adapter_user_ratingbar)
    RatingBar ad_adapter_user_ratingbar;
    @Bind(R.id.ad_adapter_user_num_reviews)
    TextView ad_adapter_user_num_reviews;

    private Context _context;
    private ArrayList<Announcement> _announcements;
    private ArrayList<Review> _reviews;

    FlattomateService api;

    public AnnouncementAdapter(Context context, ArrayList<Announcement> announcements){
        super();

        _context = context;
        _announcements = announcements;

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //int idUser = _announcements.get(position).getIdUser();
        int idAnnouncement = _announcements.get(position).getId();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.announcement_adapter, null);

            //get main image from announcement and put in the ImageView
            Call<Image> imageCall = api.getAnnouncementMainImage(idAnnouncement);
            imageCall.enqueue(new Callback<Image>() {
                @Override
                public void onResponse(Call<Image> call, Response<Image> response) {
                    if(response.code() == 200) {
                        if (response.body() != null){
                            Picasso.with(_context)
                                    .load(restAPI.API_BASE_URL+"announcements/"+response.body().getName())
                                    .into(ad_adapter_image);
                        }
                    }
                    else
                        Log.e("ERROR", "Reviews can not be retrieved");
                }
                @Override
                public void onFailure(Call<Image> call, Throwable t) { call.cancel(); }
            });

            //set title
            ad_adapter_title.setText(_announcements.get(position).getTitle());
            int rentKindNumber = _announcements.get(position).getRent_kind();
            String rentKind;
            switch(rentKindNumber){
                case 0: rentKind = "€/día"; break;
                case 1: rentKind = "€/semana"; break;
                case 2: rentKind = "€/mes"; break;
                case 3: rentKind = "€/año"; break;
                default: rentKind = "€/mes";
            }
            ad_adapter_price.setText(String.valueOf(_announcements.get(position).getPrice()) + rentKind);

            //ratingbar configuration
            ad_adapter_user_ratingbar.setMax(5);
            ad_adapter_user_ratingbar.setNumStars(5);

            //call to API for getting reviews from ad
            Call<ArrayList<Review>> reviewsCall = api.getReviews(idAnnouncement);
            reviewsCall.enqueue(new Callback<ArrayList<Review>>() {
                @Override
                public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                    if(response.code() == 200)
                        _reviews = response.body();
                    else
                        Log.e("ERROR", "Reviews can not be retrieved");

                    if(_reviews != null){
                        int numReviews = _reviews.size();
                        float reviewsSum = 0;
                        float avg = reviewsSum / numReviews;

                        for (Review review: _reviews)  //get rating average
                            reviewsSum += review.getRating();

                        //set ratingbar and number of reviews
                        ad_adapter_user_ratingbar.setRating(avg);
                        ad_adapter_user_num_reviews.setText(numReviews + " evaluaciones");
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Review>> call, Throwable t) { call.cancel(); }
            });
        }

        return convertView;

    }
}
