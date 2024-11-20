package com.example.antitheftandroidapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antitheftandroidapp.Model.LogItem;
import com.example.antitheftandroidapp.R;

import java.util.List;
public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    private List<LogItem> logList;

    public LogAdapter(List<LogItem> logList) {
        this.logList = logList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item_layout, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogItem logItem = logList.get(position);
        holder.bind(logItem);
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public void updateData(List<LogItem> newLogList) {
        logList.clear();
        logList.addAll(newLogList);
        notifyDataSetChanged();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        private TextView timestampTextView;
        private TextView levelTextView;
        private TextView tagTextView;
        private TextView messageTextView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            levelTextView = itemView.findViewById(R.id.levelTextView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(LogItem logItem) {
            timestampTextView.setText(logItem.getTimestamp());
            levelTextView.setText(logItem.getLevel());
            tagTextView.setText(logItem.getTag());
            messageTextView.setText(logItem.getMessage());
        }
    }
}
