package com.example.gooder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gooder.adapter.ChatItemAdapter;
import com.example.gooder.model.ChatItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {

    private RecyclerView rvChatlist;
    private ChatItemAdapter adapter;
    private List<ChatItem> chatItemList;
    private FirebaseFirestore db;

    private final String TAG = "ChatListFragment";

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
        FirebaseApp.initializeApp(getContext());
        db = FirebaseFirestore.getInstance();
        //String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //TODO 之後要改成真正的使用者
        String currentUserId = "j9HCU31IAaYNJv2wP5dKfWMQ3KD2";

        chatItemList = new ArrayList<>();

        // 取得所有聊天對話（chats 集合）
        db.collection("Chats")
                .get()
                .addOnSuccessListener(chatSnapshots -> {
                    Log.d(TAG, "找到 " + chatSnapshots.size() + " 個聊天室.");

                    AtomicInteger processedCount = new AtomicInteger(0);
                    int totalChats = chatSnapshots.size();

                    for (DocumentSnapshot chatDoc : chatSnapshots) {
                        String chater1 = chatDoc.getString("chater1_id");
                        String chater2 = chatDoc.getString("chater2_id");

                        // 判斷目前使用者是否為此聊天室的其中一人
                        if (currentUserId.equals(chater1) || currentUserId.equals(chater2)) {
                            String anotherChaterId = currentUserId.equals(chater1) ? chater2 : chater1;
                            String chatroomId = chatDoc.getId();

                            // 讀取聊天室內所有訊息（按時間遞減排序）
                            db.collection("Chats")
                                    .document(chatroomId)
                                    .collection("chat")
                                    .orderBy("time", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnSuccessListener(messageSnapshots -> {

                                        if (!messageSnapshots.isEmpty()) {
                                            // 取得最新一則訊息（排在最前面）
                                            DocumentSnapshot lastMessageDoc = messageSnapshots.getDocuments().get(0);

                                            String lastMsg = lastMessageDoc.getString("message");
                                            Timestamp time = lastMessageDoc.getTimestamp("time");
                                            String senderId = lastMessageDoc.getString("sender_id");

                                            // 計算未讀訊息數量
                                            int unreadCount = 0;

                                            for (DocumentSnapshot msgDoc : messageSnapshots) {
                                                Boolean isRead = msgDoc.getBoolean("isReaded");
                                                Log.d(TAG, "是否已讀: "+isRead);

                                                // 確保senderId != null，並且不是當前用戶，且訊息未讀
                                                if (senderId != null && !senderId.equals(currentUserId)) {
                                                    // 如果isRead為null，視為未讀，或者isRead為false，也視為未讀
                                                    if (isRead == null || !isRead) {
                                                        unreadCount++;
                                                    }
                                                }
                                            }


                                            Log.d(TAG, "未讀訊息數: "+unreadCount);
                                            ChatItem chatItem = new ChatItem(
                                                    chatroomId,
                                                    anotherChaterId, //TODO 改成對方的名字
                                                    lastMsg,
                                                    time,
                                                    R.drawable.setting, //TODO 改成對方的頭像
                                                    unreadCount
                                            );

                                            chatItemList.add(chatItem);

                                        }

                                        // 檢查是否所有聊天室都處理完
                                        if (processedCount.incrementAndGet() == totalChats) {
                                            Collections.sort(chatItemList, (a, b) -> b.getTime().compareTo(a.getTime()));
                                            Log.d(TAG, "更新聊天室列表");
                                            adapter.updateList(chatItemList);
                                        }
                                    });
                        }
                        else {
                            if (processedCount.incrementAndGet() == totalChats) {
                                Collections.sort(chatItemList, (a, b) -> b.getTime().compareTo(a.getTime()));
                                Log.d(TAG, "更新聊天室列表");
                                adapter.updateList(chatItemList);
                            }
                        }
                    }
                });


        rvChatlist = view.findViewById(R.id.rv_chatlist);
        rvChatlist.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatItemAdapter(chatItemList, chatItem -> {
            // 切換到 ChatRoomFragment（聊天室頁）
            ChatRoomFragment chatRoomFragment = ChatRoomFragment.newInstance(chatItem.getName());
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