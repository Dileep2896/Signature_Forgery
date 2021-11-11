package com.hilspade.signatureforgery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hilspade.signatureforgery.ml.SignModel;
import com.hilspade.signatureforgery.ml.SignatureForgeryModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Uri image_uri;

    Button capture, btnAnalyse;
    ImageView ivCameraPicture;

    List<Category> probability;

    float fakeScore, realScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove Dark Theme From Android
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        capture = findViewById(R.id.btnCapture);
        btnAnalyse = findViewById(R.id.btnAnalyse);

        btnAnalyse.setEnabled(false);
        capture.setText("Scan Signature");

        ivCameraPicture = findViewById(R.id.ivCameraPicture);

        capture.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    // permission not granted
                    String[] permission = {Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    // Show pop up to request
                    requestPermissions(permission, PERMISSION_CODE);
                } else {
                    // permission already granted
                    cameraIsAccessed();
                }
            } else {
                // System OS < marshmallow

            }
        });

        btnAnalyse.setOnClickListener(view -> {
            analyseSignature();
        });


    }

    private void analyseSignature() {

        if (realScore != 0 && fakeScore != 0) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("realScore", realScore);
            intent.putExtra("fakeScore", fakeScore);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "The Result Was Not Generated Please ReScan",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void cameraIsAccessed() {
        capture.setOnClickListener(view -> {
            openCamera();
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From The Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Camera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    // Handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // This method is called when user allows the permission or deny it.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission from pop up was granted
                Toast.makeText(getBaseContext(), "Permission Granted...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "Permission Denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Call when image was capture

        if (resultCode == RESULT_OK) {
            Log.i("ImageInfo", image_uri.toString());
            ivCameraPicture.setImageURI(image_uri);

            btnAnalyse.setEnabled(true);
            capture.setText("Re-Scan Signature");

            try {
                Log.i("Probability", "img");

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);

                SignatureForgeryModel model = SignatureForgeryModel.newInstance(getBaseContext());

                // Creates inputs for reference.
                TensorImage image = TensorImage.fromBitmap(bitmap);

                // Runs model inference and gets result.
                SignatureForgeryModel.Outputs outputs = model.process(image);
                probability = outputs.getProbabilityAsCategoryList();

                // Releases model resources if no longer used.
                model.close();

            } catch (IOException e) {
                // TODO Handle the exception
            }

            Log.i("Probability", probability.toString());

            fakeScore = probability.get(0).getScore();
            realScore = probability.get(1).getScore();

        } else {
            btnAnalyse.setEnabled(false);
            capture.setText("Scan Signature");
        }
    }
}