package com.example.gooder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatRoomFragment extends Fragment {

    private TextView tvChatroomTitle;

    private static final String ARG_CHAT_NAME = "chat_name";
    private String chatName;

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
        tvChatroomTitle.setText("與 " + chatName + " 聊天");

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