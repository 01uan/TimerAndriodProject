package com.example.giaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

public class SettingsTab extends Fragment {

    private Spinner listBackground, listHourFormat, listAutoBreak;
    private EditText etTimerSetting, etShortBreak, etLongBreak;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listBackground = view.findViewById(R.id.listBackground);
        listHourFormat = view.findViewById(R.id.listHourFormat);
        listAutoBreak = view.findViewById(R.id.listAutoBreak);
        etTimerSetting = view.findViewById(R.id.etTimerSetting);
        etShortBreak = view.findViewById(R.id.etShortBreak);
        etLongBreak = view.findViewById(R.id.etLongBreak);

        setupSpinners(view);
        setupEditTexts();

        sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        loadSettings();
    }

    public void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Background", listBackground.getSelectedItem().toString());
        editor.putString("HourFormat", listHourFormat.getSelectedItem().toString());
        editor.putString("AutoBreak", listAutoBreak.getSelectedItem().toString());
        editor.putString("TimerSetting", etTimerSetting.getText().toString());
        editor.putString("ShortBreak", etShortBreak.getText().toString());
        editor.putString("LongBreak", etLongBreak.getText().toString());
        editor.apply();
    }

    public void loadSettings() {
        listBackground.setSelection(getIndex(listBackground, sharedPreferences.getString("Background", "Default")));
        listHourFormat.setSelection(getIndex(listHourFormat, sharedPreferences.getString("HourFormat", "24 Hours")));
        listAutoBreak.setSelection(getIndex(listAutoBreak, sharedPreferences.getString("AutoBreak", "Enable")));
        etTimerSetting.setText(sharedPreferences.getString("TimerSetting", "25:00"));
        etShortBreak.setText(sharedPreferences.getString("ShortBreak", "5:00"));
        etLongBreak.setText(sharedPreferences.getString("LongBreak", "10:00"));
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return -1;
    }

    public void setupSpinners(View view) {
        listBackground = view.findViewById(R.id.listBackground);

        // Create an ArrayAdapter using the string array and a default spinner layout

        List<Spinner> spinnerList = List.of(listBackground, listHourFormat, listAutoBreak);
        List<Integer> spinnerArray = List.of(R.array.background, R.array.hourFormat, R.array.autoBreak);
        int count = 0;

        for (Spinner spinner : spinnerList) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    spinnerArray.get(count), android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    saveSettings();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });
            count++;
        }
    }

    private void setupEditTexts() {
        List<EditText> listTexts = List.of(etShortBreak, etTimerSetting, etLongBreak);

        for (EditText editText : listTexts) {
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE) {
                        saveSettings();
                        return true;
                    }
                    return false;
                }
            });
        }

    }
}


