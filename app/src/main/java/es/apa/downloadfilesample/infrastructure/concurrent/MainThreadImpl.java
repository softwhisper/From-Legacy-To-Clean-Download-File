package es.apa.downloadfilesample.infrastructure.concurrent;

import android.os.Handler;
import android.os.Looper;

/**
 * MainThread represents ability to change the execution flow between background thread to UI thread
 */
public class MainThreadImpl {


    private Handler handler;


    public MainThreadImpl() {
        this.handler = new Handler(Looper.getMainLooper());
    }


    /**
     * Run the passed runnable in the UI thread
     * @param runnable
     */
    public void run(Runnable runnable) {
        handler.post(runnable);
    }
}
