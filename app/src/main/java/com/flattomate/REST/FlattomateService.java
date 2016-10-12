package com.flattomate.REST;

import com.flattomate.Model.Accommodation;
import com.flattomate.Model.Announcement;
import com.flattomate.Model.Image;
import com.flattomate.Model.Language;
import com.flattomate.Model.Service;
import com.flattomate.Model.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by kike on 19/5/16.
 */
public interface FlattomateService{

    /*
    * User
    *
    */
    @GET("user/{id}")
    Call<User> getUser(@Path("id") Integer id);

    //create new user
    @POST("user")
    Call<User> register(@Body User user);

    //update user
    @PUT("user/{id}")
    Call<ResponseBody> updateUser(@Body User user, @Path("id") Integer id);

    @Multipart
    @POST("user/upload")
    Call<ResponseBody> uploadImageProfile(@Part("description") RequestBody description,
                                          @Part MultipartBody.Part file);
                                           //@Path("id") Integer id);

    @POST("user/{name}/{password}")
    Call<User> login(@Path("name") String name, @Path("password") String password);

    @GET("user/{iduser}/languages")
    Call<List<Language>> getLanguages(@Path("iduser") int iduser);

    @POST("languages/add/{iduser}/{idlanguage}")
    Call<List<Language>> setLanguage(@Path("iduser") Integer iduser, @Path("idlanguage") Integer idlanguage);

    @DELETE("languages/remove/{iduser}/{idlanguage}")
    Call<List<Language>> removeLanguage(@Path("iduser") Integer iduser, @Path("idlanguage") Integer idlanguage);

    /*
    * Announcement
    *
    */
    @GET("announcement/{id}")
    Call<Announcement> getAnnouncement(@Path("id") Integer id);

    @GET("announcement/{id}/images")
    Call<ArrayList<Image>> getAnnouncementImages(@Path("id") Integer id);

    @GET("announcement/{id}/services")
    Call<ArrayList<Service>> getAnnouncementServices(@Path("id") Integer id);

    @GET("announcement/{id}/user")
    Call<User> getAnnouncementUser(@Path("id") Integer id);

    @GET("announcement/{id}/accommodation")
    Call<Accommodation> getAnnouncementAccommodation(@Path("id") Integer id);
}