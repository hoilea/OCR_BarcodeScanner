package com.muchlish.scan_ai.activity.singlescan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.muchlish.scan_ai.BarcodeTracker.BarcodeGraphic;
import com.muchlish.scan_ai.BarcodeTracker.BarcodeGraphicTracker;
import com.muchlish.scan_ai.BarcodeTracker.BarcodeTrackerFactory;
import com.muchlish.scan_ai.BarcodeTracker.MyDetector;
import com.muchlish.scan_ai.R;
import com.muchlish.scan_ai.activity.dashboard.DashboardActivity;
import com.muchlish.scan_ai.activity.entity.MyResponse;
import com.muchlish.scan_ai.activity.listdownload.ListDownloadActivity;
import com.muchlish.scan_ai.activity.main.MainActivity;
import com.muchlish.scan_ai.activity.main.MainAdapterScan;
import com.muchlish.scan_ai.activity.main.MainPresenter;
import com.muchlish.scan_ai.activity.main.MainView;
import com.muchlish.scan_ai.activity.singlechooseactivity.SingleChooseActivity;
import com.muchlish.scan_ai.service.ApiClient;
import com.muchlish.scan_ai.service.BarcodeDataService;
import com.muchlish.scan_ai.ui.camera.CameraSource;
import com.muchlish.scan_ai.ui.camera.CameraSourcePreviewSingleScan;
import com.muchlish.scan_ai.ui.camera.GraphicOverlay;
import com.muchlish.scan_ai.utils.SharedUserPreferences;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class SingleScanActivity  extends  CommunicationsActivity {
    private static final String TAG = "Multiple Scan";

    private static final int RC_HANDLE_GMS = 9001;

    private static final int RC_HANDLE_CAMERA_PERM = 2;
    public final static int MESSAGE_READ = 2;
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data


    public static final String AutoFocusS = "AutoFocus";
    public static final boolean AutoFocus = true;
    public static final boolean UseFlash = false;
    public static final String BarcodeObject = "Barcode";

    private CameraSource mCameraSource;
    private CameraSourcePreviewSingleScan mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private MainPresenter mainPresenter;
    private MainAdapterScan adapterScan;
    private RecyclerView.Adapter adapter;
    private MainAdapterScan.ItemClickListener itemClickListener;

    private DrawerLayout drawerLayout;
    public static ArrayList<String> datescan = new ArrayList<>();
    public static ArrayList<String> barcodes = new ArrayList<>();
    public static ArrayList<String> barcodes2 = new ArrayList<>();
    public static ArrayList<String> type_code = new ArrayList<>();

    private PopupWindow mPopupWindow;
    private PopupWindow mPopupWindowASS;
    protected PowerManager.WakeLock mWakeLock;

    private static final int REQUEST_PERMISSIONS = 20;

    private boolean previewScanStatus,afterSingleScan,timescanruning,onedimension;
    protected TextView typecodetv,valuecodetv,asstypecode,assvaluecode;
    private String sessionCode = "";
    private View rectanglescan,aftersinglescanbg;
    LinearLayout aftersinglescancontent,seelist,continuescan;
    private Timer timer;
    public static TextView descriptioncode;
    private ProgressBar progressDescription;
    public static SharedUserPreferences sp;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_scan);
        previewScanStatus = false;
        afterSingleScan = false;
        timescanruning = false;
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        String descsinglescan = getIntent().getStringExtra("dimension");
        onedimension = getIntent().getBooleanExtra("onedimension",true);

        asstypecode = findViewById(R.id.asstypecode);
        assvaluecode = findViewById(R.id.assvaluecode);
        rectanglescan = findViewById(R.id.rectanglescan);
        typecodetv = findViewById(R.id.typecodetv);
        valuecodetv = findViewById(R.id.valuecodetv);
        descriptioncode = findViewById(R.id.descriptioncode);
        progressDescription = findViewById(R.id.progressDescription);
        new AskPermission.Builder(this).setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).setCallback(this).setErrorCallback(this).request(REQUEST_PERMISSIONS);

        Toolbar toolbar = findViewById(R.id.toolbar);
        sp = new SharedUserPreferences(this);

        drawerLayout = findViewById(R.id.topLayout);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        timer = new Timer();
        mPreview = findViewById(R.id.preview);
        mGraphicOverlay = findViewById(R.id.graphicOverlay);


        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(AutoFocus, UseFlash);
        } else {
            requestCameraPermission();
        }

