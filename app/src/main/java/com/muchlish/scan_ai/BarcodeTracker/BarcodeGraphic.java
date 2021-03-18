package com.muchlish.scan_ai.BarcodeTracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.android.gms.vision.barcode.Barcode;
import com.muchlish.scan_ai.ui.camera.GraphicOverlay;

public class BarcodeGraphic extends GraphicOverlay.Graphic {


    private int mId;

    private static final int COLOR_CHOICES[] = {
            Color.WHITE,
    };

    private static int mCurrentColorIndex = 0;

    private Paint mRectPaint;
    private Paint mTextPaint;
    private volatile Barcode mBarcode;

    private Context mContext;

    public BarcodeGraphic(GraphicOverlay overlay, Context context) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mContext = context;

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.parseColor("#8862D18F"));
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setStrokeWidth(4.0f);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#121212"));
        mTextPaint.setTextSize(26.0f);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Barcode getBarcode() {
        return mBarcode;
    }

    /**
     * Updates the barcode instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateItem(Barcode barcode) {
        mBarcode = barcode;
        postInvalidate();
    }


    /**
     * Draws the barcode annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Barcode barcode = mBarcode;
        if (barcode == null) {
            return;
        }
        RectF rect = new RectF(barcode.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);

        mRectPaint.setAntiAlias(true);
        if (barcode.format == Barcode.CODE_128 || barcode.format == Barcode.CODE_39 || barcode.format == Barcode.CODE_93 || barcode.format == Barcode.EAN_13 || barcode.format == Barcode.UPC_A) {
            float rectp = rect.bottom - rect.top;
            if(rectp>300f){
                RectF rect1 = new RectF(barcode.getBoundingBox());
                rect1.left = rect.left;
                rect1.top = rect.top-10f;
                rect1.right = rect.right;
                rect1.bottom = rect.top+100f;
                canvas.drawRect(rect1, mRectPaint);

                RectF rect2 = new RectF(barcode.getBoundingBox());
                rect2.left = rect.left;
                rect2.bottom = rect.bottom+10f;
                rect2.right = rect.right;
                rect2.top = rect.bottom-100f;
                canvas.drawRect(rect2, mRectPaint);
            }else{
                canvas.drawRect(rect, mRectPaint);
            }
        }else{
            canvas.drawRect(rect, mRectPaint);

        }


    }

}
