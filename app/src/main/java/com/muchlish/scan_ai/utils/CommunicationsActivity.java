/*
Bluetooth communications activity

Works with BluetoothConnection to provide simple interaction with a sever over a Bluetooth socket:
seek bar (slider) sends serialized values to server; activity checks for available responses from
server.

Copyright 2018  Gunnar Bowman, Emily Boyes, Trip Calihan, Simon D. Levy, Shepherd Sims

MIT License
*/

package com.muchlish.scan_ai.utils;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.muchlish.scan_ai.ActivityBluetoothDiscover;
import com.muchlish.scan_ai.R;
import com.muchlish.scan_ai.activity.main.MainView;

public abstract class CommunicationsActivity extends AppCompatActivity implements MainView, PermissionCallback, ErrorCallback, NavigationView.OnNavigationItemSelectedListener{


    private String mDeviceAddress;
    protected CommunicationsTask mBluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_scan);

        // Retrieve the address of the bluetooth device from the BluetoothListDeviceActivity
        Intent newint = getIntent();
        mDeviceAddress = newint.getStringExtra(ActivityBluetoothDiscover.EXTRA_ADDRESS);

        // Create a connection to this device
        mBluetoothConnection = new CommunicationsTask(this, mDeviceAddress);
        mBluetoothConnection.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothConnection.disconnect();
    }
}
