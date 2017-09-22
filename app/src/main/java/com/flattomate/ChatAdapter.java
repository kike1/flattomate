package com.flattomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flattomate.Model.Chat;
import com.flattomate.Model.Image;
import com.flattomate.Model.Review;
import com.flattomate.Model.User;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adapter that load a list of announcements and put dinamycally on views
 */

public class ChatAdapter extends ArrayAdapter<Chat>{

    private Context _context;
    private ArrayList<Chat> _chats;
    private ArrayList<Review> _reviews;
    int _resource;

    FlattomateService api;
    SharedPreferences manager;
    Transformation transformation;


    public ChatAdapter(Context context, int resource, ArrayList<Chat> chats){
        super(context, resource, chats);
        _context = context;
        _chats = chats;
        //_resource = resource;
        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");
        manager = _context.getSharedPreferences("settings", Context.MODE_PRIVATE);

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
        //int idUser = _announcements.get(position).getIdUser();

        //Toast.makeText(_context, "ad " + idAnnouncement, Toast.LENGTH_SHORT).show();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_adapter, null, true);
            int idAnnouncement = 0;

            holder = new ViewHolder();

            holder.chat_adapter_layout = (RelativeLayout) convertView.findViewById(R.id.chat_adapter_layout);
            holder.chat_adapter_image = (ImageView) convertView.findViewById(R.id.chat_adapter_image);
            holder.txt_adapter_user = (TextView) convertView.findViewById(R.id.txt_adapter_user);
            holder.txt_adapter_message = (TextView) convertView.findViewById(R.id.txt_adapter_message);
            holder.txt_adapter_hour = (TextView) convertView.findViewById(R.id.txt_adapter_hour);
            //holder.txt_adapter_new_notifications = (TextView) convertView.findViewById(R.id.txt_adapter_new_notifications);
            holder.txt_no_chats = (TextView) convertView.findViewById(R.id.txt_no_chats);

            idAnnouncement = _chats.get(position).getIdAnnouncement();

            //get main image from announcement and put in the ImageView
            Call<Image> imageCall = api.getAnnouncementMainImage(idAnnouncement);
            imageCall.enqueue(new Callback<Image>() {
                @Override
                public void onResponse(Call<Image> call, Response<Image> response) {
                    Log.e("RS chat", String.valueOf(response.code()));

                    if (response.code() == 200) {
                        if (response.body() != null) {
                            Picasso.with(_context)
                                    .load(restAPI.API_BASE_URL + "announcements/" + response.body().getName())
                                    .transform(transformation)
                                    .into(holder.chat_adapter_image);
                        } else
                            Log.e("ERROR", "No images for ad ");
                    } else
                        Log.e("ERROR", "Image can not be retrieved");
                }

                @Override
                public void onFailure(Call<Image> call, Throwable t) {
                    call.cancel();
                    Log.e("ERROR", "PETE " + t.getMessage());
                }
            });

            //set user name
            Call<User> userRQ = api.getUser(_chats.get(position).getId());
            userRQ.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.e("RS chat", String.valueOf(response.code()));

                    if (response.code() == 200) {
                        if (response.body() != null) {
                            holder.txt_adapter_user.setText(Html.fromHtml("<b>"+response.body().getName()+"</b>") );
                        }
                    } else
                        Log.e("ERROR", "Image can not be retrieved");
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    call.cancel();
                }
            });

            //set last msg
            holder.txt_adapter_message.setText(_chats.get(position).getMessage());

            //parse hh:mm from msg

            String msgTime = _chats.get(position).getCreatedAt();
            if(msgTime != null){
                msgTime = msgTime.substring(msgTime.length()-8,msgTime.length()-3);
                holder.txt_adapter_hour.setText(msgTime);
            }


            //holder.txt_adapter_new_notifications

            //user wanna see the chat with user_wrote
            holder.chat_adapter_layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent activityChat = new Intent(_context, ChatActivity.class);
                    activityChat.putExtra("idAnnouncement", _chats.get(position).getIdAnnouncement());
                    activityChat.putExtra("idUserR", _chats.get(position).getIdUserReceive());
                    activityChat.putExtra("idUserW", _chats.get(position).getIdUserWrote());
                    _context.startActivity(activityChat);
                }
            });

            convertView.setTag(holder);
        }else
                holder = (ViewHolder) convertView.getTag();

        return convertView;

    }

    private class ViewHolder {
        RelativeLayout chat_adapter_layout;
        TextView txt_no_chats;
        ImageView chat_adapter_image;
        TextView txt_adapter_user;
        TextView txt_adapter_message;
        TextView txt_adapter_hour;
        //TextView txt_adapter_new_notifications;
    }
}
