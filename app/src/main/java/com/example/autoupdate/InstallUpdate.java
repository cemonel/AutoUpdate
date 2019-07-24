package com.example.autoupdate;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class InstallUpdate {

    Context context;

    InstallUpdate(Context context) {
        this.context = context;

    }

    public static boolean installPackage(Context context, InputStream in, String packageName)
            throws IOException {
        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        params.setAppPackageName(packageName);
        // set params
        int sessionId = packageInstaller.createSession(params);
        PackageInstaller.Session session = packageInstaller.openSession(sessionId);
        OutputStream out = session.openWrite("Cem", 0, -1);
        byte[] buffer = new byte[65536];
        int c;
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
        session.fsync(out);
        in.close();
        out.close();

        session.commit(createIntentSender(context, sessionId));
        return true;
    }


    private static IntentSender createIntentSender(Context context, int sessionId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                new Intent("cm.android.intent.action.INSTALL_COMPLETE"), 0);
        return pendingIntent.getIntentSender();
    }

    public void install() {
        System.out.println("Install");
        try {
            installPackage(this.context, getInputStream(), "com.cem.defaultapp");
        }catch (Exception e){
            System.out.println("Error while installing package.");
            Log.e("Exception", "Error while installing package.");
        }
    }

    public InputStream getInputStream(){
        File file =  new File(Environment.getExternalStorageDirectory() + "/download/" +
                context.getResources().getString(R.string.file_name));
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        }
        catch (Exception e){
            Log.e("Exception", "Error while creating fileInputStream.");
            System.out.println("Error while creating fileInputStream.");
        }

        return  fileInputStream;
    }

    public void install2() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File(Environment.getExternalStorageDirectory() + "/download/" +
                context.getResources().getString(R.string.file_name));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
