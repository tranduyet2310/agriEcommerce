package com.example.argiecommerce.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.argiecommerce.R

class ProgressDialog() {
    fun createAlertDialog(activity: Activity): AlertDialog{
        val inflater = activity.layoutInflater
        val dialogLayout = inflater.inflate(R.layout.custom_progress_dialog, null);
        val builder = AlertDialog.Builder(activity);
        builder.setView(dialogLayout)
        val alert = builder.create();
        alert.show();
        alert.window?.setLayout(600, 300)
        return alert
    }
}