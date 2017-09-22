package com.flattomate;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.flattomate.Model.Chat;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    @Bind(R.id.list_view_chats)
    ListView list_view;
    @Bind(R.id.txt_chat)
    TextView tvMessage;
    @Bind(R.id.btn_send)
    Button btn_send;

    Integer myId, idOtherUser, idAnnouncement;
    FlattomateService api;
    Transformation transformation;
    SharedPreferences manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.chats_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Chat");
        }

        manager = this.getSharedPreferences("settings", Context.MODE_PRIVATE);

        myId = manager.getInt("id",0);
        idOtherUser = getIntent().getExtras().getInt("idUserR");
        if(idOtherUser == myId)
            idOtherUser = getIntent().getExtras().getInt("idUserW");

        idAnnouncement = getIntent().getExtras().getInt("idAnnouncement");

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(60)
                .oval(false)
                .build();

        if(myId != 0){
            getChats();
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ""+tvMessage.getText();
                Chat chat = new Chat(idOtherUser,myId,idAnnouncement,message);

                Call<ResponseBody> sendChatMessageRQ = api.sendChatMessage(chat);
                sendChatMessageRQ.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200) {
                            //refresh messages including the new one
                            getChats();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {call.cancel();}
                });
            }
        });

    }

    private void getChats() {
        Call<ArrayList<Chat>> chatsRQ = api.chatsFromAnnouncement(myId, idOtherUser, idAnnouncement);
        chatsRQ.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                if(response.code() == 200) {
                    setAdapterWithChats(response.body());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {call.cancel();}
        });
    }

    private void setAdapterWithChats(ArrayList<Chat> chats) {
        //set adapter with near chats
        ListChatAdapter adapter = new ListChatAdapter(this,
                R.layout.list_chat_adapter, chats);
        list_view.setAdapter(adapter);

    }
}
