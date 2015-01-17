package com.leilei.cropimg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.leilei.cropimg.utils.FileUtils;
import com.leilei.cropimg.widget.CropImageView;

import java.io.File;

public class MyActivity extends Activity {

    private CropImageView cropImageView;
    private int drawRes;
    private Bitmap sourceBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        cropImageView = (CropImageView) findViewById(R.id.cropImg);
        drawRes = R.drawable.demo;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sourceBitmap == null) {
            sourceBitmap = BitmapFactory.decodeResource(getResources(), drawRes);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, 2, 2, "crop");
        MenuItem menuItem2 = menu.add(0, 1, 1, "draw");
        if (Build.VERSION.SDK_INT >= 11) {
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //crop
        if (item.getItemId() == 2) {
            showToast(item.toString(), true);
            return true;
        } else if (item.getItemId() == 1) {
            //draw
            showToast(item.toString(), true);
            Rect rect = cropImageView.getCoverBounds();
            if (rect == null) {
                showToast("超出剪裁区域!", true);
                return false;
            }
            cropImageView.drawPoints(new float[]{rect.left, rect.top});
            cropImageView.drawPoints(new float[]{rect.right, rect.bottom});
            new Thread(new MyRunnable()).start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String text, boolean isLong) {
        int duration = Toast.LENGTH_SHORT;
        if (isLong)
            duration = Toast.LENGTH_LONG;
        Toast.makeText(this, text, duration).show();
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
        System.out.println("MyActivity.onDestroy##");
        if (sourceBitmap != null) {
            if (!sourceBitmap.isRecycled()) sourceBitmap.recycle();
            sourceBitmap = null;
        }
    }
}
