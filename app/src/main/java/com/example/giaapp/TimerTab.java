package com.example.giaapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * TimerTab class that handles the timer functionality within the application.
 * It provides methods for starting, stopping, and restarting timers, as well as managing breaks and notifications.
 */
public class TimerTab extends Fragment {

    private TextView etTime, tvCurrentTask, tvTotal;
    private Button btnStart, btnStop, btnRestart, btnShortBreak, btnLongBreak, btnTimer;
    private int currentBtn = R.id.btnTimer;
    private CountDownTimer timer;
    private TaskDBHelper db;
    private SharedView sharedView;
    private Task currentTask;
    private SharedPreferences sharedPreferences;

    int completed, completedChunks;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedView = new ViewModelProvider(requireActivity()).get(SharedView.class);
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTime = view.findViewById(R.id.etTime);
        tvCurrentTask = view.findViewById(R.id.tvCurrentTask);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnStart = view.findViewById(R.id.btnStart);
        btnStop = view.findViewById(R.id.btnStop);
        btnRestart = view.findViewById(R.id.btnRestart);
        btnTimer = view.findViewById(R.id.btnTimer);
        btnShortBreak = view.findViewById(R.id.btnShortBreak);
        btnLongBreak = view.findViewById(R.id.btnLongBreak);

        sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        etTime.setText(sharedPreferences.getString("TimerSetting", "25:00"));

        db = new TaskDBHelper(getActivity());
        db.open();

        // First loading up
        if (db.getAllTasks().getCount() >= 0) {
            updateScreen(db.getAllTasksList());
        }

