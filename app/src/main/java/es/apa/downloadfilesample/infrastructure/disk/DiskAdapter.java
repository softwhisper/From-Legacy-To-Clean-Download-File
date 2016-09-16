package es.apa.downloadfilesample.infrastructure.disk;

import java.io.File;
import java.io.IOException;

/**
 * Created by alberto on 18/7/16.
 */
public class DiskAdapter implements es.apa.downloadfilesample.application.Disk.Accesor {


    private Disk disk;


    public DiskAdapter(Disk disk) {
        this.disk = disk;
    }


    @Override
    public void write(File path, byte[] data) throws IOException {
        disk.write(path, data);
    }


    @Override
    public byte[] read(File path) throws IOException {
        throw new UnsupportedOperationException("Not implemented!!!");
    }
}
