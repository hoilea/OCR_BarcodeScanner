package com.muchlish.scan_ai.activity.singlechooseactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.navigation.NavigationView;
import com.muchlish.scan_ai.ActivityBluetoothDiscover;
import com.muchlish.scan_ai.OcrCaptureActivity;
import com.muchlish.scan_ai.R;
import com.muchlish.scan_ai.activity.dashboard.DashboardActivity;
import com.muchlish.scan_ai.activity.listdownload.ListDownloadActivity;
import com.muchlish.scan_ai.activity.main.MainActivity;
import com.muchlish.scan_ai.activity.singlescan.SingleScanActivity;

import java.util.Set;

public class SingleChooseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private PopupWindow mPopupWindow;
    protected PowerManager.WakeLock mWakeLock;

    private CardView dimension1d,mocr;

    private static final int RC_OCR_CAPTURE = 9003;

    private Set<BluetoothDevice> mPairedDevices;

    public static String EXTRA_ADDRESS = "device_address";

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_choose);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        drawerLayout = findViewById(R.id.topLayout);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dimension1d = findViewById(R.id.dimension1d);
        mocr = findViewById(R.id.ocr);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        dimension1d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SingleScanActivity.class);
                intent.putExtra("dimension","One Dimension");
                intent.putExtra("onedimension",true);
                startActivity(intent);
            }
        });
        mocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SingleScanActivity.class);
//                intent.putExtra("dimension","Two Dimension");
//                intent.putExtra("onedimension",false);
//                startActivity(intent);

                Intent i = new Intent(SingleChooseActivity.this, OcrCaptureActivity.class);
                i.putExtra(OcrCaptureActivity.AutoFocus, true);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, "00:1A:7D:DA:71:07"); //this will be received at CommunicationsActivity
                startActivityForResult(i, RC_OCR_CAPTURE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
//                    statusMessage.setText(R.string.ocr_success);
//                    textValue.setText(text);
//                    Log.d(TAG, "Text read: " + text);
//                } else {
//                    statusMessage.setText(R.string.ocr_failure);
//                    Log.d(TAG, "No Text captured, intent data is null");
//                }
//            } else {
//                statusMessage.setText(String.format(getString(R.string.ocr_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
//            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_view_list_download:
                Intent listdownload=new Intent(this, ListDownloadActivity.class);
                startActivity(listdownload);
                break;
            case R.id.single_scan:
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleChooseActivity.this);
                builder.setTitle("Information")
                        .setMessage("You are in Single Scan Activity")
                        .setCancelable(false)
                        .setPositiveButton("Oke",null).create().show();
                break;
            case R.id.multi_scan:
                Intent singlescan=new Intent(this, MainActivity.class);
                startActivity(singlescan);
                break;
            case R.id.homescanai:
                Intent homescanai=new Intent(this, DashboardActivity.class);
                startActivity(homescanai);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);;
        return true;
    }
}
