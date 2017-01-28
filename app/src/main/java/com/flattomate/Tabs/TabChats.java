package com.flattomate.Tabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flattomate.Model.User;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;

import butterknife.ButterKnife;

/**
 * Class that represent a tab in dashboard that have all chats
 */

public class TabChats extends Fragment {

    User user;
    FlattomateService api;
    int idUser;
    public static SharedPreferences _manager;
    private static final String ID_USER = "id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

        //idUser = _manager.getInt(ID_USER,0);

        Log.e("","CHATFragment.onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.tab_chat, container, false);

        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);

        populateFragment();

        return rootView;
    }

    private void populateFragment() {


    }
}
