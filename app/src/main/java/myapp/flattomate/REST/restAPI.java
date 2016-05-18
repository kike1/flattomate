package myapp.flattomate.REST;

import java.util.List;

import myapp.flattomate.Model.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by kike on 15/5/16.
 */
public class restAPI {

    private static final String ENDPOINT = "http://192.168.2.102:8000/";
    private final FlattomateService mService;

    public interface FlattomateService{
        @GET("users/{username}")
        Call<User> getUser(@Path("username") Integer username);

        @GET("users/index")
        Call<List<User>> allUsers();

        @GET("group/{id}/users")
        Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

        @POST("users")
        Call<User> register(@Body User user);

        @GET("users/{name}/{password}")
        Call<User> login(@Path("name") String name, @Path("password") String password);

        @GET("users")
        Call<User> emailUsed(String email);

    }

    public restAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(FlattomateService.class);
    }

    public FlattomateService getService(){ return mService; }


}
