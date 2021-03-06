package com.leilei.cropimg.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.leilei.cropimg.R;

/**
 * USER: liulei
 * DATA: 2015/1/16
 * TIME: 15:30
 */
public class DisplayActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Display");

        imageView = new ImageView(this);
        String filePath = getIntent().getStringExtra("filePath");
        if (filePath != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        } else imageView.setImageResource(R.drawable.demo);

        setContentView(imageView);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
