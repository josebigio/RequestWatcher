package com.josebigio.requestwatcher;


import android.content.Context;
import android.util.Log;

import rx.Observable;
import rx.functions.Func1;

/**
 * <h1>RequestWatcher</h1>
 */


public class RequestWatcher<T>{

    private static final String TAG = RequestWatcher.class.getCanonicalName();
    

    private boolean showProgress;
    private boolean showErrorAlert;
    private boolean withRetry;
    private ProgressDialog progressDialog;
    private ErrorDialog errorDialog;
    private Context context;
    private String loadingMessage;
    private ErrorMessageHandler errorMessageHandler;


    private RequestWatcher(Context activity, boolean showProgress, boolean showErrorAlert,
                            boolean withRetry, ProgressDialog progressDialog,ErrorDialog errorDialog, String loadingMessage,
                            ErrorMessageHandler errorMessageHandler) {

        this.showProgress = showProgress;
        this.showErrorAlert = showErrorAlert;
        this.withRetry = withRetry;
        this.progressDialog = progressDialog;
        this.context = activity;
        this.loadingMessage = loadingMessage;
        this.errorMessageHandler = errorMessageHandler;
        this.errorDialog = errorDialog;
    }
    
    public Observable<T> dispatch(Observable<T> networkObservable) {
        if (networkObservable == null) {
            throw new IllegalStateException("NetworkObservable cannot be null!");
        }

        return networkObservable
                .retryWhen(new ErrorHandler())
                .onErrorResumeNext(Observable::error)
                .doOnSubscribe(() -> {
                    if (showProgress) {
                        Log.d(TAG,"Subscribing, showing progress");
                        progressDialog.show();
                    }
                }).doOnUnsubscribe(() -> {
                    if (showProgress) {
                        Log.d(TAG,"Unsubscribing, hiding progress");
                        progressDialog.hide();
                    }
                });
    }


    //region ErrorHandler
    private class ErrorHandler extends Thread
            implements Func1<Observable<? extends Throwable>, Observable<Boolean>> {

         
        public Observable<Boolean> call(Observable<? extends Throwable> observable) {
            return observable
                    .switchMap(new Func1<Throwable, Observable<? extends Boolean>>() {
                         
                        public Observable<? extends Boolean> call(Throwable throwable) {
                            return Observable.create(
                                    subscriber -> {
                                        if(showErrorAlert) {
                                            String errorMessage = errorMessageHandler.getMessage(throwable);
                                            errorDialog.setTitle("Error");
                                            errorDialog.setMessage(errorMessage);
                                            if(withRetry) {
                                                errorDialog.addOnRetryAction((dialogInterface, i) -> {
                                                    subscriber.onNext(true);
                                                    subscriber.onCompleted();
                                                });
                                            }
                                            errorDialog.addOnOkAction((dialogInterface, i) -> {
                                                errorDialog.dismiss();
                                                subscriber.onError(throwable);
                                            });
                                           errorDialog.show();
                                        }
                                        else {
                                            subscriber.onError(throwable);
                                        }
                                    });
                        }
                    });
        }

    }
    //endregion


//    //region Builder
    public static class Builder<T> {

        private boolean innerShowProgress;
        private boolean innerShowErrorAlert;
        private boolean innerWithRetry;
        private String innerLoadingMessage = "Loading";
        private Context innerContext;
        private ErrorMessageHandler innerErrorHandler;
        private ProgressDialog innerProgressDialog;
        private ErrorDialog innerErrorDialog;

    public Builder(Context context) {
            this.innerContext = context;
            this.innerProgressDialog = new ProgressDialogImpl(context);
            this.innerErrorDialog = new ErrorDialogImpl(context);
            this.innerErrorHandler = new ErrorMessageHandlerImpl(context);
            this.innerShowProgress = true;
            this.innerShowErrorAlert = true;
            this.innerWithRetry = true;
        }


        public Builder<T> showProgress(boolean showProgress) {
            innerShowProgress = showProgress;
            return this;
        }

        public Builder<T> withProgressDialog(ProgressDialog progressDialog) {
            innerProgressDialog = progressDialog;
            return this;
        }

        public Builder<T> withErrorDialog(ErrorDialog errorDialog) {
            innerErrorDialog = errorDialog;
            return this;
        }

        public Builder<T> setLoadingMessage(String loadingMessage) {
            innerLoadingMessage = loadingMessage;
            return this;
        }

        public Builder<T> showErrorAlert(boolean show) {
            innerShowErrorAlert = show;
            return this;
        }

        public Builder<T> withRetry(boolean retry) {
            innerWithRetry = retry;
            return this;
        }


        public Builder<T> withErrorMessageHandler(ErrorMessageHandler errorMessageHandler) {
            this.innerErrorHandler = errorMessageHandler;
            return this;
        }


        public RequestWatcher<T> build() {
            return new RequestWatcher<T>(innerContext,innerShowProgress,innerShowErrorAlert,innerWithRetry,innerProgressDialog,innerErrorDialog,innerLoadingMessage,innerErrorHandler);
        }


    }
    //end region

}
