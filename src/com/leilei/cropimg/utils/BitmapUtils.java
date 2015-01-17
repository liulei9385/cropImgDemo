package com.leilei.cropimg.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * USER: liulei
 * DATA: 2015/1/17
 * TIME: 14:37
 */
public class BitmapUtils {

    private BitmapUtils() {
    }

    public static Bitmap decodeBitmap(Resources resources, int resId, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);

        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) maxHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) maxWidth);
            }

            final float totalPixels = width * height;

            final float maxTotalPixels = maxWidth * maxHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > maxTotalPixels) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

}