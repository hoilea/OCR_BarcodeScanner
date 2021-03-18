package com.muchlish.scan_ai.activity.singlescan;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

public interface SingleScanInterface {

    void onDetectedBarcode(Detector.Detections<Barcode> detections);
}
