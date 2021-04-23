package com.muchlish.scan_ai.activity.dashboard;

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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.muchlish.scan_ai.ActivityBluetoothDiscover;
import com.muchlish.scan_ai.utils.OcrCaptureActivity;
import com.muchlish.scan_ai.R;
import com.muchlish.scan_ai.activity.entity.MyResponse;
import com.muchlish.scan_ai.activity.entity.User;
import com.muchlish.scan_ai.activity.login.LoginActivity;
import com.muchlish.scan_ai.activity.singlescan.SingleScanActivity;
import com.muchlish.scan_ai.service.ApiClient;
import com.muchlish.scan_ai.service.AuthService;
import com.muchlish.scan_ai.utils.SharedUserPreferences;

import retrofit2.Call;
import retrofit2.Callback;

import static com.muchlish.scan_ai.ActivityBluetoothDiscover.CHECK_ID;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private PopupWindow mPopupWindow;
    protected PowerManager.WakeLock mWakeLock;

    private CardView singleactcv,dimension2d ;

    public static SharedUserPreferences sp;

    public static String EXTRA_ADDRESS = "device_address";
    private static final int RC_OCR_CAPTURE = 9003;

    public static SharedPreferences sp_bluetooth;
    private static final String SHARED_PREF_ID = "sharedPrefs";
    private static final String TEXT = "text";
    private static final String TEXT_ADDRESS = "text";
    private String text ;
    private TextView b_id;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        String bluetooth_address = getIntent().getStringExtra("bluethoot_address");
        String check_id = getIntent().getStringExtra("check_id");
        b_id = findViewById(R.id.Bluetooth_id);
        b_id.setText(check_id);
        if(b_id.getText().toString().equals("1"))
        {
            //b_id.setText(bluetooth_address);
            saveid(getIntent().getStringExtra("bluethoot_address"));
        }
        else
        {
            loadData();
            updateViews();
        }

        sp = new SharedUserPreferences(this);
        checkLogin();

//        b_id.setText(getIntent().getStringExtra("check_id"));




        drawerLayout = findViewById(R.id.topLayout);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        setNavigationView();

        singleactcv = findViewById(R.id.singleactcv);
        dimension2d = findViewById(R.id.dimension2d);
        //multiactcv = findViewById(R.id.multiactcv);
        //listdownloadactcv = findViewById(R.id.listdownloadactcv);

        //imgbluetooth = findViewById(R.id.img_bluetooth);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        singleactcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SingleChooseActivity.class);
