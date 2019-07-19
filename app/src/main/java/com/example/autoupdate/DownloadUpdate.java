package com.example.autoupdate;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class DownloadUpdate {

    public Context context;

    DownloadUpdate(Context context) {
        this.context = context;
        start();  // TODO don't waits start() method to finish
    }

    public void start() {
        delete_old_apk();
        String url = "http://cemonel.github.io/";
        url += context.getResources().getString(R.string.file_name);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("");
        request.setTitle(context.getResources().getString(R.string.file_name));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // need for sdk 6.0
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                context.getResources().getString(R.string.file_name));

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void delete_old_apk() {

        File file = new File(Environment.getExternalStorageDirectory() + "/download/" +
                context.getResources().getString(R.string.file_name));
        file.delete();
    }
}
