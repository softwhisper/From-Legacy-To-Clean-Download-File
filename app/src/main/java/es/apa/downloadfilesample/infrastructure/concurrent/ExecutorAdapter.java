package es.apa.downloadfilesample.infrastructure.concurrent;


import es.apa.downloadfilesample.application.Executor;

/**
 * Created by alberto on 18/6/16.
 */
public class ExecutorAdapter implements Executor {

    private ExecutorImpl executor;
    private MainThreadImpl mainThread;


    public ExecutorAdapter(ExecutorImpl executor, MainThreadImpl mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }


    @Override
    public void runInBackgroundThread(Runnable runnable) {
        executor.execute(runnable);
    }


    @Override
    public void runInMainThread(Runnable runnable) {
        mainThread.run(runnable);
    }
}