//                startActivity(intent);
                if (b_id.getText().equals("no save bluetooth id"))
                {
                    Toast.makeText(DashboardActivity.this, "Please Select Bluetooth Device First", Toast.LENGTH_SHORT).show();
                }
                else if( b_id.getText().length() >15)
                {
                    Intent i = new Intent(DashboardActivity.this, SingleScanActivity.class);
                    //i.putExtra(OcrCaptureActivity.AutoFocus, true);
                    //Change the activity.
                    //i.putExtra(EXTRA_ADDRESS, b_id.getText()); //this will be received at CommunicationsActivity
                    i.putExtra("BluetoothAddress", b_id.getText());
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(DashboardActivity.this, "Please Select Bluetooth Device First", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        multiactcv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
//        listdownloadactcv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ListDownloadActivity.class);
//                startActivity(intent);
//            }
//        });

        //b_id.setText("edvan nys");
        //saveid("edvan nys");

        dimension2d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SingleChooseActivity.class);
//                startActivity(intent);
                if (b_id.getText().equals("no save bluetooth id"))
                {
                    Toast.makeText(DashboardActivity.this, "Please Select Bluetooth Device First", Toast.LENGTH_SHORT).show();
                }
                else if( b_id.getText().length() >15)
                {
                    Intent i = new Intent(DashboardActivity.this, SingleScanActivity.class);
                    //i.putExtra(OcrCaptureActivity.AutoFocus, true);
                    //Change the activity.
                    //i.putExtra(EXTRA_ADDRESS, b_id.getText()); //this will be received at CommunicationsActivity
                    i.putExtra("BluetoothAddress", b_id.getText());
                    i.putExtra("onedimension",false);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(DashboardActivity.this, "Please Select Bluetooth Device First", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
//            super.onBackPressed();
            finish();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.single_scan:
//                Intent singlescan=new Intent(this, SingleChooseActivity.class);
//                startActivity(singlescan);
//                Intent i = new Intent(DashboardActivity.this, SingleScanActivity.class);
//                i.putExtra(OcrCaptureActivity.AutoFocus, true);
//                //Change the activity.
//                i.putExtra(EXTRA_ADDRESS, b_id.getText()); //this will be received at CommunicationsActivity
//                startActivity(i);

                    Intent i = new Intent(DashboardActivity.this, SingleScanActivity.class);
                    //i.putExtra(OcrCaptureActivity.AutoFocus, true);
                    //Change the activity.
                    //i.putExtra(EXTRA_ADDRESS, b_id.getText()); //this will be received at CommunicationsActivity
                    i.putExtra("BluetoothAddress", b_id.getText());
                    startActivity(i);
                break;

            case R.id.bluetooth_setting:
                Intent intent = new Intent(getApplicationContext(), ActivityBluetoothDiscover.class);
                startActivity(intent);
                break;

            case R.id.homescanai:
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Information")
                        .setMessage("You are in Home Activity")
                        .setCancelable(false)
                        .setPositiveButton("Oke",null).create().show();
            case R.id.logout:
                sp.saveToken("");
                sp.setIsLogin(false);
                checkLogin();
        }
        drawerLayout.closeDrawer(GravityCompat.START);;
        return true;
    }
    public void setNavigationView(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = (TextView) headerView.findViewById(R.id.name);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        AuthService authService = ApiClient.getRetrofitInstance().create(AuthService.class);
        Call<MyResponse<User>> call = authService.getAccount("Bearer "+sp.getToken());
        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, retrofit2.Response<MyResponse<User>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    navName.setText(response.body().getData().getName());
                    navUsername.setText(response.body().getData().getUsername());
                }else{
                    Log.e(this.getClass().getName(), response.errorBody().toString());
//                    System.out.println(response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {
                Log.e(this.getClass().getName(), t.getLocalizedMessage());
            }
        });
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.10.248:7742/v1/users/account",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if(jsonObject.getBoolean("status")){
//                                JSONObject data = jsonObject.getJSONObject("data");
//                                navName.setText(data.getString("name"));
//                                navUsername.setText(data.getString("username"));
//                            }else{
//                                Toast.makeText(DashboardActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
//                                System.out.println(jsonObject.get("message").toString());
//                            }
//                            System.out.println(jsonObject.toString());
//                        } catch (JSONException e) {
//                            System.out.println("Error catch" + e.toString());
//                            System.out.println("Error resp\n" + response);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("Errornrs \n" + error.networkResponse);
//                        System.out.println("Errortstr \n" + error.toString());
//                        System.out.println("Errormsg \n" + error.getMessage());
//                        System.out.println("Errorjst \n" + error);
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", "Bearer "+DashboardActivity.sp.getToken());
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }
    public void checkLogin(){
        if(!sp.isLogin()){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
    private void saveid(String _bluetoothAddress)
    {
        sp_bluetooth = getSharedPreferences(SHARED_PREF_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_bluetooth.edit();

        editor.putString(TEXT_ADDRESS,_bluetoothAddress);
        editor.apply();
        loadData();
        updateViews();
    }

    private void loadData()
    {
        sp_bluetooth = getSharedPreferences(SHARED_PREF_ID,MODE_PRIVATE);
        text = sp_bluetooth.getString(TEXT_ADDRESS,"no save bluetooth id");

    }

    private void updateViews()
    {
        b_id.setText(text);
    }

}
