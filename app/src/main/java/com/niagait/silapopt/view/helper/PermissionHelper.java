package com.niagait.silapopt.view.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    private Activity mActivity;
    private final int REQUEST_PERMISSION = 99;

    public PermissionHelper(Activity activity) {
        mActivity = activity;
    }

    public boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCamera = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
            int permissionReadStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWriteStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            List<String> listPermissionNeeded = new ArrayList<>();

            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(Manifest.permission.CAMERA);
            }

            if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!listPermissionNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(mActivity, listPermissionNeeded.toArray(new String[0]), REQUEST_PERMISSION);
                return false;
            }
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return true;
    }
}
