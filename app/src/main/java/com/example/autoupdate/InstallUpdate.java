package com.example.autoupdate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class InstallUpdate {

    Context context;

    InstallUpdate(Context context) {
        this.context = context;

    }

    public void install() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File(Environment.getExternalStorageDirectory() + "/download/" +
                context.getResources().getString(R.string.file_name));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
