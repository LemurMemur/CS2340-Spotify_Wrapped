package com.example.cs2340_spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<WrapperData> history;
    WrapperData returnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initHistory();
        initHistorySelectionSpinner();
        initButtons();
    }

    private void initHistory() {
        history = new ArrayList<>();
        history.add(Wrapper.currWrapperData);
    }

    private void initHistorySelectionSpinner() {
        Spinner historySelectSpinner = (Spinner)findViewById(R.id.historySelectSpinner);
        String[] lst = new String[history.size()];
        for (int i = 0; i < lst.length; ++i) {
            lst[i] = "" + i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                Settings.this,
                android.R.layout.simple_spinner_item,
                lst
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historySelectSpinner.setAdapter(adapter);
        historySelectSpinner.setOnItemSelectedListener(this);
        historySelectSpinner.setSelection(0);
        historySelectSpinner.setVisibility(View.VISIBLE);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
        returnData = history.get(pos);
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }
    private void initButtons() {
        ImageButton settings = findViewById(R.id.settingsGoBack);
        settings.setOnClickListener((v) -> {
            Wrapper.currWrapperData = null;
            Intent intent = new Intent(getApplicationContext(), Wrapper.class);
            startActivity(intent);
        });
        Button game = findViewById(R.id.timeTravelButton);
        game.setOnClickListener((v) -> {
            Wrapper.currWrapperData = returnData;
            Intent intent = new Intent(getApplicationContext(), Wrapper.class);
            startActivity(intent);
        });
    }

}