//        Snackbar.make(mGraphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
//                Snackbar.LENGTH_LONG)
//                .show();

        mainPresenter = new MainPresenter(this);

        aftersinglescanbg = findViewById(R.id.aftersinglescanbg);
        aftersinglescancontent = findViewById(R.id.aftersinglescancontent);
        seelist = (LinearLayout) findViewById(R.id.seelist);
        continuescan = (LinearLayout) findViewById(R.id.continuescan);

        seelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewScan(drawerLayout);
            }
        });
        continuescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aftersinglescanbg.setVisibility(View.GONE);
                aftersinglescancontent.setVisibility(View.GONE);
                rectanglescan.setVisibility(View.VISIBLE);
                afterSingleScan = false;
            }
        });

        typecodetv.setText(descsinglescan);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(previewScanStatus){
            mPopupWindow.dismiss();
            previewScanStatus = false;
            rectanglescan.setVisibility(View.VISIBLE);
            typecodetv.setVisibility(View.VISIBLE);
            valuecodetv.setVisibility(View.VISIBLE);
            descriptioncode.setVisibility(View.VISIBLE);
        }else{
//            Intent intent = new Intent(this, DashboardActivity.class);
//            startActivity(intent);
            super.onBackPressed();
        }
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        drawerLayout.setOnClickListener(listener);
        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();
        int barcodeformat = 0;
        if(onedimension){
            barcodeformat = Barcode.EAN_13 | Barcode.EAN_8 | Barcode.UPC_A | Barcode.UPC_E | Barcode.CODE_39 | Barcode.CODE_93 | Barcode.CODE_128 | Barcode.ITF | Barcode.CODABAR;
            System.out.println("1D-a : "+barcodeformat);
        }else{
            barcodeformat = Barcode.QR_CODE | Barcode.DATA_MATRIX | Barcode.PDF417 | Barcode.AZTEC;
            System.out.println("2D-b : "+barcodeformat);
        }
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(barcodeformat).build();
        MyDetector myDetector = new MyDetector(barcodeDetector);
        BarcodeGraphic barcodeGraphic = new BarcodeGraphic(mGraphicOverlay,this);
        BarcodeGraphicTracker barcodeGraphicTracker = new BarcodeGraphicTracker(mGraphicOverlay,barcodeGraphic,this);
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());
//        barcodeDetector.setProcessor(new CentralBarcodeFocusingProcessor(barcodeDetector,barcodeGraphicTracker));
//        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
//                if(barcodeSparseArray.size()>0) {
//                    Barcode barcode = barcodeSparseArray.valueAt(0);
//                    if(!afterSingleScan){
//                        int check = 0;
//                        for(int i=0;i<barcodes.size();i++){
//                            if(barcode.displayValue.equals(barcodes.get(i))){
//                                check = 1;
//                                break;
//                            }
//                        }
//                        if(check==0){
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            String currentDateandTime = sdf.format(new Date());
//                            barcodes.add(barcode.displayValue);
//                            type_code.add(mainPresenter.decodeFormat(barcode.format));
//                            datescan.add(currentDateandTime);
//                        }
//                        typecodetv.setText(mainPresenter.decodeFormat(barcode.format));
//                        valuecodetv.setText(barcode.displayValue);
//                        System.out.println(barcode.displayValue+" | "+sessionCode);
//                        if(!barcode.displayValue.equals(sessionCode)){
//                            final MediaPlayer tapfinger = MediaPlayer.create(SingleScanActivity.this,R.raw.scanner_beep);
//                            tapfinger.start();
//                            runOnUiThread(new Runnable() {
//                                              //
//                                              @Override
//                                              public void run() {
//
//                                                  cekDB(barcode.displayValue);
//                                              }
//                                          });
//                            sessionCode = barcode.displayValue;
//                        }
//                        afterSingleScan=true;
//                    }else{
//                        if(!timescanruning && !barcode.displayValue.equals(sessionCode)){
//                            System.out.println(timer.purge());
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    afterSingleScan=false;
//                                    timescanruning=false;
//                                }
//                            },500);
//                            timescanruning=true;
//                        }
//                    }
//                }
////                runOnUiThread(new Runnable() {
////
////                    @Override
////                    public void run() {
////
////                        // Stuff that updates the UI
////                        final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
////                        if(barcodeSparseArray.size()>0){
////                            Barcode barcode = barcodeSparseArray.valueAt(0);
////                            if(!afterSingleScan){
////                                int check = 0;
////                                for(int i=0;i<barcodes.size();i++){
////                                    if(barcode.displayValue.equals(barcodes.get(i))){
////                                        check = 1;
////                                        break;
////                                    }
////                                }
////                                if(check==0){
////                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                                    String currentDateandTime = sdf.format(new Date());
////                                    barcodes.add(barcode.displayValue);
////                                    type_code.add(mainPresenter.decodeFormat(barcode.format));
////                                    datescan.add(currentDateandTime);
////                                }
//////                    typecodetv.setText(mainPresenter.decodeFormat(barcode.format));
//////                    valuecodetv.setText(barcode.displayValue);
////                                System.out.println(barcode.displayValue+" | "+sessionCode);
//////                                if(!barcode.displayValue.equals(sessionCode) || !afterSingleScan){
////                                    final MediaPlayer tapfinger = MediaPlayer.create(SingleScanActivity.this,R.raw.scanner_beep);
////                                    tapfinger.start();
////                                    sessionCode = barcode.displayValue;
//////                                }
////                                asstypecode.setText(mainPresenter.decodeFormat(barcode.format));
////                                assvaluecode.setText(barcode.displayValue);
////                                rectanglescan.setVisibility(View.GONE);
////                                afterSingleScan = true;
////
////                                aftersinglescanbg.setVisibility(View.VISIBLE);
////                                aftersinglescancontent.setVisibility(View.VISIBLE);
////                            }
////                            System.out.println(afterSingleScan);
////                        }
////
////                    }
////                });
//            }
//        });

        if (!barcodeDetector.isOperational()) {

            Log.w(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(getWallpaperDesiredMinimumHeight(), getWallpaperDesiredMinimumWidth())
                .setRequestedFps(60.0f);

        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    private void cekDB(String valuecode) {
        progressDescription.setVisibility(View.VISIBLE);
        descriptioncode.setVisibility(View.GONE);
        BarcodeDataService barcodeDataService = ApiClient.getRetrofitInstance().create(BarcodeDataService.class);
        Call<MyResponse<com.muchlish.scan_ai.activity.entity.Barcode>> call = barcodeDataService.findBarcode("Bearer "+sp.getToken(),valuecode);
        call.enqueue(new Callback<MyResponse<com.muchlish.scan_ai.activity.entity.Barcode>>() {
            @Override
            public void onResponse(Call<MyResponse<com.muchlish.scan_ai.activity.entity.Barcode>> call, retrofit2.Response<MyResponse<com.muchlish.scan_ai.activity.entity.Barcode>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().isSuccess()){
                        SingleScanActivity.descriptioncode.setText(response.body().getData().getDesc());
                        progressDescription.setVisibility(View.GONE);
                        descriptioncode.setVisibility(View.VISIBLE);
                        //for (byte b : String.valueOf(valuecode+"\n").getBytes()) {
                        //    mBluetoothConnection.write(b);
                        //}
                        if(mConnectedThread != null) //First check to make sure thread created
                            mConnectedThread.write(valuecode+"\n");
                        Toast.makeText(getApplicationContext(), valuecode, Toast.LENGTH_LONG).show();
                    }else{
                        progressDescription.setVisibility(View.GONE);
                        descriptioncode.setVisibility(View.VISIBLE);
                        SingleScanActivity.descriptioncode.setText("Data not found in database");
                    }
                }else{
                    progressDescription.setVisibility(View.GONE);
                    descriptioncode.setVisibility(View.VISIBLE);
                    Log.e(this.getClass().getName(), "errorbody:"+response.errorBody().toString());
                    System.out.println("detail:"+response.body());
//                    SingleScanActivity.descriptioncode.setText("Data not found in database");
                }
            }

            @Override
            public void onFailure(Call<MyResponse<com.muchlish.scan_ai.activity.entity.Barcode>> call, Throwable t) {
                progressDescription.setVisibility(View.GONE);
                descriptioncode.setVisibility(View.VISIBLE);
                Log.e(this.getClass().getName(),"onfailure:"+t.getLocalizedMessage());
            }
        });
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://uberando.com/test/scanbarcode.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            SingleScanActivity.descriptioncode.setText(jsonObject.getString("desc"));
//                            progressDescription.setVisibility(View.GONE);
//                            descriptioncode.setVisibility(View.VISIBLE);
////                            Toast.makeText(SingleScanActivity.this, "Data Found", Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
////                            Toast.makeText(SingleScanActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
//                            progressDescription.setVisibility(View.GONE);
//                            descriptioncode.setVisibility(View.VISIBLE);
//                            SingleScanActivity.descriptioncode.setText("Data not found in database");
//                            e.printStackTrace();
//                            System.out.println("Error catch"+e.toString());
//                            System.out.println("Error resp\n"+response);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SingleScanActivity.this, "Data Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
//                        System.out.println("Errornrs \n"+error.networkResponse);
//                        System.out.println("Errortstr \n"+error.toString());
//                        System.out.println("Errormsg \n"+error.getMessage());
//                        System.out.println("Errorjst \n"+error);
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("barcode",valuecode);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource(AutoFocus, UseFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }
    //@Override
    public void onDestroy() {
        this.mWakeLock.release();
        if (mPreview != null) {
            mPreview.release();
        }
        super.onDestroy();
    }
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    @Override
    public void onBarcodeDetected(Barcode barcode) {
//        System.out.println("Detected");
//        int check = 0;
//        for(int i=0;i<barcodes.size();i++){
//            if(barcode.displayValue.equals(barcodes.get(i))){
//                check = 1;
//                break;
//            }
//        }
//        if(check==0){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String currentDateandTime = sdf.format(new Date());
//            barcodes.add(barcode.displayValue);
//            type_code.add(mainPresenter.decodeFormat(barcode.format));
//            datescan.add(currentDateandTime);
//        }
//        typecodetv.setText(mainPresenter.decodeFormat(barcode.format));
//        valuecodetv.setText(barcode.displayValue);
//        final MediaPlayer tapfinger = MediaPlayer.create(this,R.raw.scanner_beep);
//        tapfinger.start();
    }

    @Override
    public void onAllBarcodeDetected(Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
        if(barcodeSparseArray.size()>0) {
            Barcode barcode = barcodeSparseArray.valueAt(0);
            if(!afterSingleScan){
                int check = 0;
                for(int i=0;i<barcodes.size();i++){
                    if(barcode.displayValue.equals(barcodes.get(i))){
                        check = 1;
                        break;
                    }
                }
                if(check==0){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    barcodes.add(barcode.displayValue);
                    type_code.add(mainPresenter.decodeFormat(barcode.format));
                    datescan.add(currentDateandTime);
                }
                typecodetv.setText(mainPresenter.decodeFormat(barcode.format));
                valuecodetv.setText(barcode.displayValue);
                System.out.println(barcode.displayValue+" | "+sessionCode);
                if(!barcode.displayValue.equals(sessionCode)){
                    final MediaPlayer tapfinger = MediaPlayer.create(SingleScanActivity.this,R.raw.scanner_beep);
                    tapfinger.start();
                    runOnUiThread(new Runnable() {
                        //
                        @Override
                        public void run() {

                            cekDB(barcode.displayValue);
                        }
                    });
                    sessionCode = barcode.displayValue;
                }
                afterSingleScan=true;
            }else{
                if(!timescanruning && !barcode.displayValue.equals(sessionCode)){
                    System.out.println(timer.purge());
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            afterSingleScan=false;
                            timescanruning=false;
                        }
                    },500);
                    timescanruning=true;
                }
            }
        }
    }

    public void PreviewScan(View view) {
        rectanglescan.setVisibility(View.INVISIBLE);
        typecodetv.setVisibility(View.INVISIBLE);
        valuecodetv.setVisibility(View.INVISIBLE);
        descriptioncode.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(LAYOUT_INFLATER_SERVICE);
        previewScanStatus = true;
        View customView = inflater.inflate(R.layout.popup_layout,null);
        mPopupWindow = new PopupWindow(
                customView,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        recyclerView = customView.findViewById(R.id.recycler_view);
        swipeRefresh = customView.findViewById(R.id.swipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterScan = new MainAdapterScan(this,itemClickListener,datescan,barcodes,type_code);
        adapterScan.notifyDataSetChanged();
        recyclerView.setAdapter(adapterScan);
        swipeRefresh.setOnRefreshListener(()->{
            swipeRefresh.setRefreshing(true);
            adapterScan = new MainAdapterScan(this,itemClickListener,datescan,barcodes,type_code);
            adapterScan.notifyDataSetChanged();
            recyclerView.setAdapter(adapterScan);
            swipeRefresh.setRefreshing(false);
        });
        ImageButton closeButton = customView.findViewById(R.id.ib_close);
        Button closeButton_btn = customView.findViewById(R.id.ib_close_btn);
        Button resetButton_btn = customView.findViewById(R.id.ib_reset_btn);
        Button download_excel = customView.findViewById(R.id.download_excel);

        download_excel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(barcodes.size()>0){
                    try {
                        File myFile = new File(Environment.getExternalStorageDirectory()+"/DataBarcode");
                        if(!myFile.exists()){
                            myFile.mkdirs();
                        }
                        HSSFWorkbook workbook = new HSSFWorkbook();
                        HSSFSheet sheet = workbook.createSheet("List Product");
                        Row rowHeading = sheet.createRow(0);
                        rowHeading.createCell(0).setCellValue("No.");
                        rowHeading.createCell(1).setCellValue("Time");
                        rowHeading.createCell(2).setCellValue("Value");
                        rowHeading.createCell(3).setCellValue("Type");
                        for(int i = 0; i<4; i++){
                            CellStyle styleRowHeading = workbook.createCellStyle();
                            Font font = workbook.createFont();
                            font.setBold(true);
                            font.setFontName(HSSFFont.FONT_ARIAL);
                            font.setFontHeightInPoints((short)11);
                            styleRowHeading.setFont(font);
                            styleRowHeading.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                            rowHeading.getCell(i).setCellStyle(styleRowHeading);
                        }
                        for(int i = 0; i<barcodes.size();i++){
                            Row row = sheet.createRow(1+i);
                            row.createCell(0).setCellValue(i+1);
                            row.createCell(1).setCellValue(datescan.get(i));
                            row.createCell(2).setCellValue(barcodes.get(i));
                            row.createCell(3).setCellValue(type_code.get(i));

                            sheet.setColumnWidth(0,(""+i).length()*150+100*11);
                            sheet.setColumnWidth(1,datescan.get(i).length()*200+100*11);
                            sheet.setColumnWidth(2,longestString(barcodes).length()*210+100*11);
                            sheet.setColumnWidth(3,longestString(type_code).length()*210+100*11);
                            sheet.setDefaultRowHeight((short) 400);
                            for(int b=0;b<4;b++){
                                CellStyle styleRow = workbook.createCellStyle();
                                styleRow.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                                row.getCell(b).setCellStyle(styleRow);

                            }
                        }
                        if(barcodes.toString()!=barcodes2.toString()){
                            File listbarcodefile = new File(myFile,"listbarcode_"+
                                    Calendar.getInstance().getTime().getTime()+
                                    ".xls");
                            FileOutputStream out = new FileOutputStream(listbarcodefile);
                            workbook.write(out);
                            out.close();
                            workbook.close();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleScanActivity.this);
                            builder.setTitle("Information")
                                    .setMessage("File has been saved in "+myFile+"\nOr as we know in InternalMemory/Databarcode \nOpen File?")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Uri path = FileProvider.getUriForFile(SingleScanActivity.this,SingleScanActivity.this.getApplicationContext().getPackageName()+".provider",listbarcodefile);
                                            Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
                                            pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            pdfOpenIntent.setDataAndType(path, "application/vnd.ms-excel");
                                            startActivity(pdfOpenIntent);
                                        }
                                    })
                                    .create().show();
                            System.out.println("created on "+Environment.getExternalStorageDirectory());

                            barcodes2.addAll(barcodes);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleScanActivity.this);
                            builder.setTitle("Information")
                                    .setMessage("You have been download this data!")
                                    .setCancelable(false)
                                    .setPositiveButton("Oke",null).create().show();
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found "+e);
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("IO Exception "+e);
                        e.printStackTrace();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SingleScanActivity.this);
                    builder.setTitle("Information")
                            .setMessage("No Barcode has been scanned")
                            .setCancelable(false)
                            .setPositiveButton("Oke",null).create().show();
                    System.out.println("No barcode has been scanned");
                }
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                previewScanStatus = false;
//                if(!afterSingleScan){
                    rectanglescan.setVisibility(View.VISIBLE);
                    typecodetv.setVisibility(View.VISIBLE);
                    valuecodetv.setVisibility(View.VISIBLE);
                descriptioncode.setVisibility(View.VISIBLE);
