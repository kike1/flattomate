package com.flattomate.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.flattomate.AnnouncementAdapter;
import com.flattomate.Model.Announcement;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that represent a tab in dashboard that is a list of Announcement
 */

public class TabAnnouncementList extends Fragment {


    ListView list_view;
    public static final String ARG_OBJECT = "object";

    FlattomateService api;
    //SharedPreferences manager;
    ArrayList<Announcement> announcements;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.tab_announcement_list, container, false);
        list_view = (ListView) rootView.findViewById(R.id.dashboard_list_view);
        populateAnnouncementList();

        Bundle args = getArguments();

        return rootView;
    }

    private void populateAnnouncementList() {
        final Call<ArrayList<Announcement>> request = api.getAnnouncements();
        request.enqueue(new Callback<ArrayList<Announcement>>() {
            @Override
            public void onResponse(final Call<ArrayList<Announcement>> call,
                                   final Response<ArrayList<Announcement>> response) {
                announcements = response.body();

                if (announcements != null) {
                    AnnouncementAdapter adapter = new AnnouncementAdapter(getContext(),
                            R.layout.announcement_adapter, announcements);
                    list_view.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(final Call<ArrayList<Announcement>> call, final Throwable t) {
                call.cancel();
            }
        });
    }
}
