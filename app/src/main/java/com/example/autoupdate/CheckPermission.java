package com.example.autoupdate;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class CheckPermission {

  private static final int REQUEST = 112;
  Context context;

  CheckPermission(Context context) {
    this.context = context;
  }

  public static boolean hasPermissions(Context context, String... permissions) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }

  public void request() {

    if (Build.VERSION.SDK_INT >= 23) {
      String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
      if (!hasPermissions(context, PERMISSIONS)) {
        ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, REQUEST);
      } else {
        //do here // TODO handle here
      }
    } else {
      //don't need for pepper
    }
  }
}
