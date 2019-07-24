package com.example.autoupdate;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    CheckPermission checkPermission;
    CheckUpdate checkUpdate;
    public static final String TAG = "Device:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                devicePolicyManager.clearDeviceOwnerApp(context.getPackageName());
                System.out.println("Device Admin Deleted");
            }
        });

        //  CHECKS PERMISSIONS HERE
        checkUpdate = new CheckUpdate(this);
        checkPermission = new CheckPermission(context);
        if (checkPermission.hasPermissions()) {
            checkUpdate.run();
        } else {
            checkPermission.request();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 112: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkUpdate.run();
                }
                return;
            }
        }
    }

    public class DevAdminReceiver extends DeviceAdminReceiver{

        @Override
        public void onEnabled(Context context, Intent intent){
            super.onEnabled(context, intent);
            Log.d(TAG, "Device owner Enabled");
        }
    }

}
