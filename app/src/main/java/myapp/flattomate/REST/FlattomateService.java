package myapp.flattomate.REST;

import java.util.List;

import myapp.flattomate.Model.Language;
import myapp.flattomate.Model.User;
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
}