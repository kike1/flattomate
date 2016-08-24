package myapp.flattomate.REST;

import java.util.List;

import myapp.flattomate.Model.Language;
import myapp.flattomate.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kike on 19/5/16.
 */
public interface FlattomateService{
    @GET("users/{id}")
    Call<User> getUser(@Path("id") Integer id);

    @GET("users/index")
    Call<List<User>> allUsers();

    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("users")
    Call<User> register(@Body User user);

    @POST("users/{name}/{password}")
    Call<User> login(@Path("name") String name, @Path("password") String password);

    @GET("users/{iduser}/languages")
    Call<List<Language>> getLanguages(@Path("iduser") int iduser);

    @POST("languages/add/{iduser}/{idlanguage}")
    Call<List<Language>> setLanguage(@Path("iduser") Integer iduser, @Path("idlanguage") Integer idlanguage);

    @DELETE("languages/remove/{iduser}/{idlanguage}")
    Call<List<Language>> removeLanguage(@Path("iduser") Integer iduser, @Path("idlanguage") Integer idlanguage);

}