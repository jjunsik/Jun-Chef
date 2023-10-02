package main.java.util.error.dialog;

import static main.java.util.error.constant.ErrorConstant.CHECK_BUTTON_TEXT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import main.java.util.error.ErrorFormat;

public class ErrorDialog implements DialogInterface{
    private final String title;
    private final String message;
    private final Context context;
    private AlertDialog alertDialog;

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
            // 확인 버튼을 클릭했을 때 수행할 동작
            dialog.dismiss(); // 다이얼로그 닫기
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
