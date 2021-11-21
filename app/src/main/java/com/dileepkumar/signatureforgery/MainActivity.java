package com.dileepkumar.signatureforgery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dileepkumar.signatureforgery.ml.SignatureForgeryModel;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Side Menu Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    public static final int PICK_IMAGE = 1;

    Uri image_uri;

    Button capture, btnAnalyse, btnSelectFromPhone;
    ImageView ivCameraPicture;

    List<Category> probability;

    float fakeScore, realScore;

    private FirebaseAuth mAuth;

    String name, email;

    @SuppressLint({"IntentReset", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Side menu variables
        drawerLayout = findViewById(R.id.drawableLayout);
        navigationView = findViewById(R.id.navigationSideBar);
        toolbar = findViewById(R.id.toolBarMain);

        // Page other components
        capture = findViewById(R.id.btnCapture);
        btnAnalyse = findViewById(R.id.btnAnalyse);
        btnSelectFromPhone = findViewById(R.id.btnSelectFromPhone);

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
            }  // System OS < marshmallow

        });

        btnAnalyse.setOnClickListener(view -> {
            analyseSignature();
        });

        btnSelectFromPhone.setOnClickListener(view -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        });

    }

    // Implementing side menu bar
    @SuppressLint("NonConstantResourceId")
    private void sideMainMenu() {

        Log.i("Email", email);
        Log.i("Name", name);

        Menu menu = navigationView.getMenu();
        MenuItem userEmail = menu.findItem(R.id.userEmail);
        userEmail.setTitle(email);

        MenuItem userName = menu.findItem(R.id.userName);
        userName.setTitle(name);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.cameraConnect:
                    intent = new Intent(this, ConnectToCamera.class);
                    startActivity(intent);
                    drawerLayout.close();
                    break;
                case R.id.analyseMenu:
                    drawerLayout.close();
                    break;
                case R.id.compareImages:
                    intent = new Intent(this, CompareImagesActivity.class);
                    startActivity(intent);
                    Toast.makeText(getBaseContext(), "Compare Images", Toast.LENGTH_SHORT).show();
                    drawerLayout.close();
                    break;
                case R.id.trainNewImages:
                    intent = new Intent(this, TrainActivity.class);
                    startActivity(intent);
                    drawerLayout.close();
                    break;
                case R.id.subscriptions:
                    intent = new Intent(this, Subscriptions.class);
                    startActivity(intent);
                    drawerLayout.close();
                    break;
                case R.id.faqMenu:
                    Toast.makeText(getBaseContext(), "FAQ", Toast.LENGTH_SHORT).show();
                    drawerLayout.close();
                    break;
                case R.id.logout:
                    Toast.makeText(getBaseContext(), "Logout", Toast.LENGTH_SHORT).show();
                    drawerLayout.close();
                    FirebaseAuth.getInstance().signOut();
                    intent = new Intent(this, LoginPage.class);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(getBaseContext(), "No Options", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            name = currentUser.getDisplayName();
            email = currentUser.getEmail();
        } else {
            name = "No User Name Found";
            email = "No User Email Found";
        }
        sideMainMenu();
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

        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            Log.i("ImageInfo", image_uri.toString());
            ivCameraPicture.setImageURI(image_uri);

            btnAnalyse.setEnabled(true);
            capture.setText("Re-Scan Signature");

            try {
                Log.i("Probability", "img");

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);

                SignatureForgeryModel model =
                        SignatureForgeryModel.newInstance(getBaseContext());

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

        } else if (requestCode == PICK_IMAGE) {

            assert data != null;
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                ivCameraPicture.setImageBitmap(bitmap);

                if (bitmap != null) {
                    btnAnalyse.setEnabled(true);

                    SignatureForgeryModel model =
                            SignatureForgeryModel.newInstance(getBaseContext());
                    // Creates inputs for reference.
                    TensorImage image = TensorImage.fromBitmap(bitmap);

                    // Runs model inference and gets result.
                    SignatureForgeryModel.Outputs outputs = model.process(image);
                    probability = outputs.getProbabilityAsCategoryList();

                    // Releases model resources if no longer used.
                    model.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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