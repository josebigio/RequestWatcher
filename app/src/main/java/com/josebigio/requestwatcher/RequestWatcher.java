package com.josebigio.requestwatcher;


import android.content.Context;
import android.util.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * <h1>RequestWatcher</h1>
 */


public class RequestWatcher<T>{

    private static final String TAG = RequestWatcher.class.getCanonicalName();
    

    private boolean showProgress;
    private boolean showErrorAlert;
    private boolean withRetry;
    private ProgressDialog progressDialog;
    private Context context;
    private String loadingMessage;
    private ErrorMessageHandler errorMessageHandler;


    private RequestWatcher(Context activity, boolean showProgress, boolean showErrorAlert,
                            boolean withRetry, ProgressDialog progressDialog, String loadingMessage,
                            ErrorMessageHandler errorMessageHandler) {

        this.showProgress = showProgress;
        this.showErrorAlert = showErrorAlert;
        this.withRetry = withRetry;
        this.progressDialog = progressDialog;
        this.context = activity;
        this.loadingMessage = loadingMessage;
        this.errorMessageHandler = errorMessageHandler;
    }
    
    public Observable<T> dispatch(Observable<T> networkObservable) {
        if (networkObservable == null) {
            throw new IllegalStateException("NetworkObservable cannot be null!");
        }

        return networkObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new ErrorHandler())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                     
                    public Observable<? extends T> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .doOnSubscribe(() -> {
                    if (showProgress) {
                        Log.d(TAG,"Subscribing, showing progress");
                        progressDialog.show();
                    }
                }).doOnUnsubscribe(new Action0() {
                     
                    public void call() {
                        if (showProgress) {
                            Log.d(TAG,"Unsubscribing, hiding progress");
                            progressDialog.hide();
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }


    //region ErrorHandler
    private class ErrorHandler extends Thread
            implements Func1<Observable<? extends Throwable>, Observable<Boolean>> {

         
        public Observable<Boolean> call(Observable<? extends Throwable> observable) {
            return observable
                    .switchMap(new Func1<Throwable, Observable<? extends Boolean>>() {
                         
                        public Observable<? extends Boolean> call(Throwable throwable) {
                            return Observable.<Boolean>create(
                                    subscriber -> {
                                        if(showErrorAlert) {
                                            String errorMessage = errorMessageHandler.getMessage(throwable);
                                            ErrorDialog errorDialog = new ErrorDialogImpl(context);
                                            errorDialog.setTitle("Error");
                                            errorDialog.setMessage(errorMessage);
                                            if(withRetry) {
                                                errorDialog.setOnRetryAction((dialogInterface, i) -> {
                                                    subscriber.onNext(true);
                                                    subscriber.onCompleted();
                                                });
                                            }
                                            errorDialog.setOnOkAction((dialogInterface, i) -> {
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
//    public static class Builder<T> {
//
//        private boolean innerShowProgress;
//        private boolean innerShowErrorAlert;
//        private boolean innerWithRetry;
//        private String innerLoadingMessage;
//        private String innerCustomMessage;
//        private Context innerContext;
//        private ErrorMessageHandler innerErrorHandler;
//        private RSDialogController innerDialogControler;
//
//        public Builder(Context activity, RSDialogController dialogController) {
//            this.innerContext = activity;
//            this.innerDialogControler = dialogController;
//            innerErrorHandler = new DefaultErrorMessageHandler(activity);
//        }
//
//
//        public Builder<T> showProgress(boolean showProgress, String loadingMessage) {
//            innerShowProgress = showProgress;
//            innerLoadingMessage = loadingMessage;
//            return this;
//        }
//
//        public Builder<T> showErrorAlert(boolean show) {
//            innerShowErrorAlert = show;
//            return this;
//        }
//
//        public Builder<T> withRetry(boolean retry) {
//            innerWithRetry = retry;
//            return this;
//        }
//
//        public Builder<T> withCustomMessage(String customMessage) {
//            innerCustomMessage = customMessage;
//            return this;
//        }
//
//        public Builder<T> withErrorMessageHandler(ErrorMessageHandler errorMessageHandler) {
//            this.innerErrorHandler = errorMessageHandler;
//            return this;
//        }
//
//
//        public MRequestWatcher<T> build() {
//            return new MRequestWatcher<>(innerContext,innerDialogControler, innerShowProgress, innerShowErrorAlert,
//                    innerWithRetry, innerCustomMessage, innerLoadingMessage,innerErrorHandler);
//        }
//
//
//    }
//    //endregion

    public interface ErrorMessageHandler {
        String getMessage(Throwable throwable);
    }

    private static class DefaultErrorMessageHandler implements ErrorMessageHandler {

        private Context context;

        public DefaultErrorMessageHandler(Context context) {
            this.context = context;
        }


        public String getMessage(Throwable throwable) {
//            String defaultErrorMessage = context.getString(R.string.default_network_error_message);
//            if(throwable instanceof HttpException) {
//                HttpException exception = (HttpException)throwable;
//                Response<?> response = exception.response();
//                try {
//                    String errorBody = response.errorBody().string();
//                    try {
//                        HashMap<String,Object> map =
//                                new Gson().fromJson(errorBody
//                                        , new TypeToken<HashMap<String, Object>>(){}.getType());
//                        return map.containsKey("message") ? map.get("message").toString() : defaultErrorMessage;
//                    }
//                    catch (JsonSyntaxException je) {
//                        return defaultErrorMessage;
//                    }
//
//                } catch (IOException e) {
//                    return defaultErrorMessage;
//                }
//
//            }
//            return defaultErrorMessage;
//        }
            return context.getString(R.string.default_network_error_message);
        }


    }
}
