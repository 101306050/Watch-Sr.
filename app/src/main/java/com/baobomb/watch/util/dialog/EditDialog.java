package com.baobomb.watch.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.UiThread;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditDialog {
    private Context context;

    public void bind(Context context) {
        this.context = context;
    }


    @UiThread
    public void showDialog(String messageTitle, final OnDialogClickListener onDialogClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(messageTitle);

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Comfirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogClickListener.onDialogClick(input.getText().toString());
                    }
                });
        alertDialog.show();
    }


    public static abstract class OnDialogClickListener {
        public abstract void onDialogClick(String condition);
    }
}
