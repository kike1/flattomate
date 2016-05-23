package myapp.flattomate;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import myapp.flattomate.Model.User;

/**
 * Created by kike on 18/5/16.
 */
public class SessionManager {
    public void setPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setPreferences(Context context, User user) {
        SharedPreferences.Editor editor = context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();

        //datos en la sesion del usuario
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("activity", user.getActivity());
        editor.putString("avatar", user.getAvatar());
        editor.putString("bio", user.getBio());

        if(editor.commit())
            Log.d("DEBUG ", "Settings almacenado correctamente");

    }
    
    public  String getPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("settings",	Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public String getLoginEmailAddress(Context context){
        return getPreferences(context, "email");
    }
}