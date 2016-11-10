package com.flattomate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> service_name;
    private final ArrayList<Integer> Imageid;
    View.OnClickListener listener;
   // static int numOfView = 1;
    //static ArrayList<Boolean> servicesPressed;

    public CustomGrid(Context c, ArrayList<String> service_name,
                      ArrayList<Integer> Imageid) {
        mContext = c;
        this.Imageid = Imageid;
        this.service_name = service_name;
    }

    public CustomGrid(Context c, ArrayList<String> service_name,
                      ArrayList<Integer> Imageid, View.OnClickListener listener) {
        this.listener = listener;
        mContext = c;
        this.Imageid = Imageid;
        this.service_name = service_name;
    }

    @Override
    public int getCount() {
        return service_name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(service_name.get(position));
            imageView.setImageResource(getIdResource(Imageid.get(position)));
        } else {
            grid = (View) convertView;
        }

       /* final int pos = position;
        //if listener is setted then add listener for colour selected
        if(listener != null){
            grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Service ON/OFF
                    if(servicesPressed.get(pos).booleanValue()) {
                        servicesPressed.set(pos, false);
                        v.setBackgroundColor(Color.GRAY);
                    }else {
                        servicesPressed.set(pos, true);
                        v.setBackgroundColor(Color.WHITE);
                    }
                }
            });
        }*/


        return grid;
    }

    int getIdResource(int serviceId){
        Log.d("SERVICE ID", String.valueOf(serviceId));
        switch(serviceId){
            case 1: return R.drawable.service_1;
            case 2: return R.drawable.service_2;
            case 3: return R.drawable.service_3;
            case 4: return R.drawable.service_4;
            case 5: return R.drawable.service_5;
            case 6: return R.drawable.service_6;
            case 7: return R.drawable.service_7;

            default: return R.drawable.service_7;
        }
    }
}