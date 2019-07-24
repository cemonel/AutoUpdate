package com.example.autoupdate;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DevAdminReceiver extends DeviceAdminReceiver {

    public String TAG = "DEVADMIN";

    @Override
    public void onEnabled(Context context, Intent intent){
        super.onEnabled(context, intent);
        Log.d(TAG, "Device Owner Enabled");
    }
}
