package com.josebigio.requestwatcher.internal;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * <h1>ApiService</h1>
 */
public class ApiService {

    private static final String BASE_URL = "https://api.github.com";
    private static Api api;
    private static long DELAY = 3000;
    private static int TIMES = 0;
    private static final String TAG = ApiService.class.getCanonicalName();

    public ApiService() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
        api = retrofit.create(Api.class);
    }


    public Observable<User> getUser(String userName) {
        return api.getUser(userName).delay(DELAY, TimeUnit.MILLISECONDS);

//        TIMES++;
//        if(TIMES%2 == 0) {
//            Log.d(TAG,String.format("sending fake"));
//            return api.getUser("NOPE" + userName).delay(DELAY, TimeUnit.MILLISECONDS);
//        }
//        else {
//            Log.d(TAG,String.format("sending real"));
//            return api.getUser(userName).delay(DELAY, TimeUnit.MILLISECONDS);
//        }
    }




}
