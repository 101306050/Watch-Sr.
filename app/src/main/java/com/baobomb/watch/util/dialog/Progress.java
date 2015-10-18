package com.baobomb.watch.util.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Baobomb on 15/8/3.
 */
public class Progress {

    private ProgressDialog progressDialog;

    public void bind(Context context) {
        progressDialog = new ProgressDialog(context);
    }

    public void showProgress(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismiss() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}
