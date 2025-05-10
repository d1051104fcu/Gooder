package com.example.gooder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.model.ChatItem;

import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ViewHolder> {

    private List<ChatItem> chatItemList;
    private OnItemClickListener listener;

    public ChatItemAdapter(List<ChatItem> list){
        this.chatItemList = list;
    }

    public interface OnItemClickListener {
        void onItemClick(ChatItem item);
    }

    public ChatItemAdapter(List<ChatItem> list, OnItemClickListener listener) {
        this.chatItemList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chatlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem chatitem = chatItemList.get(position);

        holder.tvChatitemName.setText(chatitem.getName());
        holder.tvChatitemLastMessage.setText(chatitem.getLastMessage());
        holder.ivChatitemImage.setImageResource(chatitem.getAvatarResId());

        int unreadCount = chatitem.getUnreadCount();
        if (unreadCount > 0) {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(String.valueOf(unreadCount));
        } else {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(chatitem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatitemName, tvChatitemLastMessage, tvUnreadCount;
        ImageView ivChatitemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatitemName = itemView.findViewById(R.id.tv_chatitem_name);
            tvChatitemLastMessage = itemView.findViewById(R.id.tv_chatitem_last_message);
            ivChatitemImage = itemView.findViewById(R.id.iv_chatitem_image);
            tvUnreadCount = itemView.findViewById(R.id.tv_unread_count);
        }
    }
}
