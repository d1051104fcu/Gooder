package com.example.gooder;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import com.example.gooder.adapter.MessageItemAdapter;
import com.example.gooder.model.MessageItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomFragment extends Fragment {

    private TextView tvChatroomTitle;
    private RecyclerView rvChatroomMessages;
    private EditText etChatroomMessage;
    private Button btnChatroomSend;

    private static final String ARG_CHATER_ID = "chater_id";
    private static final String ARG_CHAT_NAME = "chat_name";
    private String chaterId;
    private String chatName;
    private String currentUserId;
    private String chatroomId;

    private List<MessageItem> messageItemList;
    private MessageItemAdapter adapter;

    private FirebaseFirestore db;
    private ListenerRegistration messageListener;

    private final String TAG = "ChatRoomFragment";

    public static ChatRoomFragment newInstance(String chaterId, String chatName) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHATER_ID, chaterId);
        args.putString(ARG_CHAT_NAME, chatName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chaterId = getArguments().getString(ARG_CHATER_ID);
            chatName = getArguments().getString(ARG_CHAT_NAME);
        }

        db = FirebaseFirestore.getInstance();
        //currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserId = "j9HCU31IAaYNJv2wP5dKfWMQ3KD2"; // 假資料測試用
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        if (nav != null) nav.setVisibility(View.GONE);

        tvChatroomTitle = view.findViewById(R.id.tv_chatroom_title);
        rvChatroomMessages = view.findViewById(R.id.rv_chatroom_messages);
        etChatroomMessage = view.findViewById(R.id.et_chatroom_message);
        btnChatroomSend = view.findViewById(R.id.btn_chatroom_send);

        tvChatroomTitle.setText("與 " + chatName + " 聊天");

        messageItemList = new ArrayList<>();
        adapter = new MessageItemAdapter(messageItemList, currentUserId);
        rvChatroomMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChatroomMessages.setAdapter(adapter);

        // Step 1: 找聊天室 ID
        db.collection("Chats")
                .whereEqualTo("chater1_id", currentUserId)
                .whereEqualTo("chater2_id", chaterId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        chatroomId = snapshot.getDocuments().get(0).getId();
                        listenForMessages();
                    } else {
                        // 若順序相反再找一次
                        db.collection("Chats")
                                .whereEqualTo("chater1_id", chaterId)
                                .whereEqualTo("chater2_id", currentUserId)
                                .get()
                                .addOnSuccessListener(snapshot2 -> {
                                    if (!snapshot2.isEmpty()) {
                                        chatroomId = snapshot2.getDocuments().get(0).getId();
                                        listenForMessages();
                                    } else {
                                        Log.w(TAG, "聊天室不存在");
                                    }
                                });
                    }
                });

        btnChatroomSend.setOnClickListener(v -> {
            String msg = etChatroomMessage.getText().toString().trim();
            if (!msg.isEmpty() && chatroomId != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("isReaded", false);
                data.put("message", msg);
                data.put("sender_id", currentUserId);
                data.put("time", Timestamp.now());

                db.collection("Chats")
                        .document(chatroomId)
                        .collection("chat")
                        .add(data)
                        .addOnSuccessListener(doc -> etChatroomMessage.setText(""));
            }
        });

        return view;
    }

    private void listenForMessages() {
        messageListener = db.collection("Chats")
                .document(chatroomId)
                .collection("chat")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    messageItemList.clear();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Boolean isRead = doc.getBoolean("isReaded");
                        String msg = doc.getString("message");
                        String senderId = doc.getString("sender_id");
                        Timestamp time = doc.getTimestamp("time");

                        messageItemList.add(new MessageItem(isRead != null ? isRead : false, msg, senderId, time));

                        // 自動標記為已讀
                        if (chaterId.equals(senderId)) {
                            doc.getReference().update("isReaded", true);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    rvChatroomMessages.scrollToPosition(messageItemList.size() - 1);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BottomNavigationView nav = getActivity().findViewById(R.id.bottom_nav);
        if (nav != null) nav.setVisibility(View.VISIBLE);

        if (messageListener != null) {
            messageListener.remove();
        }
    }
}
