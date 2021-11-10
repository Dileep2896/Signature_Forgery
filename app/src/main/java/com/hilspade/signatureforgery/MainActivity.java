package com.hilspade.signatureforgery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_ACTION_CAMERA = 9;

    List<String> listPermissionsNeeded = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }



        Button capture = findViewById(R.id.btnCapture);

        capture.setOnClickListener(v -> {
            openCamera();
        });

    }

    void openCamera() {
        Intent cameraImgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraImgIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",
                        new File("your_file_name_with_dir")));
        startActivityForResult(cameraImgIntent, REQUEST_ACTION_CAMERA);
    }

}