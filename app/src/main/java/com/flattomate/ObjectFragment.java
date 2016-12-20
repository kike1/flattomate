package com.flattomate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Instances of this class are fragments representing a single
// object in our collection.
public class ObjectFragment extends Fragment {
    public static final String ARG_SECTION = "section";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        View rootView = null;

        if (args.getInt(ARG_SECTION) == 0) {
            rootView = inflater.inflate(
                    R.layout.tab_announcement_list, container, false);
        }else if (args.getInt(ARG_SECTION) == 1) {
            rootView = inflater.inflate(
                    R.layout.tab_announcement_list, container, false);
        }else if (args.getInt(ARG_SECTION) == 2) {
            rootView = inflater.inflate(
                    R.layout.activity_profile, container, false);
        }

        //((TextView) rootView.findViewById(android.R.id.text1)).setText(
        //   Integer.toString(args.getInt(ARG_SECTION)));
        return rootView;
    }
}
