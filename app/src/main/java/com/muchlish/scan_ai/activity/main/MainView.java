package com.muchlish.scan_ai.activity.main;

import androidx.annotation.UiThread;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

public interface MainView {
    @UiThread
    void onBarcodeDetected(Barcode barcode);
    @UiThread
    void onAllBarcodeDetected(Detector.Detections<Barcode> detections);
}
