package com.josebigio.requestwatcher;

/**
 * <h1>DialogController</h1>
 */
public interface ProgressDialog {
    void show();
    void hide();
    void dismiss();
    void setTitle(String title);
    void setMessage(String message);
}
