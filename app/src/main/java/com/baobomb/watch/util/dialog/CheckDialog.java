package com.baobomb.watch.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.UiThread;


public class CheckDialog {
    private Context context;

    public void bind(Context context) {
        this.context = context;
    }


    public void showDialog(String messageTitle, String message, final OnDialogClickListener
            onDialogClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(messageTitle)
                .setMessage(message)
                .setPositiveButton("Confirm", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (onDialogClickListener != null) {
                                    onDialogClickListener.onDialogClick(true);
                                }
                            }
                        })
                .setNegativeButton("Cancel", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (onDialogClickListener != null) {
                                    onDialogClickListener.onDialogClick(false);
                                }
                            }
                        })
                .show();
    }

    @UiThread
    public void showAlertDialog(String messageTitle, String message, final OnDialogClickListener
            onDialogClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(messageTitle)
                .setMessage(message)
                .setNeutralButton("Confirm", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (onDialogClickListener != null) {
                                    onDialogClickListener.onDialogClick(true);
                                }
                            }
                        })
                .show();
    }


    public static abstract class OnDialogClickListener {
        public abstract void onDialogClick(Boolean result);
    }
}
