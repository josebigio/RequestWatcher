package com.josebigio.requestwatcher;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * <h1>ErrorMessageHandler</h1>
 */
public class ErrorMessageHandlerImpl implements ErrorMessageHandler {

    private Context context;

    public ErrorMessageHandlerImpl(Context context) {
        this.context = context;
    }


    public String getMessage(Throwable throwable) {
        String defaultErrorMessage = context.getString(R.string.default_network_error_message);
        if(throwable instanceof HttpException) {
            HttpException exception = (HttpException)throwable;
            Response<?> response = exception.response();
            try {
                String errorBody = response.errorBody().string();
                try {
                    HashMap<String,Object> map =
                            new Gson().fromJson(errorBody
                                    , new TypeToken<HashMap<String, Object>>(){}.getType());
                    return map.containsKey("message") ? map.get("message").toString() : defaultErrorMessage;
                }
                catch (JsonSyntaxException je) {
                    return defaultErrorMessage;
                }

            } catch (IOException e) {
                return defaultErrorMessage;
            }

        }
        return defaultErrorMessage;
    }



}