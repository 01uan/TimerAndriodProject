package com.example.giaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * SettingsTab class that handles the settings functionality of the app.
 * This fragment allows users to modify settings such as timer duration, break durations, and auto break options.
 */
public class SettingsTab extends Fragment {

    private Spinner listAutoBreak;
    private EditText etTimerSetting, etShortBreak, etLongBreak;
    private SharedPreferences sharedPreferences;

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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listAutoBreak = view.findViewById(R.id.listAutoBreak);
        etTimerSetting = view.findViewById(R.id.etTimerSetting);
        etShortBreak = view.findViewById(R.id.etShortBreak);
        etLongBreak = view.findViewById(R.id.etLongBreak);

        // Setup the spinners and edit texts
        setupSpinners();
        setupEditTexts();

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        // Load saved settings
        loadSettings();
    }

    /**
     * Save the current settings to SharedPreferences.
     */
    public void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AutoBreak", listAutoBreak.getSelectedItem().toString());
        editor.putString("TimerSetting", etTimerSetting.getText().toString());
        editor.putString("ShortBreak", etShortBreak.getText().toString());
        editor.putString("LongBreak", etLongBreak.getText().toString());
        editor.apply();
    }

    /**
     * Load the settings from SharedPreferences and set them to the UI elements.
     */
    public void loadSettings() {
        listAutoBreak.setSelection(getIndex(listAutoBreak, sharedPreferences.getString("AutoBreak", "Enable")));
        etTimerSetting.setText(sharedPreferences.getString("TimerSetting", "25:00"));
        etShortBreak.setText(sharedPreferences.getString("ShortBreak", "5:00"));
        etLongBreak.setText(sharedPreferences.getString("LongBreak", "10:00"));
    }

    /**
     * Get the index of a specific string in a spinner.
     * @param spinner The spinner to search in.
     * @param myString The string to search for.
     * @return The index of the string in the spinner, or -1 if not found.
     */
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Setup the spinners with adapters and item selected listeners.
     */
    public void setupSpinners() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<Spinner> spinnerList = List.of(listAutoBreak);
        List<Integer> spinnerArray = List.of(R.array.autoBreak);
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
                    // Save settings when an item is selected
                    saveSettings();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Do nothing
                }
            });
            count++;
        }
    }

    /**
     * Setup the EditTexts with action listeners to save settings when editing is done.
     */
    private void setupEditTexts() {
        List<EditText> listTexts = List.of(etShortBreak, etTimerSetting, etLongBreak);

        for (EditText editText : listTexts) {
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE) {
                        // Save settings when editing is done
                        saveSettings();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
