package com.flattomate;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.flattomate.Model.Announcement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver resultReceiver;
    private static final String TAG = "IntentService";

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        TreeMap<Integer, Address> announcementsAddress = new TreeMap<>();
        String errorMessage = "";

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        //ArrayList<Announcement> announcements = intent.getParcelableExtra(Constants.ANNOUNCEMENTS);
        int fetchType = intent.getIntExtra(Constants.FETCH_TYPE_EXTRA, 0);
        if(fetchType == Constants.USE_ADDRESS_NAME) {
            ArrayList<Announcement> ads = intent.getParcelableArrayListExtra(Constants.ANNOUNCEMENTS);
            if (ads != null && ads.size() > 0)
                for (Announcement ad : ads) {
                    try {
                        //return addresses from announcements for calculate distances between ads and user
                        if (ad.getAccommodation() != null &&
                                ad.getAccommodation().getLocation() != null &&
                                !ad.getAccommodation().getLocation().equals("")) {

                            addresses = geocoder.getFromLocationName(ad.getAccommodation().getLocation(), 1);
                            announcementsAddress.put(ad.getId(), addresses.get(0));
                        }
                    } catch (IOException e) {
                        errorMessage = "Service not available";
                        Log.e(TAG, errorMessage, e);
                    }
                    // String name = intent.getStringExtra(Constants.LOCATION_NAME_DATA_EXTRA);

                }
        }else if(fetchType == Constants.USE_ADDRESS_LOCATION) {
            Location location = intent.getParcelableExtra(
                    Constants.LOCATION_DATA_EXTRA);

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException ioException) {
                errorMessage = "Service Not Available";
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() + ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }
        }
        else {
            errorMessage = "Unknown Type";
        }

        if (announcementsAddress == null || announcementsAddress.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Not Found";
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null);
        } else {
            for(Address address : addresses) {
                String outputAddress = "";
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    outputAddress += " --- " + address.getAddressLine(i);
                }
            }
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments), announcementsAddress);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message, TreeMap<Integer, Address> ads) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ANNOUNCEMENTS, (Parcelable) ads);
        bundle.putString(Constants.RESULT_DATA_DISTANCE, message);
        resultReceiver.send(resultCode, bundle);
    }

}