package com.example.gooder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message msg = messageList.get(position);
        holder.tvMessage.setText(msg.getMessage());
        holder.tvTime.setText(msg.getTime());

        // 你可以在這裡改變訊息樣式或位置，例如根據 sender_id 判斷靠左靠右
        if (msg.getSender_id().equals(currentUserId)) {
            // 自己發的
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_self);
        } else {
            // 對方發的
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_other);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message_text);
            tvTime = itemView.findViewById(R.id.tv_message_time);
        }
    }
}
