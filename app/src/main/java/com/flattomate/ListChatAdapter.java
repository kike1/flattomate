package com.flattomate;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flattomate.Model.Chat;
import com.flattomate.Model.Review;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Adapter that load a list of chats and put dinamycally on views
 */

public class ListChatAdapter extends ArrayAdapter<Chat>{

    private Context _context;
    private ArrayList<Chat> _chats;
    private ArrayList<Review> _reviews;
    int _resource;

    Integer idUserR, idUserW, idAnnouncement;
    FlattomateService api;
    SharedPreferences manager;
    Transformation transformation;


    public ListChatAdapter(Context context, int resource, ArrayList<Chat> chats){
        super(context, resource, chats);
        _context = context;
        _chats = chats;
        //_resource = resource;
        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        manager = _context.getSharedPreferences("settings", Context.MODE_PRIVATE);

        idUserR = chats.get(0).getIdUserReceive();
        idUserW = chats.get(0).getIdUserWrote();
        idAnnouncement = chats.get(0).getIdAnnouncement();

        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(60)
                .oval(false)
                .build();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_chat_adapter, null, true);

            holder = new ViewHolder();

            holder.list_chat_adapter_layout = (LinearLayout) convertView.findViewById(R.id.list_chat_adapter_layout);
            holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.message_time = (TextView) convertView.findViewById(R.id.message_time);

            //set user image
            Picasso.with(_context)
                    .load(restAPI.API_BASE_URL+"imgs/"+_chats.get(position).getIdUserWrote()+".jpg")
                    .placeholder(R.drawable.user_default)
                    .into(holder.user_image);

            //set the message from user
            holder.message.setText(_chats.get(position).getMessage());

            //set message time
            String message_time = _chats.get(position).getCreatedAt();
            if(message_time != null){
                message_time = message_time.substring(message_time.length()-8,message_time.length()-3);
                holder.message_time.setText(message_time);
            }

            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        return convertView;

    }

    private class ViewHolder {
        LinearLayout list_chat_adapter_layout;
        ImageView user_image;
        TextView message;
        TextView message_time;
    }
}
