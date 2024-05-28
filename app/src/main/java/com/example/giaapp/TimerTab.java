package com.example.giaapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class TimerTab extends Fragment {

    private TextView etTime, tvCurrentTask, tvTotal;
    private Button btnStart, btnStop, btnRestart;
    private CountDownTimer timer;
    private TaskDBHelper db;
    private SharedView sharedView;
    private Task currentTask;

    int time, shortBreak, longBreak, completed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedView = new ViewModelProvider(requireActivity()).get(SharedView.class);
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTime = view.findViewById(R.id.etTime);
        tvCurrentTask = view.findViewById(R.id.tvCurrentTask);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnStart = view.findViewById(R.id.btnStart);
        btnStop = view.findViewById(R.id.btnStop);
        btnRestart = view.findViewById(R.id.btnRestart);

        db = new TaskDBHelper(getActivity());
        db.open();

        //first loading up
        if (db.getAllTasks().getCount() >= 0) {
            updateScreen(db.getAllTasksList());
        }

        btnStart.setOnClickListener(this::startTimer);
        btnRestart.setOnClickListener(this::restartTimer);
        btnStop.setOnClickListener(this::stopTimer);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedView.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                updateScreen(tasks);
            }
        });
    }

    public void updateScreen(ArrayList<Task> tasks) {
        completed = 0;

        if (tasks.size() > 0) {
            for (Task task : tasks) {
                if (!task.isCompleted()) {
                    currentTask = task;
                    tvCurrentTask.setText("Currently Doing " + task.getName());
                    break;
                } else {
                    completed++;
                }
                tvCurrentTask.setText("All Tasks Have Finished");
            }
        }

        tvTotal.setText(String.format("Tasks: %d/%d", completed, tasks.size()));

        if (tasks.size() == 0) {
            tvCurrentTask.setText("No Tasks Available");
        }

    }

    /**
     * Starts the timer
     *
     * @param view
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


        //making countDownTimer object to track time and count down
        timer = new CountDownTimer(duration, 1000) {
            //onTick method is called every second to update the time
            public void onTick(long millisUntilFinished) {
                long remainingSeconds = millisUntilFinished / 1000;
                long minutes = remainingSeconds / 60;
                etTime.setText(String.format("%02d:%02d", minutes, remainingSeconds % 60));
            }

            //onFinish method is called when the timer is finished
            public void onFinish() {
                restartTimer(view);
                btnStop.setVisibility(View.GONE);

                //update the task to completed
                if (currentTask != null) {
                    currentTask.setCompleted(true);
                    db.updateTask(currentTask);
                    sharedView.setTasks(db.getAllTasksList());
                    updateScreen(db.getAllTasksList());
                }
            }

        }.start();
        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
        etTime.setEnabled(false);
    }

    /**
     * Stops the timer
     *
     * @param view
     */
    public void stopTimer(View view) {
        timer.cancel();
        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
    }

    /**
     * Restarts the timer
     *
     * @param view
     */
    public void restartTimer(View view) {
        //TODO: get time from settings page
        etTime.setEnabled(true);
        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
    }


}