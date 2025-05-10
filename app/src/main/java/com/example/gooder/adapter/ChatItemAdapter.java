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
        ChatItem chatItem = chatItemList.get(position);

        holder.tvChatitemName.setText(chatItem.getName());
        holder.tvChatitemLastMessage.setText(chatItem.getLastMessage());
        holder.ivChatitemImage.setImageResource(chatItem.getAvatarResId());

        int unreadCount = chatItem.getUnreadCount();
        if (unreadCount > 0) {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            String unreadCountStr;
            if(unreadCount < 99){
                unreadCountStr = String.valueOf(unreadCount);
            }
            else {
                unreadCountStr = "99";
            }
            holder.tvUnreadCount.setText(unreadCountStr);
        }
        else {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(chatItem);
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

    public void updateList(List<ChatItem> newList) {
        chatItemList = newList;
        notifyDataSetChanged();
    }

}
