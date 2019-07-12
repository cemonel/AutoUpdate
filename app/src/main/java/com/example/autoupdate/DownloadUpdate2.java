package com.example.autoupdate;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

public class DownloadUpdate2 {

  public Context context;

  DownloadUpdate2(Context context) {
    this.context = context;
    new CheckPermission(context).request();
    start();  // TODO don't waits start() method to finish
  }

  public void start() {
    String url = "http://cemonel.github.io/new1.apk";
    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.setDescription("");
    request.setTitle("BranchApp.apk");
// in order for this if to run, you must use the android 3.2 to compile your app
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      request.allowScanningByMediaScanner();
      request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    }
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BranchApp.apk");

// get download service and enqueue file
    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    manager.enqueue(request);
  }
}
