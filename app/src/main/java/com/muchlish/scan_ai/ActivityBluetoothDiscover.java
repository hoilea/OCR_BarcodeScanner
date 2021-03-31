package com.muchlish.scan_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muchlish.scan_ai.activity.dashboard.DashboardActivity;
import com.muchlish.scan_ai.activity.singlescan.SingleScanActivity;

import java.util.ArrayList;
import java.util.Set;

public class ActivityBluetoothDiscover extends AppCompatActivity {
    ListView mDeviceList;
    private BluetoothAdapter mBluetoothAdapter = null;
    private Set<BluetoothDevice> mPairedDevices;

//    public static SharedPreferences sp_bluetooth;
//    private static final String SHARED_PREF_ID = "sharedPrefs";
//    private static final String TEXT = "text";
//    private String text ;


    public static String EXTRA_ADDRESS = "device_address";
    public static String CHECK_ID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_discover);
        mDeviceList = (ListView)findViewById(R.id.listView);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            if (mBluetoothAdapter.isEnabled()) {
                listPairedDevices();
            }
            else {
                //Ask to the user turn the bluetooth on
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }
        //b_id.setText("edvan");
        //loadData();
        //updateViews();
    }

    private void listPairedDevices() {
        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (mPairedDevices.size()>0)
        {
            for(BluetoothDevice bt : mPairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        mDeviceList.setAdapter(adapter);
        mDeviceList.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            String vchange = "1";
            // Make an intent to start next activity.
            Intent i = new Intent(ActivityBluetoothDiscover.this, DashboardActivity.class);
            //Change the activity.
            i.putExtra("bluethoot_address", address); //this will be received at CommunicationsActivity
            i.putExtra("check_id", vchange);
//            saveid(address);
            startActivity(i);
        }
    };

//    private void saveid(String _bluetoothAddress)
//    {
//        sp_bluetooth = getSharedPreferences(SHARED_PREF_ID,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp_bluetooth.edit();
//
//        editor.putString(TEXT,_bluetoothAddress);
//        editor.apply();
//    }
//
//    private void loadData()
//    {
//        sp_bluetooth = getSharedPreferences(SHARED_PREF_ID,MODE_PRIVATE);
//        text = sp_bluetooth.getString(TEXT,"no save bluetooth id");
//        updateViews(text);
//    }
//
//    private void updateViews(String _address)
//    {
//        Intent i = new Intent(ActivityBluetoothDiscover.this, DashboardActivity.class);
//        //Change the activity.
//        i.putExtra(EXTRA_ADDRESS, _address);
//    }


}