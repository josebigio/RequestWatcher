package com.josebigio.requestwatcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.observers.TestSubscriber;

/**
 * <h1>RequestWatcherUnitTest</h1>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=22)
public class RequestWatcherUnitTest {

    private static long DELAY = 3000;

    @Test
    public void testSuccess() throws InterruptedException {
        RequestWatcher<TestPojo> requestWatcher =
                new RequestWatcher.Builder<TestPojo>(RuntimeEnvironment.application)
                        .showErrorAlert(false)
                        .showProgress(false)
                .build();
        log("starting request");
        TestPojo result = requestWatcher.dispatch(RequestProvider.getSuccess(DELAY, "jose"))
                .doOnNext(next -> log("onNext"))
                .toBlocking().first();

    }

    @Test
    public void testFail() {
        RequestWatcher<TestPojo> requestWatcher =
                new RequestWatcher.Builder<TestPojo>(RuntimeEnvironment.application)
                        .showErrorAlert(false)
                        .showProgress(false)
                        .build();
        log("starting request");
        TestSubscriber<TestPojo> testSubscriber = new TestSubscriber<>();
        Throwable error = new Throwable("jose");
        requestWatcher.dispatch(RequestProvider.getError(DELAY,error)).subscribe(testSubscriber);
        testSubscriber.assertError(error);

    }

    @Test
    public void testDialogShown() {
        ProgressDialog progressDialog = Mockito.mock(ProgressDialogImpl.class);
        RequestWatcher<TestPojo> requestWatcher =
                new RequestWatcher.Builder<TestPojo>(RuntimeEnvironment.application)
                        .showErrorAlert(false)
                        .showProgress(true)
                        .withProgressDialog(progressDialog)
                        .build();
        log("starting request");
        TestSubscriber<TestPojo> testSubscriber = new TestSubscriber<>();
        requestWatcher.dispatch(RequestProvider.getSuccess(DELAY,"jose")).subscribe(testSubscriber);
        Mockito.verify(progressDialog).show();
        testSubscriber.unsubscribe();
        Mockito.verify(progressDialog).hide();

    }

    private void log(String message) {
        System.out.println(message);
    }

}
