package com.example.giaapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> tasks;
    private LayoutInflater mInflator;
    private onItemClickListener mClickListener;


    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.mInflator = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        //1 is to account from starting at 0
        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvTaskName.setText(task.getName());
        holder.tvChunks.setText(String.valueOf(task.getChunks()));

        if (task.isCompleted()) {
            holder.entireTask.setBackgroundColor(Color.GREEN);
        } else {
            holder.entireTask.setBackgroundColor(Color.parseColor("#D3CBCB"));
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getItem(int id) {
        return tasks.get(id);
    }

    public void setClickListener(onItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNumber;
        TextView tvTaskName;
        TextView tvChunks;
        LinearLayout entireTask;

        ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvTaskName = itemView.findViewById(R.id.tvCurrentTask);
            tvChunks = itemView.findViewById(R.id.tvChunks);
            entireTask = itemView.findViewById(R.id.entireTask);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }
}
