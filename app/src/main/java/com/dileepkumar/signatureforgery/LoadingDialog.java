package com.dileepkumar.signatureforgery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;

public class LoadingDialog {

    Activity activity;
    AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("InflateParams")
    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_loading_dialog, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

    }

    void dismissDialog() {
        alertDialog.dismiss();
    }

}
