package es.apa.downloadfilesample.application.downloadfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import es.apa.downloadfilesample.application.Disk;
import es.apa.downloadfilesample.application.Download;
import es.apa.downloadfilesample.application.Executor;
import es.apa.downloadfilesample.application.downloadfile.exceptions.StorateNotReadyException;

/**
 * Created by alberto on 6/8/16.
 */
public class DownloadFileUseCase implements DownloadFile.UseCase {

    private Disk.Accesor diskAccesor;
    private Disk.State diskState;
    private Executor executor;
    private Callback callback;
    private Download.File downloadFile;


    public DownloadFileUseCase(Disk.Accesor diskAccesor,
                               Disk.State diskState,
                               Executor executor,
                               Download.File downloadFile) {
        this.diskAccesor = diskAccesor;
        this.diskState = diskState;
        this.executor = executor;
        this.downloadFile = downloadFile;
    }


    @Override
    public void start(final String fileUrl, final File file, Callback callback) {
        this.callback = callback;
        executor.runInBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!diskState.isExternalStorageReadable()) {
                        sendError(new StorateNotReadyException());
                    } else if (file.exists() && file.length() > 0) {
                        //read
                        sendSuccess(file);
                    } else {
                        //write
                        if (diskState.isExternalStorageWritable()) {
                            boolean create = file.createNewFile();
                            if (create) {
                                byte[]content = downloadFile.start(fileUrl);
                                diskAccesor.write(file, content);
                                sendSuccess(file);
                            } else {
                                sendError(new FileNotFoundException());
                            }
                        } else {
                            sendError(new StorateNotReadyException());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    sendError(e);
                }
            }
        });
    }

    @Override
    public void stop() {
        clearCallback();
    }

    @Override
    public boolean isRunning() {
        return callback != null;
    }


    private void sendSuccess(final File storedFile){
        executor.runInMainThread(new Runnable() {
            @Override
            public void run() {
                if (hasCallback()){
                    callback.onDownload(storedFile);
                }
                clearCallback();
            }
        });
    }


    private void sendError(final Throwable throwable){
        executor.runInMainThread(new Runnable() {
            @Override
            public void run() {
                if (hasCallback()){
                    callback.onDownloadError(throwable);
                }
                clearCallback();
            }
        });
    }

    private boolean hasCallback(){
        return callback != null;
    }


    private void clearCallback(){
        callback = null;
    }
}
