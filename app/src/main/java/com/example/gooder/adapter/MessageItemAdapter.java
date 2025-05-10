package com.example.gooder.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gooder.R;
import com.example.gooder.model.MessageItem;

import java.util.List;

public class MessageItemAdapter extends RecyclerView.Adapter<MessageItemAdapter.ViewHolder> {

    private List<MessageItem> messageItemList;
    private String currentUserId;

    public MessageItemAdapter(List<MessageItem> messageItemList, String currentUserId) {
        this.messageItemList = messageItemList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageItemAdapter.ViewHolder holder, int position) {
        MessageItem messageItem = messageItemList.get(position);
        holder.tvMessage.setText(messageItem.getMessage());
        holder.tvMessageTime.setText(messageItem.getFormattedTime());

        // 判斷是否是本人發送，變更背景與對齊
        if (messageItem.getSenderId().equals(currentUserId)) {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_self);
            ((LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams()).gravity = Gravity.END;
            if(messageItem.isReaded()){
                holder.tvIsReaded.setText("已讀");
                holder.tvIsReaded.setVisibility(View.VISIBLE);
            }
            else {
                holder.tvIsReaded.setVisibility(View.GONE);
            }
        } else {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_other);
            ((LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams()).gravity = Gravity.START;
            holder.tvIsReaded.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvMessageTime, tvIsReaded;
        LinearLayout messageContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message_text);
            tvMessageTime = itemView.findViewById(R.id.tv_message_time);
            tvIsReaded = itemView.findViewById(R.id.tv_message_is_readed);
            messageContainer = itemView.findViewById(R.id.message_container);
        }
    }
}