//                }
            }
        });
        closeButton_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                previewScanStatus = false;
//                if(!afterSingleScan){
                    rectanglescan.setVisibility(View.VISIBLE);
                    typecodetv.setVisibility(View.VISIBLE);
                    valuecodetv.setVisibility(View.VISIBLE);
                descriptioncode.setVisibility(View.VISIBLE);
//                }
            }
        });
        resetButton_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodes.clear();
                datescan.clear();
                type_code.clear();
                swipeRefresh.setRefreshing(true);
                adapterScan = new MainAdapterScan(SingleScanActivity.this,itemClickListener,datescan,barcodes,type_code);
                adapterScan.notifyDataSetChanged();
                recyclerView.setAdapter(adapterScan);
                swipeRefresh.setRefreshing(false);
            }
        });

        mPopupWindow.showAtLocation(drawerLayout, Gravity.CENTER,0,0);
    }
    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String[] files = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
    public String longestString(ArrayList<String> array){
        int index = 0;
        int elementLength = array.get(0).length();
        for(int i=1; i< array.size(); i++) {
            if(array.get(i).length() > elementLength) {
                index = i; elementLength = array.get(i).length();
            }
        }
        return array.get(index);
    }
    @Override
    public void onShowRationalDialog(PermissionInterface permissionInterface, int requestCode) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
    }
    @Override
    public void onShowSettings(PermissionInterface permissionInterface, int requestCode) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("We need Permissions for this app. Open Setting screen?");
        builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }
    @Override
    public void onPermissionsGranted(int requestCode) {

    }
    @Override
    public void onPermissionsDenied(int requestCode) {

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_view_list_download:
                Intent listdownload=new Intent(this, ListDownloadActivity.class);
                startActivity(listdownload);
                break;
            case R.id.single_scan:
                Intent singlechoose=new Intent(this, SingleChooseActivity.class);
                startActivity(singlechoose);
                break;
            case R.id.multi_scan:
                Intent multiscan=new Intent(this, MainActivity.class);
                startActivity(multiscan);
                break;
            case R.id.homescanai:
                Intent homescanai=new Intent(this, DashboardActivity.class);
                startActivity(homescanai);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);;
        return true;
    }
    public void AfterSingleScanGagal(View view) {
        afterSingleScan = true;
        rectanglescan.setVisibility(View.GONE);
        System.out.println("lol pusing");
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.aftersinglescan, null);
        mPopupWindowASS = new PopupWindow(
                customView,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindowASS.setElevation(4.0f);
        }

        LinearLayout seelist = (LinearLayout) customView.findViewById(R.id.seelist);
        LinearLayout continuescan = (LinearLayout) customView.findViewById(R.id.continuescan);
        ImageButton close = customView.findViewById(R.id.ib_close);
        seelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        continuescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowASS.dismiss();
                afterSingleScan = false;
                rectanglescan.setVisibility(View.VISIBLE);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowASS.dismiss();
                afterSingleScan = false;
                rectanglescan.setVisibility(View.VISIBLE);

            }
        });
        mPopupWindowASS.showAtLocation(drawerLayout, Gravity.CENTER,0,0);
    }

}
