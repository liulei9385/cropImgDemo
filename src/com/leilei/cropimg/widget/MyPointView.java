package com.leilei.cropimg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

/**
 * USER: liulei
 * DATA: 2015/1/16
 * TIME: 16:00
 */
public class MyPointView extends View {

    private float[] location;
    private Paint paint;

    public MyPointView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MyPointView(Context context, float[] location) {
        this(context);
        this.location = location;
    }

    public MyPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(12);
        if (location == null)
            location = new float[]{0.0f, 0.0f};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint(location[0], location[1], paint);
    }

    @Override
    public String toString() {
        return "MyPointView{" +
                "location=" + Arrays.toString(location) +
                '}';
    }
}
