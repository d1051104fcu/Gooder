package com.example.gooder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gooder.adapter.ChatitemAdapter;
import com.example.gooder.model.Chatitem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {

    private RecyclerView rvChatlist;
    private ChatitemAdapter adapter;
    private List<Chatitem> chatitemList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatListFragment newInstance(String param1, String param2) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        //這裡取得firebase資料


        chatitemList = new ArrayList<>();
        chatitemList.add(new Chatitem("chatId", "Tom", "hello", "time", R.drawable.setting, 999));
        chatitemList.add(new Chatitem("chatId", "Jay", "hi", "time", R.drawable.setting, 99));
        chatitemList.add(new Chatitem("chatId", "Zoe", "helo", "time", R.drawable.setting, 9));
        chatitemList.add(new Chatitem("chatId", "May", "A", "time", R.drawable.setting, 0));

        rvChatlist = view.findViewById(R.id.rv_chatlist);
        rvChatlist.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatitemAdapter(chatitemList, chatitem -> {
            // 切換到 ChatRoomFragment（聊天室頁）
            ChatRoomFragment chatRoomFragment = ChatRoomFragment.newInstance(chatitem.getName());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_main, chatRoomFragment)
                    .addToBackStack(null)
                    .commit();
        });

        rvChatlist.setAdapter(adapter);

        return view;
    }
}