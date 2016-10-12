package com.flattomate;

import android.util.Log;

import com.flattomate.Model.Announcement;
import com.flattomate.Model.Image;
import com.flattomate.Model.Service;
import com.flattomate.Model.User;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by funkyou on 11/10/2016.
 */

public class AnnouncementController {

    FlattomateService api;

    Announcement _announcement;
    User _user;
    ArrayList<Service> _services;
    ArrayList<Image> _images;
    int idAnnouncement;

    AnnouncementController context = this;

    public AnnouncementController(int idAnnouncement, Announcement an, User user, ArrayList<Service> services) throws IOException {
        _announcement = an;
        _user = user;
        _services = services;
        this.idAnnouncement = idAnnouncement;
        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");


    }

    Announcement getAnnouncement(){ return _announcement; }
    User getUser(){ return _user; }
    ArrayList<Service> getServices(){ return _services; }

    public void setData() {
        //Getting Announcement
        Call<Announcement> callAnnouncement = api.getAnnouncement(idAnnouncement);
        callAnnouncement.enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                if(response.code() == 200) {
                    _announcement = response.body();
                    Log.e("ANNOUNCEMENT", _announcement.getTitle());
                }else
                    Log.d("Setting announcement", "ERROR");
            }
            @Override
            public void onFailure(Call<Announcement> call, Throwable t) {
                call.cancel();
            }
        });



        //Getting announcement's services
        Call<ArrayList<Service>> callServices = api.getAnnouncementServices(idAnnouncement);
        callServices.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if(response.code() == 200) {
                    _services = response.body();
                    Log.d("Setting services", "OK");
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
                    _user = response.body();
                    Log.e("USER", _user.getName());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
