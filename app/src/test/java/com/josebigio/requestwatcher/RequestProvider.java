package com.josebigio.requestwatcher;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * <h1>RequestProvider</h1>
 */
public class RequestProvider {

    private static int errorCount = 0;

    public static Observable<TestPojo> getSuccess(long delay,String id) {
        return Observable.just(new TestPojo(id)).delay(delay, TimeUnit.MILLISECONDS);
    }

    public static Observable<TestPojo> getError(long delay,Throwable error) {
        return Observable.error(error);
    }

    public static Observable<TestPojo> getError(long delay,int numberOfErrorsBeforeSuccess, Throwable error, String id) {
        if(errorCount == numberOfErrorsBeforeSuccess) {
            errorCount = 0;
            return getSuccess(delay,id);
        }
        errorCount++;
        return getError(delay,error);

    }



}
