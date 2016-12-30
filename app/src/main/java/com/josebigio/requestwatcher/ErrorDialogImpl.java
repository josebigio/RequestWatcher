package com.josebigio.requestwatcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * <h1>ErrorDialogImpl</h1>
 */
public class ErrorDialogImpl implements ErrorDialog {

    private Context context;
    private AlertDialog alertDialog;
    private DialogInterface.OnClickListener retryAction;
    private DialogInterface.OnClickListener okAction;

    public ErrorDialogImpl(Context context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, this::doOk)
                .setNegativeButton(android.R.string.no, this::doRetryAction)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    @Override
    public void show() {
        alertDialog.show();
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
        alertDialog.dismiss();
    }

    @Override
    public void setOnRetryAction(DialogInterface.OnClickListener action) {
        this.retryAction = action;
    }

    @Override
    public void setOnOkAction(DialogInterface.OnClickListener action) {
        this.okAction = action;
    }

    private void doOk(DialogInterface dialogInterface, int i) {
        okAction.onClick(dialogInterface,i);
    }

    private void doRetryAction(DialogInterface dialogInterface, int i) {
        retryAction.onClick(dialogInterface,i);
    }
}
