package es.apa.downloadfilesample.infrastructure.permissions;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Created by alberto on 22/7/16.
 */
public class Permissions {
    public interface Callback{
        void onPermissionGranted();
        void onPermissionDenied();
    }

    public static void check(String permissionName, final Callback callback){
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                callback.onPermissionGranted();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                callback.onPermissionDenied();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, permissionName);
    }
}
