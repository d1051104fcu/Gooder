package com.example.gooder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gooder.adapter.MessageItemAdapter;
import com.example.gooder.model.MessageItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatRoomFragment extends Fragment {

    private TextView tvChatroomTitle;
    private RecyclerView rvChatroomMessages;
    private EditText etChatroomMessage;
    private Button btnChatroomSend;

    private static final String ARG_CHAT_NAME = "chat_name";
    private String chatName;
    private List<MessageItem> messageItemList;
    private MessageItemAdapter adapter;

    public static ChatRoomFragment newInstance(String chatName) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHAT_NAME, chatName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatName = getArguments().getString(ARG_CHAT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        if (nav != null) {
            nav.setVisibility(View.GONE);
        }

        tvChatroomTitle = view.findViewById(R.id.tv_chatroom_title);
        rvChatroomMessages = view.findViewById(R.id.rv_chatroom_messages);
        etChatroomMessage = view.findViewById(R.id.et_chatroom_message);
        btnChatroomSend = view.findViewById(R.id.btn_chatroom_send);

        tvChatroomTitle.setText("與 " + chatName + " 聊天");

        //這裡要從firebase取資料

        messageItemList = new ArrayList<>();
        messageItemList.add(new MessageItem("測試訊息1", "sender_id1", Timestamp.now(), true));
        messageItemList.add(new MessageItem("測試訊息2", "sender_id2", Timestamp.now(), true));
        messageItemList.add(new MessageItem("測試訊息3", "sender_id1", Timestamp.now(), false));

        rvChatroomMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessageItemAdapter(messageItemList, "sender_id1");
        rvChatroomMessages.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        if (nav != null) {
            nav.setVisibility(View.VISIBLE);
        }
    }
}