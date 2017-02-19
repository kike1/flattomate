package com.flattomate.Tabs;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
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
import android.widget.Toast;

import com.flattomate.AnnouncementAdapter;
import com.flattomate.Constants;
import com.flattomate.DashboardActivity;
import com.flattomate.FetchAddressIntentService;
import com.flattomate.Model.Announcement;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.flattomate.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that represent a tab in dashboard that is a list of Announcement
 */

public class TabAnnouncementList extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ListView list_view;
    //public static final String ARG_OBJECT = "object";

    FlattomateService api;
    //Context context = getActivity().getApplicationContext();
    ArrayList<Announcement> announcements;
    GoogleApiClient mGoogleApiClient = null;
    Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    ArrayList<Integer> nearAds = new ArrayList<>();
    TreeMap<Float, Integer> distanceIDAds = new TreeMap<>();
    TreeMap<Integer, Address> distanceAddress = new TreeMap<>();

    LocationRequest mLocationRequest;

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

        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case Constants.REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        populateAnnouncementList("");
                        Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getActivity(), "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.tab_announcement_list, container, false);
        list_view = (ListView) rootView.findViewById(R.id.dashboard_list_view);
        populateAnnouncementList("");

        ((DashboardActivity)getActivity()).setFragmentRefreshListener(new DashboardActivity.FragmentRefreshListener() {
            public void onRefresh(String query) {

                populateAnnouncementList(query);
            }
        });
        return rootView;
    }

    private void populateAnnouncementList(String query) {

        final Call<ArrayList<Announcement>> request;

        if(query.isEmpty()){
            request = api.getAnnouncements();
        }else
            request = api.getAnnouncementsWithQuery(query);


        request.enqueue(new Callback<ArrayList<Announcement>>() {
            @Override
            public void onResponse(final Call<ArrayList<Announcement>> call,
                                   final Response<ArrayList<Announcement>> response) {
                announcements = response.body();

                if (announcements != null) {
                    setAdapterWithAnnouncements(announcements);
                    //mGoogleApiClient.connect();
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
    * Request location and check permissions about GPS allowing enable GPS as well
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        //populateAnnouncementList();
                        getLocationAndStartIntentService();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    getActivity(),
                                    Constants.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    /*
    * Calculates distances between ads and user.
    * Set the adapter to the nearAnnouncements
     */
    private void getLocationAndStartIntentService(){
        boolean result = Utility.checkPermission(getActivity());
        result = Utility.checkGPS(getActivity());
        if(result){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
               startIntentService(announcements);
            }

        }
    }

    private void setNearAnnouncements(){
        //create the announcements to be display on screen
        ArrayList<Announcement> nearAnnouncements = new ArrayList<>();
        int index = 0;
        for (Announcement ad : announcements){
            if (nearAds.size() > 0 && index < nearAds.size() && ad.getId() == nearAds.get(index))
                nearAnnouncements.add(ad);
            ++index;
        }

        setAdapterWithAnnouncements(nearAnnouncements);
    }
    private void setAdapterWithAnnouncements(ArrayList<Announcement> nearAnnouncements) {
        //set adapter with near ads
        AnnouncementAdapter adapter = new AnnouncementAdapter(getContext(),
                R.layout.announcement_adapter, nearAnnouncements);
        list_view.setAdapter(adapter);
    }

    protected void startIntentService(ArrayList<Announcement> ads) {
        Intent intent = new Intent(getActivity().getApplicationContext(), FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        //intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, ad.getAccommodation().getLocation());
        intent.putExtra(Constants.ANNOUNCEMENTS, ads);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_NAME);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        //intent.putExtra(RESULT_DATA_ID_ANNOUNCEMENT, ad.getId());
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

            //Receive ads by distance to user and idAd

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                distanceAddress = resultData.getParcelable(Constants.ANNOUNCEMENTS);
                if(distanceAddress != null && distanceAddress.size() > 0){
                    calculateDistances();
                    setNearAnnouncements();
                }
            }

        }
    }

    private void calculateDistances() {

        for(Map.Entry<Integer, Address> entry : distanceAddress.entrySet()){
            Integer idAd = entry.getKey();
            Address address = entry.getValue();
            Log.d("Address", address.getLatitude() + ", " + address.getLongitude());
            Location locAd = new Location("");
            locAd.setLatitude(address.getLatitude());
            locAd.setLongitude(address.getLongitude());

            Float distance = mLastLocation.distanceTo(locAd);
            if(distance < Constants.DISTANCE_FROM_AD)
                distanceIDAds.put(distance, idAd);
        }
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



