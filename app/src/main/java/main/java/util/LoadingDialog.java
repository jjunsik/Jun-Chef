package main.java.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import main.java.R;

public class LoadingDialog implements DialogInterface {
    private final Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false); // default == true

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void dismiss(){
        dialog.dismiss();
    }

    @Override
    public void cancel() {
        dismiss();
    }
}
