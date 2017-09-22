package com.flattomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flattomate.Announcement.AnnouncementActivity;
import com.flattomate.Model.Announcement;
import com.flattomate.Model.Image;
import com.flattomate.Model.Review;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flattomate.R.id.ad_adapter_image;
import static com.flattomate.R.id.ad_adapter_price;
import static com.flattomate.R.id.ad_adapter_title;
import static com.flattomate.R.id.ad_adapter_user_num_reviews;
import static com.flattomate.R.id.ad_adapter_user_rating;
import static com.flattomate.R.id.ad_adapter_user_ratingbar;

/**
 * Adapter that load a list of announcements and put dinamycally on views
 */

public class AnnouncementAdapter extends ArrayAdapter<Announcement>{

    private Context _context;
    private ArrayList<Announcement> _announcements;
    private ArrayList<Review> _reviews;
    int _resource;

    FlattomateService api;
    SharedPreferences manager;




    public AnnouncementAdapter(Context context, int resource, ArrayList<Announcement> announcements){
        super(context, resource, announcements);
        _context = context;
        _announcements = announcements;
        _resource = resource;
        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        manager = _context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        //int idUser = _announcements.get(position).getIdUser();
        final int idAnnouncement = _announcements.get(position).getId();
        //Toast.makeText(_context, "ad " + idAnnouncement, Toast.LENGTH_SHORT).show();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.announcement_adapter, null, true);

            holder = new ViewHolder();

            holder.ad_adapter_image = (ImageView) convertView.findViewById(ad_adapter_image);
            holder.ad_adapter_title = (TextView) convertView.findViewById(ad_adapter_title);
            holder.ad_adapter_price = (TextView) convertView.findViewById(ad_adapter_price);
            holder.ad_adapter_user_ratingbar = (RatingBar) convertView.findViewById(ad_adapter_user_ratingbar);
            holder.ad_adapter_user_rating = (TextView) convertView.findViewById(ad_adapter_user_rating);
            holder.ad_adapter_user_num_reviews = (TextView) convertView.findViewById(ad_adapter_user_num_reviews);

            convertView.setTag(holder);

        }else
            holder = (ViewHolder) convertView.getTag();

        //get main image from announcement and put in the ImageView
        Call<Image> imageCall = api.getAnnouncementMainImage(idAnnouncement);
        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                Log.e("CODIGO imagen" , String.valueOf(response.code()));

                if(response.code() == 200) {
                    Log.e("IMAGEN PRINCIPAL1" , "");
                    if (response.body() != null){
                        Log.e("IMAGEN PRINCIPAL2" , response.body().getName());
                        Picasso.with(_context)
                                .load(restAPI.API_BASE_URL+"announcements/"+response.body().getName())
                                .placeholder(R.drawable.no_camera_24)
                                .resize(holder.ad_adapter_image.getWidth(), holder.ad_adapter_image.getHeight())
                                .into(holder.ad_adapter_image);
                    }else
                        Log.e("ERROR", "No images for ad " + idAnnouncement );
                }
                else
                    Log.e("ERROR", "Image can not be retrieved");
            }
            @Override
            public void onFailure(Call<Image> call, Throwable t) { call.cancel(); Log.e("ERROR" , "PETE " + t.getMessage());}
        });

        //set title
        holder.ad_adapter_title.setText(_announcements.get(position).getTitle());

        //set price
        int rentKindNumber = _announcements.get(position).getRent_kind();
        String rentKind;
        switch(rentKindNumber){
            case 0: rentKind = "€/día"; break;
            case 1: rentKind = "€/semana"; break;
            case 2: rentKind = "€/mes"; break;
            case 3: rentKind = "€/año"; break;
            default: rentKind = "€/mes";
        }
        holder.ad_adapter_price.setText(String.valueOf(_announcements.get(position).getPrice()) + rentKind);

        //ratingbar configuration
        holder.ad_adapter_user_ratingbar.setMax(5);
        holder.ad_adapter_user_ratingbar.setNumStars(5);

        //call to API for getting reviews from ad
        Call<ArrayList<Review>> reviewsCall = api.getReviews(idAnnouncement);
        final View finalConvertView = convertView;
        reviewsCall.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                if(response.code() == 200)
                    _reviews = response.body();
                else
                    Log.e("ERROR", "Reviews can not be retrieved");

                if(_reviews != null){
                    final int numReviews;
                    final float reviewsSum;
                    final float avg;
                    float reviewsSumTemp = 0;
                    numReviews = _reviews.size();

                    for (Review review: _reviews)  //get rating average
                        reviewsSumTemp += review.getRating();

                    reviewsSum = reviewsSumTemp;
                    avg = reviewsSum / numReviews;

                    //set ratingbar and number of reviews
                    holder.ad_adapter_user_ratingbar.setRating(avg);

                    if(Float.isNaN(avg))
                        holder.ad_adapter_user_rating.setText("("+0+") ");
                    else
                        holder.ad_adapter_user_rating.setText("("+String.format("%.1f", avg)+") ");

                    holder.ad_adapter_user_num_reviews.setText(numReviews + " evaluaciones");

                    LinearLayout dashboard_adapter_layout = (LinearLayout) finalConvertView.findViewById(R.id.dashboard_adapter_layout);
                    dashboard_adapter_layout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                            Intent activityAnnouncement = new Intent(_context, AnnouncementActivity.class);
                            activityAnnouncement.putExtra("idAnnouncement", idAnnouncement);
                            activityAnnouncement.putExtra("idUser", manager.getInt("id", 0));
                            activityAnnouncement.putExtra("avg", avg);
                            activityAnnouncement.putExtra("reviewsSum", reviewsSum);
                            activityAnnouncement.putExtra("numReviews", numReviews);
                            _context.startActivity(activityAnnouncement);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) { call.cancel(); }
        });

        return convertView;

    }
    private class ViewHolder {
        ImageView ad_adapter_image;
        TextView ad_adapter_title;
        TextView ad_adapter_price;
        RatingBar ad_adapter_user_ratingbar;
        TextView ad_adapter_user_rating;
        TextView ad_adapter_user_num_reviews;
    }

}
