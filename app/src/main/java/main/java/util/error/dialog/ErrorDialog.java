package main.java.util.error.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import main.java.util.error.ErrorFormat;

public class ErrorDialog implements DialogInterface{

    private final String title;
    private final String message;

    private static final String CHECK_BUTTON_TEXT = "확인";
    Context context;
    AlertDialog alertDialog;

    public ErrorDialog(Context context, ErrorFormat errorFormat) {
        this.context = context;
        this.title = errorFormat.getTitle();
        this.message = errorFormat.getMessage();
    }

    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(CHECK_BUTTON_TEXT, (dialog, which) -> {
            dialog.dismiss();
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void cancel() {
        dismiss();
    }

    @Override
    public void dismiss() {
        alertDialog.dismiss();
    }
}
