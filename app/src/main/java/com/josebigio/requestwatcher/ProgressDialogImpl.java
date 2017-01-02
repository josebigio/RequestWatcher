package com.josebigio.requestwatcher;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * <h1>ProgressDialogImpl</h1>
 */
public class ProgressDialogImpl implements ProgressDialog {

    private android.app.ProgressDialog progressDialog;
    private Context context;
    private Handler handler = new Handler(Looper.getMainLooper());

    public ProgressDialogImpl(Context context) {
        this.context = context;
        progressDialog = new android.app.ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        handler.post(()->progressDialog.show());
    }

    @Override
    public void hide() {
        handler.post(()->progressDialog.hide());
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
