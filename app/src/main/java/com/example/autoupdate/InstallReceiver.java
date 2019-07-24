package com.example.autoupdate;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.cem.defaultapp",
                "com.cem.defaultapp"));
        context.startActivity(intent);
    }
}
