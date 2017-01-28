package com.flattomate.Tabs;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.flattomate.AnnouncementAdapter;
import com.flattomate.Constants;
import com.flattomate.FetchAddressIntentService;
import com.flattomate.Model.Announcement;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.flattomate.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.flattomate.Constants.RESULT_ADDRESS;

/**
 * Class that represent a tab in dashboard that is a list of Announcement
 */

public class TabAnnouncementList extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ListView list_view;
    public static final String ARG_OBJECT = "object";

    FlattomateService api;
    //SharedPreferences manager;
    ArrayList<Announcement> announcements;
    GoogleApiClient mGoogleApiClient = null;
    Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    HashMap<Float, Announcement> nearAds = new HashMap<>();
    TreeMap<Float, Integer> distanceIDAds = new TreeMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

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

                ArrayList<Announcement> nearAnnouncements = new ArrayList<>();

                if (announcements != null) {
                    AnnouncementAdapter adapter = new AnnouncementAdapter(getContext(),
                            R.layout.announcement_adapter, announcements);
                    list_view.setAdapter(adapter);
                    mGoogleApiClient.connect();
                }
            }
            @Override
            public void onFailure(final Call<ArrayList<Announcement>> call, final Throwable t) {
                call.cancel();
            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /*
    * Connect to GoogleApi and calculate distances between user and announcements
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        boolean result = Utility.checkPermission(getActivity().getApplicationContext());
        result = Utility.checkGPS(getActivity());
        if(result){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                for (Announcement ad: announcements){
                    //if the ad does not have an addres not show it
                    if(!ad.getAccommodation().getLocation().contains("") &&
                            ad.getAccommodation().getLocation() != null)
                        startIntentService(ad);
                }

            }

        }
    }

    protected void startIntentService(Announcement ad) {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, ad.getAccommodation().getLocation());
        //intent.putExtra(Constants.ID_ANNOUNCEMENT, ad.getId());
        getContext().startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            Float distance = Float.parseFloat(resultData.getString(Constants.RESULT_DATA_DISTANCE));
            Integer idAnnouncement = Integer.parseInt(resultData.getString(Constants.RESULT_DATA_ID_ANNOUNCEMENT));
            Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
            Log.d("Address", address.getExtras().getString(RESULT_ADDRESS));
            distanceIDAds.put(distance, idAnnouncement);
            //displayDistanceOutput();

            // Show a toast message if an address was found.
            /*f (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }*/

        }
    }

    private void displayDistanceOutput() {

        //TODO put distance on top tag
    }
}

/*class AddressResultReceiver extends ResultReceiver {

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, final Bundle resultData) {
        if (resultCode == Constants.SUCCESS_RESULT) {
            final Address address = resultData.getParcelable(RESULT_ADDRESS);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                            "Longitude: " + address.getLongitude() + "\n" +
                            "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));
                }
            });
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                }
            });
        }
    }
}*/



