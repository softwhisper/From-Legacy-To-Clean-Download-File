package es.apa.downloadfilesample.infrastructure.network;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import es.apa.downloadfilesample.application.Download;

/**
 * Created by alberto on 7/8/16.
 */
public class DowloadFile implements Download.File {

    @Override
    public byte[] start(String url) throws IOException {
        URL URL = new URL(url);
        URLConnection ucon = URL.openConnection();
        InputStream is = ucon.getInputStream();
        byte[] content = ByteStreams.toByteArray(is);
        return content;
    }
}
