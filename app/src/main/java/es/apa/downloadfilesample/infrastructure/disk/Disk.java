package es.apa.downloadfilesample.infrastructure.disk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by alberto on 12/6/16.
 */
public class Disk {

    public synchronized void write(File path, byte[] data) throws IOException {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.write(data);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public synchronized byte[] read(File path) throws IOException {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString().getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
