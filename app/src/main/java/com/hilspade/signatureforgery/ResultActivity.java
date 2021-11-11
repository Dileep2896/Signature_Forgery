package com.hilspade.signatureforgery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.tensorflow.lite.support.label.Category;

import java.text.DecimalFormat;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    TextView tvScanResult;
    Button btnReScan;

    float fakeScore, realScore;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScanResult = findViewById(R.id.tvScanResult);
        btnReScan = findViewById(R.id.btnReScan);

        Intent intent = getIntent();
        fakeScore = intent.getFloatExtra("fakeScore", -1);
        realScore = intent.getFloatExtra("realScore", -1);

        if (fakeScore != -1 && realScore != -1) {
            if (fakeScore > realScore) {
                tvScanResult.setText("Fake - " + df.format(fakeScore* 100) + "%");
            } else if (realScore > fakeScore) {
                tvScanResult.setText("Genuine - " + df.format(realScore * 100) + "%");
            }
        }

        btnReScan.setOnClickListener(view -> {
            finish();
        });

    }
}