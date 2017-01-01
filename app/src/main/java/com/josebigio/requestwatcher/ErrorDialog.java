package com.josebigio.requestwatcher;

import android.content.DialogInterface;

/**
 * <h1>ErrorDialog</h1>
 */
public interface ErrorDialog {

    void show();
    void setTitle(String title);
    void setMessage(String message);
    void dismiss();
    void addOnOkAction(DialogInterface.OnClickListener action);
    void addOnRetryAction(DialogInterface.OnClickListener action);
}
