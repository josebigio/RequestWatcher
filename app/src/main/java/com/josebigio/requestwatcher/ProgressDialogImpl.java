package com.josebigio.requestwatcher;

import android.content.Context;

/**
 * <h1>ProgressDialogImpl</h1>
 */
public class ProgressDialogImpl implements ProgressDialog {

    private android.app.ProgressDialog progressDialog;
    private Context context;

    public ProgressDialogImpl(Context context) {
        this.context = context;
        progressDialog = new android.app.ProgressDialog(context);
    }

    @Override
    public void show() {
        progressDialog.show();
    }

    @Override
    public void hide() {
        progressDialog.hide();
    }

    @Override
    public void dismiss() {
        progressDialog.dismiss();
    }

    @Override
    public void setTitle(String title) {
        progressDialog.setTitle(title);
    }

    @Override
    public void setMessage(String message) {
        progressDialog.setMessage(message);
    }
}
