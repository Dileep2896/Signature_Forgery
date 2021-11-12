package com.hilspade.signatureforgery;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.tensorflow.lite.support.label.Category;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    BarChart barchartResult;

    TextView tvScanResult, tvDescription;
    Button btnReScan;

    float fakeScore, realScore;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScanResult = findViewById(R.id.tvScanResult);
        btnReScan = findViewById(R.id.btnReScan);
        barchartResult = findViewById(R.id.barchartResult);
        tvDescription = findViewById(R.id.tvDescription);

        Intent intent = getIntent();
        fakeScore = intent.getFloatExtra("fakeScore", -1);
        realScore = intent.getFloatExtra("realScore", -1);

        new AlertDialog.Builder(ResultActivity.this)
                .setTitle("Important Note")
                .setMessage("Please note that, If you have scanned any other image than any signature " +
                        "or a signature which is not stored in our database then the appropriate results" +
                        "will be false prediction, Please make sure your scanning or analysing a saved " +
                        "signature. \nThank you")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        if (fakeScore != -1 && realScore != -1) {
            if (fakeScore > realScore) {
                tvScanResult.setText("Forgery - " + df.format(fakeScore* 100) + "%");
                tvScanResult.setBackgroundResource(R.drawable.custom_primary_dark_bg);
                tvDescription.setText("The signature is not an original signature." +
                        "\nWe predicted that the signature is " + df.format(fakeScore* 100)
                        + "% Fake.\nThere are still " + df.format(realScore * 100)
                        + "% chances that the signature maybe original.");
            } else if (realScore > fakeScore) {
                tvScanResult.setText("Original - " + df.format(realScore * 100) + "%");
                tvScanResult.setBackgroundResource(R.drawable.custom_primary_bluish_bg);
                tvDescription.setText("The signature is an original signature." +
                        "\nWe predicted that the signature is " + df.format(realScore * 100)
                        + "% Genuine.\nThere are still " + df.format(fakeScore * 100)
                        + "% chances that the signature maybe fake.");
            }
        }

        btnReScan.setOnClickListener(view -> {
            finish();
        });

        setBarchartResult();

    }

    public void setBarchartResult() {
        ArrayList<BarEntry> entries_fake = new ArrayList<>();
        ArrayList<BarEntry> entries_real = new ArrayList<>();

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Forgery");
        xAxisLabel.add("Original");

        BarEntry barEntry_fake = new BarEntry(0, Float.parseFloat(df.format(fakeScore* 100)));
        entries_fake.add(barEntry_fake);

        BarEntry barEntry_real = new BarEntry(1, Float.parseFloat(df.format(realScore* 100)));
        entries_real.add(barEntry_real);

        BarDataSet barDataSet_fake = new BarDataSet(entries_fake, "Forgery");
        barDataSet_fake.setColor(Color.rgb(153,167,153));
        barDataSet_fake.setValueTextSize(12f);

        BarDataSet barDataSet_real = new BarDataSet(entries_real, "Original");
        barDataSet_real.setColor(Color.rgb(135,170,170));
        barDataSet_real.setValueTextSize(12f);

        BarData data = new BarData(barDataSet_fake, barDataSet_real);

        XAxis xAxis = barchartResult.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);

        barchartResult.getLegend().setEnabled(false);
        barchartResult.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        barchartResult.getDescription().setEnabled(false);

        barchartResult.setData(data);
        barchartResult.invalidate();

    }

}