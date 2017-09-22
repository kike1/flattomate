package com.flattomate.Tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.flattomate.ChatAdapter;
import com.flattomate.Model.Chat;
import com.flattomate.Model.User;
import com.flattomate.R;
import com.flattomate.REST.FlattomateService;
import com.flattomate.REST.restAPI;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that represent a tab in dashboard that have all chats
 */

public class TabChats extends Fragment {

    User user;
    FlattomateService api;
    int idUser;
    public static SharedPreferences _manager;
    private static final String ID_USER = "id";
    ArrayList<Chat> chats = new ArrayList<>();
    ListView list_view;
    TextView txt_no_chats;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = restAPI.createService(FlattomateService.class, "user", "secretpassword");

        _manager = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        idUser = _manager.getInt(ID_USER,0);

        Log.e("","CHATFragment.onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.tab_chat, container, false);
        list_view = (ListView) rootView.findViewById(R.id.chat_list_view);
        ButterKnife.bind(this, rootView);

        txt_no_chats = (TextView)rootView.findViewById(R.id.txt_no_chats);
        setHasOptionsMenu(true);

        populateFragment();

        return rootView;
    }

    private void populateFragment() {

        //obtain last chats that user has with other users
        Call<ArrayList<Chat>> rqChats = api.lastChatsFromUser(idUser);
        rqChats.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(final Call<ArrayList<Chat>> call,
                                   final Response<ArrayList<Chat>> response) {

                if (response.code() == 200){
                    chats = response.body();
                    deleteOlderDuplicate(chats);
                    setAdapterWithAnnouncements(chats);
                }else {
                    txt_no_chats.setText(R.string.lbl_no_chats);
                    txt_no_chats.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(final Call<ArrayList<Chat>> call, final Throwable t) {
                call.cancel();
            }
        });

    }

    //recreate chats deleting older duplicated items
    private void deleteOlderDuplicate(ArrayList<Chat> chats) {

        ArrayList<Chat> chatsWithoutDuplicates = new ArrayList<>();

        for(Chat chat: chats){
                if (chatsWithoutDuplicates.size() == 0)
                    chatsWithoutDuplicates.add(chat);
                else{
                    Boolean isDuplicate = false;
                    for(Chat chatW: chatsWithoutDuplicates) {
                        if (chat.getIdUserWrote() == chatW.getIdUserWrote() &&
                                chat.getIdUserReceive() == chatW.getIdUserReceive() &&
                                chat.getIdAnnouncement() == chatW.getIdAnnouncement())
                            isDuplicate = true;
                    }
                    if(!isDuplicate)
                        chatsWithoutDuplicates.add(chat);
                }
        }

        chats.clear();
        chats.addAll(chatsWithoutDuplicates);
    }

    private void setAdapterWithAnnouncements(ArrayList<Chat> chats) {
        //set adapter with chats
        ChatAdapter adapter = new ChatAdapter(getContext(),
                R.layout.chat_adapter, chats);
        list_view.setAdapter(adapter);
    }
}
