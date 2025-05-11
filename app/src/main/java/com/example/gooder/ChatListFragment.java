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

public class ChatListFragment extends Fragment {

    private RecyclerView rvChatlist;
    private ChatItemAdapter adapter;
    private List<ChatItem> chatItemList;
    private FirebaseFirestore db;
    private final String TAG = "ChatListFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ChatListFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        FirebaseApp.initializeApp(getContext());
        db = FirebaseFirestore.getInstance();
        String currentUserId = "j9HCU31IAaYNJv2wP5dKfWMQ3KD2";  // 用戶ID

        chatItemList = new ArrayList<>();

        // 監聽聊天室的更新
        db.collection("Chats")
                .addSnapshotListener((chatSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    chatItemList.clear();  // 清空列表，重新更新

                    AtomicInteger processedCount = new AtomicInteger(0);
                    int totalChats = chatSnapshots.size();

                    for (DocumentSnapshot chatDoc : chatSnapshots) {
                        String chater1 = chatDoc.getString("chater1_id");
                        String chater2 = chatDoc.getString("chater2_id");

                        if (currentUserId.equals(chater1) || currentUserId.equals(chater2)) {
                            String anotherChaterId = currentUserId.equals(chater1) ? chater2 : chater1;
                            String chatroomId = chatDoc.getId();

                            // 監聽每個聊天室內訊息的變動
                            db.collection("Chats")
                                    .document(chatroomId)
                                    .collection("chat")
                                    .orderBy("time", Query.Direction.DESCENDING)
                                    .addSnapshotListener((messageSnapshots, messageError) -> {
                                        if (messageError != null) {
                                            Log.w(TAG, "Error getting messages", messageError);
                                            return;
                                        }

                                        if (!messageSnapshots.isEmpty()) {
                                            DocumentSnapshot lastMessageDoc = messageSnapshots.getDocuments().get(0);

                                            String lastMsg = lastMessageDoc.getString("message");
                                            Timestamp time = lastMessageDoc.getTimestamp("time");


                                            int unreadCount = 0;

                                            // 計算未讀訊息數量
                                            for (DocumentSnapshot msgDoc : messageSnapshots) {
                                                Boolean isRead = msgDoc.getBoolean("isReaded");
                                                String senderId = msgDoc.getString("sender_id");

                                                if (senderId != null && senderId.equals(anotherChaterId)) {
                                                    if (isRead == null || !isRead) {
                                                        Log.d(TAG,"未讀訊息為: "+msgDoc.getString("message"));
                                                        unreadCount++;
                                                    }
                                                }
                                            }

                                            // 創建聊天項目
                                            ChatItem chatItem = new ChatItem(
                                                    chatroomId,
                                                    anotherChaterId,
                                                    anotherChaterId, // TODO: 改成對方的名字
                                                    lastMsg,
                                                    time,
                                                    R.drawable.setting, // TODO: 改成對方的頭像
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
                        } else {
                            if (processedCount.incrementAndGet() == totalChats) {
                                Collections.sort(chatItemList, (a, b) -> b.getTime().compareTo(a.getTime()));
                                Log.d(TAG, "更新聊天室列表");
                                adapter.updateList(chatItemList);
                            }
                        }
                    }
                });

        // 設定 RecyclerView
        rvChatlist = view.findViewById(R.id.rv_chatlist);
        rvChatlist.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatItemAdapter(chatItemList, chatItem -> {
            // 切換到 ChatRoomFragment（聊天室頁）
            ChatRoomFragment chatRoomFragment = ChatRoomFragment.newInstance(chatItem.getAnotherChaterId(), chatItem.getName());
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
