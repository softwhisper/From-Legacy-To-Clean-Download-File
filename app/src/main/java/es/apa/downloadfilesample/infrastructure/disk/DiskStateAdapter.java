package es.apa.downloadfilesample.infrastructure.disk;

/**
 * Created by alberto on 18/7/16.
 */
public class DiskStateAdapter implements es.apa.downloadfilesample.application.Disk.State {


    private DiskState diskState;


    public DiskStateAdapter(DiskState diskState) {
        this.diskState = diskState;
    }


    @Override
    public boolean isExternalStorageWritable() {
        return diskState.isExternalStorageWritable();
    }


    @Override
    public boolean isExternalStorageReadable() {
        return diskState.isExternalStorageReadable();
    }
}
