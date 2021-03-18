package com.muchlish.scan_ai.BarcodeTracker;

import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;

public class MyDetector extends Detector<Barcode> {
    private Detector<Barcode> mDelegate;

    public MyDetector(Detector<Barcode> delegate) {
        mDelegate = delegate;
    }

    @Override
    public SparseArray<Barcode> detect(Frame frame) {
        return null;
    }
    public boolean isOperational() {
        return mDelegate.isOperational();
    }

    public boolean setFocus(int id) {
        return mDelegate.setFocus(id);
    }
}
