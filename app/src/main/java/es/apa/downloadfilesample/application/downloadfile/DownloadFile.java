package es.apa.downloadfilesample.application.downloadfile;

import java.io.File;

/**
 * Created by alberto on 6/8/16.
 */
public interface DownloadFile {
    interface UseCase{
        void start(String url, File file, Callback callback);
        void stop();
        boolean isRunning();

        interface Callback{
            void onDownload(File storedFile);
            void onDownloadError(Throwable throwable);
        }
    }
}
