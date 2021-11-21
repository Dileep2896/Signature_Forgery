package com.dileepkumar.signatureforgery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dileepkumar.signatureforgery.ml.SignatureForgeryModel;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.IOException;

public class CompareImagesActivity extends AppCompatActivity {

    Button btnCapture1, btnCapture2;
    ImageView ivCameraPicture1, ivCameraPicture2;

    Uri image_uri;

    private static final int PERMISSION_CODE = 1000;
    public static final int PICK_IMAGE_1 = 1;
    public static final int PICK_IMAGE_2 = 2;

    @SuppressLint("IntentReset")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_images);

        btnCapture1 = findViewById(R.id.btnCapture1);
        btnCapture2 = findViewById(R.id.btnCapture2);

        ivCameraPicture1 = findViewById(R.id.ivCameraPicture1);
        ivCameraPicture2 = findViewById(R.id.ivCameraPicture2);

        btnCapture1.setOnClickListener(view -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE_1);
        });

        btnCapture2.setOnClickListener(view -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE_2);
        });

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

        if (requestCode == PICK_IMAGE_1) {

            assert data != null;
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                ivCameraPicture1.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICK_IMAGE_2) {
            assert data != null;
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                ivCameraPicture2.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}