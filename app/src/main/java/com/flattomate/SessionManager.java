package com.flattomate;

import android.content.Context;
import android.content.SharedPreferences;

import com.flattomate.Model.User;

/**
 * Created by kike on 18/5/16.
 */
public class SessionManager {

    SharedPreferences.Editor editor; //write
    SharedPreferences prefs; //read


    public SessionManager(Context context){
        prefs = context.getSharedPreferences("settings", 0);
        editor = context.getSharedPreferences("settings", 0).edit();
    }

    public void setUserPreferences(User user) {

        //datos en la sesion del usuario
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("avatar", user.getId()+".jpg");
        editor.putString("birthdate", user.getBirthdate());
        editor.putString("activity", user.getActivity());
        editor.putString("sex", user.getSex());
        editor.putInt("smoke", user.getSmoke());
        editor.putInt("sociable", user.getSociable());
        editor.putInt("tidy", user.getTidy());
        editor.putString("bio", user.getBio());

       /* if(editor.commit())
            Log.d("DEBUG ", "Settings almacenado correctamente");*/
        editor.apply();

    }
    
    public String getValue(String key) {

        return prefs.getString(key, "");
    }

    public void setValue(String key, String value) {

        editor.putString(key, value);
        editor.apply();
    }

}