package com.josebigio.requestwatcher;

/**
 * <h1>ErrorMessageHandler</h1>
 */
public interface ErrorMessageHandler {
    String getMessage(Throwable throwable);
}
