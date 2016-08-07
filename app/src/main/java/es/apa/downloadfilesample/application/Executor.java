package es.apa.downloadfilesample.application;

/**
 * Created by alberto on 18/6/16.
 */
public interface Executor {
    void runInBackgroundThread(Runnable runnable);
    void runInMainThread(Runnable runnable);
}
