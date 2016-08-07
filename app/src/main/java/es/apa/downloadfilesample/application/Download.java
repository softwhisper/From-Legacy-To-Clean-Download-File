package es.apa.downloadfilesample.application;

import java.io.IOException;

/**
 * Created by alberto on 7/8/16.
 */
public interface Download {
    interface File{
        byte[] start(String url) throws IOException;
    }
}
