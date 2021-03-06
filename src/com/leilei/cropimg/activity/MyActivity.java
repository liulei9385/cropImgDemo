package com.leilei.cropimg.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.leilei.cropimg.R;
import com.leilei.cropimg.utils.BitmapUtils;
import com.leilei.cropimg.utils.FileUtils;
import com.leilei.cropimg.widget.CropImageView;

import java.io.File;

public class MyActivity extends ActionBarActivity {

    private CropImageView cropImageView;
    private int drawRes;
    private Bitmap sourceBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.main);

        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.drawable.ic_launcher);

        cropImageView = (CropImageView) findViewById(R.id.cropImg);
        drawRes = R.drawable.demo;


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sourceBitmap == null) {
            Resources resources = getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            int maxWidth = displayMetrics.widthPixels;
            int maxHeight = displayMetrics.heightPixels;
            sourceBitmap = BitmapUtils.decodeBitmap(resources, drawRes, maxWidth, maxHeight);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SupportMenuItem menuItem = (SupportMenuItem) menu.add(0, 2, 2, "crop");
        SupportMenuItem menuItem2 = (SupportMenuItem) menu.add(0, 1, 1, "draw");
        menuItem.setShowAsAction(SupportMenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem2.setShowAsAction(SupportMenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //crop
        if (item.getItemId() == 2) {
            showToast(item.toString(), true);
            Rect rect = cropImageView.getCoverBounds();
            cropImageView.drawPoints(new float[]{rect.left, rect.top});
            return true;
        } else if (item.getItemId() == 1) {
            //draw
            showToast(item.toString(), true);
            Rect rect = cropImageView.getCoverBounds();
            if (rect == null) {
                showToast("超出剪裁区域!", true);
                return false;
            }
            new Thread(new MyRunnable()).start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(final String text, boolean isLong) {
        int duration = Toast.LENGTH_SHORT;
        if (isLong)
            duration = Toast.LENGTH_LONG;

        final int duration2 = duration;
        if (Looper.getMainLooper().getThread() == Thread.currentThread())
            Toast.makeText(this, text, duration).show();
        else new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressWarnings("ResourceType")
            @Override
            public void run() {
                Toast.makeText(MyActivity.this, text, duration2).show();
            }
        });
    }

    private void drawCoverImage() {
        Rect cropRect = cropImageView.getCropBounds();
        Rect coverRect = cropImageView.getCoverBounds();
        if (coverRect == null) {
            showToast("超出剪裁区域!", true);
            return;
        }
        if (sourceBitmap != null) {
            int x = (int) (((float) coverRect.left / cropRect.right) * sourceBitmap.getWidth());
            int y = (int) (((float) coverRect.top / cropRect.bottom) * sourceBitmap.getHeight());
            int width = (int) ((((float) coverRect.right - coverRect.left) / cropRect.right) * sourceBitmap.getWidth());
            int height = (int) ((((float) coverRect.bottom - coverRect.top) / cropRect.bottom) * sourceBitmap.getHeight());
            System.out.println("MyActivity.drawCoverImage##" + x + "  " + y + " " + width + "  " + height);
            try {
                File externalCacheDir = this.getExternalCacheDir();
                if (externalCacheDir != null) {
                    String filePath = this.getExternalCacheDir().getAbsolutePath();
                    filePath += "/crop";
                    File file = new File(filePath);
                    if (!file.exists())
                        file.mkdirs();

                    Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap, x, y, width, height);
                    String name = getResources().getResourceEntryName(drawRes);
                    filePath += "/" + name + ".png";
                    boolean success = FileUtils.saveBitmapToFile(newBitmap, filePath);
                    if (success)
                        startActivity(new Intent(this, DisplayActivity.class).putExtra("filePath", filePath));

                }

            } catch (Exception e) {
                showToast(e.getMessage(), true);
            }
        }
    }

    public class MyRunnable implements Runnable {

        @Override
        public void run() {
            drawCoverImage();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sourceBitmap != null) {
            if (!sourceBitmap.isRecycled()) sourceBitmap.recycle();
            sourceBitmap = null;
        }
    }
}