        btnStart.setOnClickListener(this::startTimer);
        btnRestart.setOnClickListener(this::restartTimer);
        btnStop.setOnClickListener(this::stopTimer);
        btnTimer.setOnClickListener(this::clickTimer);
        btnShortBreak.setOnClickListener(this::clickShortBreak);
        btnLongBreak.setOnClickListener(this::clickLongBreak);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        sharedView.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                updateScreen(tasks);
            }
        });
    }

    /**
     * Updates the screen with the current tasks and status.
     * @param tasks The list of tasks to be displayed.
     */
    public void updateScreen(ArrayList<Task> tasks) {
        completed = 0;

        if (tasks.size() > 0) {
            for (Task task : tasks) {
                if (!task.isCompleted()) {
                    currentTask = task;
                    tvCurrentTask.setText("Currently Doing " + task.getName() + " with Chunks: " + completedChunks + "/" + task.getChunks());
                    break;
                } else {
                    completed++;
                }
                tvCurrentTask.setText("All Tasks Have Finished");
            }
            if (completed == tasks.size()) {
                tvTotal.setText("You have no Tasks at all");
            } else {
                tvTotal.setText(String.format("Tasks: %d/%d \nFinish In %s", completed, tasks.size(), calculateChunks()));
            }
        }

        if (tasks.size() == 0) {
            tvCurrentTask.setText("No Tasks Available");
            tvTotal.setText("You have no Tasks at all");
        }
    }

    /**
     * Starts the timer.
     * @param view The view that was clicked.
     */
    public void startTimer(View view) {
        long duration = 0;

        String timeInput = etTime.getText().toString();
        if (timeInput.matches("\\d+:\\d+")) {
            String[] timeParts = timeInput.split(":");
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);
            duration = (minutes * 60 + seconds) * 1000;
        } else if (timeInput.matches("\\d+")) {
            duration = Integer.parseInt(timeInput) * 1000;
        } else {
            Toast.makeText(getContext(), "Invalid time input", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creating CountDownTimer object to track time and count down
        timer = new CountDownTimer(duration, 1000) {
            // Called every second to update the time
            public void onTick(long millisUntilFinished) {
                long remainingSeconds = millisUntilFinished / 1000;
                long minutes = remainingSeconds / 60;
                etTime.setText(String.format("%02d:%02d", minutes, remainingSeconds % 60));
            }

            // Called when the timer is finished
            public void onFinish() {
                moveTimer(view);
                btnStop.setVisibility(View.GONE);
                makeNotification(getContext());

                // Update the task to completed
                // Chunk is only incremented on timer and not on break finish
                if (currentTask != null && (currentBtn == R.id.btnLongBreak || currentBtn == R.id.btnShortBreak)) {
                    completedChunks++;
                    if (completedChunks == currentTask.getChunks()) {
                        currentTask.setCompleted(true);
                        db.updateTask(currentTask);
                        sharedView.setTasks(db.getAllTasksList());
                        updateScreen(db.getAllTasksList());
                        completedChunks = 0;
                    }
                }
                updateScreen(db.getAllTasksList());
            }
        }.start();

        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
        etTime.setEnabled(false);
    }

    /**
     * Stops the timer.
     * @param view The view that was clicked.
     */
    public void stopTimer(View view) {
        timer.cancel();
        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
    }

    /**
     * Restarts the timer.
     * @param view The view that was clicked.
     */
    public void restartTimer(View view) {
        etTime.setEnabled(true);

        if (R.id.btnShortBreak == currentBtn) {
            clickShortBreak(view);
        } else if (R.id.btnLongBreak == currentBtn) {
            clickLongBreak(view);
        } else if (R.id.btnTimer == currentBtn) {
            clickTimer(view);
        }

        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
    }

    /**
     * Moves the timer to the next state (break or timer).
     * @param view The view that was clicked.
     */
    public void moveTimer(View view) {
        String autoBreak = sharedPreferences.getString("AutoBreak", "Short Break");
        etTime.setEnabled(true);
        if (R.id.btnTimer == currentBtn) {
            if (autoBreak.equals("Short Break")) {
                clickShortBreak(view);
            } else if (autoBreak.equals("Long Break")) {
                clickLongBreak(view);
            } else {
                restartTimer(view);
                return;
            }
        } else if (R.id.btnShortBreak == currentBtn) {
            clickTimer(view);
        } else if (R.id.btnLongBreak == currentBtn) {
            clickTimer(view);
        }
        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
    }

    /**
     * Sets the timer to the short break duration.
     * @param view The view that was clicked.
     */
    public void clickShortBreak(View view) {
        etTime.setText(sharedPreferences.getString("ShortBreak", "5:00"));
        currentBtn = R.id.btnShortBreak;
    }

    /**
     * Sets the timer to the long break duration.
     * @param view The view that was clicked.
     */
    public void clickLongBreak(View view) {
        etTime.setText(sharedPreferences.getString("LongBreak", "10:00"));
        currentBtn = R.id.btnLongBreak;
    }

    /**
     * Sets the timer to the default timer duration.
     * @param view The view that was clicked.
     */
    public void clickTimer(View view) {
        etTime.setText(sharedPreferences.getString("TimerSetting", "25:00"));
        currentBtn = R.id.btnTimer;
    }

    /**
     * Creates and displays a notification indicating that the timer has finished.
     * @param context The context of the application.
     */
    public void makeNotification(Context context) {
        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n;
        String message = "Your Time has finished";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("timer", "Timer", NotificationManager.IMPORTANCE_DEFAULT);
            notifyMgr.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "timer")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setChannelId(channel.getId())
                    .setContentTitle("Timer Has Finished")
                    .setContentText(message)
                    .setAutoCancel(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            n = builder.build();
        } else {
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Timer Has Finished")
                    .setContentText(message)
                    .setAutoCancel(false)
                    .setPriority(Notification.PRIORITY_DEFAULT);
            n = builder.build();
        }
        notifyMgr.notify(0, n);
    }

    /**
     * Calculates the remaining time for the tasks in chunks.
     * @return The remaining time as a formatted string.
     */
    public String calculateChunks() {
        int total = 0;
        int totalTime = 0;

        String hourFormat = sharedPreferences.getString("HourFormat", "Standard Time");
        int time = Integer.parseInt(sharedPreferences.getString("TimerSetting", "25:00").split(":")[0]);
        for (Task task : db.getAllTasksList()) {
            if (!task.isCompleted()) {
                total += task.getChunks();
            }
        }

        totalTime = (total - completedChunks) * time;
        if (hourFormat.equals("Standard Time")) {
            int hours = totalTime / 60;
            int minutes = totalTime % 60;
            return String.format("%d hours and %d minutes", hours, minutes);
        } else {
            return String.format("%d minutes", totalTime);
        }
    }
}
