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
    void setOnRetryAction(DialogInterface.OnClickListener action);
    void setOnOkAction(DialogInterface.OnClickListener action);
}
