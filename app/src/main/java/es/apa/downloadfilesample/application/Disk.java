package es.apa.downloadfilesample.application;

import java.io.File;
import java.io.IOException;

/**
 * Created by alberto on 18/7/16.
 */
public interface Disk {
    interface Accesor{
        void write(File path, byte[] data) throws IOException;
        byte[] read(File path) throws IOException;
    }
    interface State{
        boolean isExternalStorageWritable();
        boolean isExternalStorageReadable();
    }
}
