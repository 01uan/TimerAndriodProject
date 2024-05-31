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

/**
 * TaskAdapter is a RecyclerView.Adapter that binds a list of Task objects to the RecyclerView.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> tasks;
    private LayoutInflater mInflater;
    private OnItemClickListener mClickListener;

    /**
     * Constructor for the TaskAdapter.
     * @param context The context of the calling activity.
     * @param tasks The list of tasks to be displayed.
     */
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder to reflect the item at the given position.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        // Adding 1 to position to account for 0-based indexing
        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvTaskName.setText(task.getName());
        holder.tvChunks.setText(String.valueOf(task.getChunks()));

        // Set the background color based on the task's completion status
        if (task.isCompleted()) {
            holder.entireTask.setBackgroundColor(Color.GREEN);
        } else {
            holder.entireTask.setBackgroundColor(Color.parseColor("#D3CBCB"));
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * Returns the Task object at the specified position.
     * @param id The position of the task.
     * @return The Task object at the specified position.
     */
    public Task getItem(int id) {
        return tasks.get(id);
    }

    /**
     * Sets the click listener for the RecyclerView items.
     * @param itemClickListener The click listener to be set.
     */
    public void setClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * Interface to handle click events on RecyclerView items.
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * ViewHolder class that describes an item view and metadata about its place within the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNumber;
        TextView tvTaskName;
        TextView tvChunks;
        LinearLayout entireTask;

        /**
         * Constructor for the ViewHolder.
         * @param itemView The view of the individual list item.
         */
        ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvTaskName = itemView.findViewById(R.id.tvCurrentTask);
            tvChunks = itemView.findViewById(R.id.tvChunks);
            entireTask = itemView.findViewById(R.id.entireTask);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         * @param view The view that was clicked.
         */
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
