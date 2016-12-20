package com.flattomate.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;

import static com.google.android.gms.measurement.internal.zzl.api;

/**
 * Class that represent a tab in dashboard with user account
 */

public class TabMyAccount extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.activity_own_profile, container, false);
        //list_view = (ListView) rootView.findViewById(R.id.dashboard_list_view);
        //populateAnnouncementList();

        //Bundle args = getArguments();

        return rootView;
    }

}
