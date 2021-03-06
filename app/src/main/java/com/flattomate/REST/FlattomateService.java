package com.flattomate.REST;

import com.flattomate.Model.Accommodation;
import com.flattomate.Model.Announcement;
import com.flattomate.Model.Chat;
import com.flattomate.Model.Image;
import com.flattomate.Model.Language;
import com.flattomate.Model.Review;
import com.flattomate.Model.Service;
import com.flattomate.Model.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;

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

    @POST("user/{id}/chat/{idUserAnnouncement}/{idAnnouncement}")
    Call<ResponseBody> requestNegotiation(@Path("id") Integer id,
                                          @Path("idUserAnnouncement") Integer idUserAnnouncement,
                                          @Path("idAnnouncement") Integer idAnnouncement);

    @GET("user/{id}/chat/{idUserAnnouncement}/{idAnnouncement}")
    Call<JsonObject> isRequestedNegotiation(@Path("id") Integer id,
                                            @Path("idUserAnnouncement") Integer idUserAnnouncement,
                                            @Path("idAnnouncement") Integer idAnnouncement);

    @GET("user/{id}/chats")
    Call<ArrayList<Chat>> chatsFromUser(@Path("id") Integer id);
    @POST("user/chat")
    Call<ResponseBody> sendChatMessage(@Body Chat chat);

    @GET("user/{id}/chats/recent")
    Call<ArrayList<Chat>> lastChatsFromUser(@Path("id") Integer id);

    @GET("user/{idr}/chats/{idw}/announcement/{idad}")
    Call<ArrayList<Chat>> chatsFromAnnouncement(@Path("idr") Integer idr,
                                                @Path("idw") Integer idw,
                                                @Path("idad") Integer idad);

    @POST("user/{name}/{password}")
    Call<User> login(@Path("name") String name, @Path("password") String password);

    @GET("user/{iduser}/languages")
    Call<ArrayList<Language>> getLanguages(@Path("iduser") int iduser);

    @POST("languages/add/{iduser}/{idlanguage}")
    Call<ArrayList<Language>> setLanguage(@Path("iduser") Integer iduser, @Path("idlanguage") Integer idlanguage);

    @DELETE("languages/remove/{iduser}/{idlanguage}")
    Call<ArrayList<Language>> removeLanguage(@Path("iduser") Integer iduser, @Path("idlanguage") Integer idlanguage);

    /*
    * Announcement
    *
    */

    //create new announcement
    @POST("announcement")
    Call<Announcement> createAnnouncement(@Body Announcement announcement);

    //update announcement
    @PUT("announcement/{id}")
    Call<ResponseBody> updateAnnouncement(@Body Announcement announcement, @Path("id") Integer id);

    @Multipart
    @POST("announcement/{id}/upload")
    Call<ResponseBody> uploadImageAnnouncement(@Part MultipartBody.Part file,
                                               @Path("id") Integer id);

    @GET("announcement")
    Call<ArrayList<Announcement>> getAnnouncements();

    @GET("announcement/search/{title}")
    Call<ArrayList<Announcement>> getAnnouncementsWithQuery(@Path("title") String title);

    @GET("announcement/{id}")
    Call<Announcement> getAnnouncement(@Path("id") Integer id);

    @GET("announcement/{id}/images")
    Call<ArrayList<Image>> getAnnouncementImages(@Path("id") Integer id);
    @GET("announcement/{id}/mainimage")
    Call<Image> getAnnouncementMainImage(@Path("id") Integer id);

    @GET("announcement/{id}/services")
    Call<ArrayList<Service>> getAnnouncementServices(@Path("id") Integer id);

    @GET("announcement/{id}/user")
    Call<User> getAnnouncementUser(@Path("id") Integer id);

    @GET("announcement/{id}/accommodation")
    Call<Accommodation> getAnnouncementAccommodation(@Path("id") Integer id);

    @GET("announcement/last")
    Call<Announcement> getLastAnnouncement();

    @GET("service")
    Call<ArrayList<Service>> getServices();

    //get ad reviews
    @GET("announcement/{id}/reviews")
    Call<ArrayList<Review>> getReviews(@Path("id") Integer id);
    @GET("user/{id}/reviews")
    Call<ArrayList<Review>> getReviewsFromUser(@Path("id") Integer id);
    //make a review
    @POST("announcement/review")
    Call<ResponseBody> makeReview(@Body Review review);

   /*
    *
    * Accommodation
    */

    //create new accommodation
    @POST("accommodation")
    Call<Accommodation> createAccommodation(@Body Accommodation accommodation);

    //update accommodation
    @PUT("accommodation/{id}")
    Call<ResponseBody> updateAccommodation(@Body Accommodation accommodation, @Path("id") Integer id);


    /*
    * Image
     */
    @POST("image")
    Call<Image> createImage(@Body Image image);


    /*
    * Service
     */
    @POST("accommodation/{id}/service/{service}")
    Call<ResponseBody> setServices(@Path("id") Integer id, @Path("service") Integer service);
}