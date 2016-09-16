package es.apa.downloadfilesample.global;

import android.app.Application;

import com.karumi.dexter.Dexter;

/**
 * Created by alberto on 6/8/16.
 */
public class AndroidApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
    }
}
