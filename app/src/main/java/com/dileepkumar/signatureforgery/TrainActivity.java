package com.dileepkumar.signatureforgery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TrainActivity extends AppCompatActivity {

    Spinner spinnerSelectSignature, spinnerSelectMode;

    String[] signatureItems = new String[]{"Signature 1", "Signature 2", "Signature 3",
            "Signature 4", "Signature 5"};

    String[] modeItems = new String[]{"Original", "Forgery"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        spinnerSelectSignature = findViewById(R.id.spinnerSelectSignature);
        spinnerSelectMode = findViewById(R.id.spinnerSelectMode);

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterSignature = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, signatureItems);

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterMode = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, modeItems);

        adapterSignature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the spinners adapter to the previously created one.
        spinnerSelectSignature.setAdapter(adapterSignature);
        // On item selected
        spinnerSelectSignature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the spinners adapter to the previously created one.
        spinnerSelectMode.setAdapter(adapterMode);
        // On item selected
        spinnerSelectMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}