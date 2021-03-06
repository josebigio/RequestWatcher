package com.josebigio.requestwatcher.internal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.josebigio.requestwatcher.R;
import com.josebigio.requestwatcher.RequestWatcher;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final ApiService apiService = new ApiService();
    View requestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestButton = findViewById(R.id.requestButton);
        requestButton.setOnClickListener(v -> makeRequest());


    }

    private void makeRequest() {
        String username = "josebigio";
        Observable<User> call = apiService.getUser(username);
        RequestWatcher<User> requestWatcher = new RequestWatcher.Builder<User>(this).build();
        Subscription subscription = requestWatcher.dispatch(call)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            int code = response.code();
                        }
                    }

                    @Override
                    public void onNext(User user) {
                        Toast.makeText(MainActivity.this,String.format("User: %s",user),Toast.LENGTH_LONG).show();
                    }
                });
    }


}
