package com.example.giaapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class TimerTab extends Fragment {

    TextView etTime, tvCurrentTask, tvTotal;
    Button btnStart, btnStop, btnRestart;
    CountDownTimer timer;

    TaskDBHelper db;

    int time, shortBreak, longBreak;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        tvTotal.setText(String.valueOf(db.getAllTasks().getCount()));
        if (db.getAllTasks().getCount() > 0) {
            tvCurrentTask.setText("Currently Doing " + db.getAllTasks().getString(1));
        }

        btnStart.setOnClickListener(this::startTimer);
        btnRestart.setOnClickListener(this::restartTimer);
        btnStop.setOnClickListener(this::stopTimer);
    }

    /**
     * Starts the timer
     * @param view
     */
    public void startTimer(View view) {
        btnStart.setVisibility(View.GONE);
        //making countDownTimer object to track time and count down
        timer = new CountDownTimer(Integer.parseInt(etTime.getText().toString()) * TimeUnit.SECONDS.toMillis(1), 6) {
            //onTick method is called every second to update the time
            public void onTick(long millisUntilFinished) {
                etTime.setText(String.valueOf(millisUntilFinished/ TimeUnit.SECONDS.toMillis(1)));
            }

            //onFinish method is called when the timer is finished
            public void onFinish() {
                restartTimer(view);
                btnStop.setVisibility(View.GONE);
            }

        }.start();
        btnStop.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
        etTime.setEnabled(false);
    }

    /**
     * Stops the timer
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
     * @param view
     */
    public void restartTimer(View view) {
        //TODO: get time from settings page
        etTime.setEnabled(true);
        btnStart.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.GONE);
    }




}