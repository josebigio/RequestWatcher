package com.josebigio.requestwatcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

/**
 * <h1>ErrorDialogImpl</h1>
 */
public class ErrorDialogImpl implements ErrorDialog {

    private Context context;
    private AlertDialog alertDialog;
    private DialogInterface.OnClickListener retryAction;
    private DialogInterface.OnClickListener okAction;
    private Handler handler = new Handler(Looper.getMainLooper());

    public ErrorDialogImpl(Context context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
    }



    @Override
    public void show() {
        handler.post(()->alertDialog.show());
    }

    @Override
    public void setTitle(String title) {
        alertDialog.setTitle(title);
    }

    @Override
    public void setMessage(String message) {
        alertDialog.setMessage(message);
    }

    @Override
    public void dismiss() {
        handler.post(()->alertDialog.dismiss());
    }

    @Override
    public void addOnRetryAction(DialogInterface.OnClickListener action) {
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Retry",action);
    }

    @Override
    public void addOnOkAction(DialogInterface.OnClickListener action) {
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Ok",action);
    }

}
