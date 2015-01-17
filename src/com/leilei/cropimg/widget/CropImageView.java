package com.leilei.cropimg.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.leilei.cropimg.R;

import java.util.LinkedList;

/**
 * USER: liulei
 * DATA: 2015/1/15
 * TIME: 15:19
 */
public class CropImageView extends FrameLayout implements View.OnTouchListener {

    private ImageView leftCornorView, rightCornorView, topCornorView, bottomCornorView;

    private Drawable cropDrawable;

    private RelativeLayout relativeLayout;

    private ImageView coverageImageView;
    private ImageView cropImageView;

    private final int INITIAL_WIDTH = 65;
    private final int INITIAL_HEIGHT = 65;

    //actionBar+statusBar
    private int extraHeight;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化
    private void init() {

        extraHeight = getStatusBarHeight(this.getContext()) + getActionBarHeight(this.getContext());

        cropDrawable = getCropDrawable(0x7fdddddd, 0xffffe5c3, INITIAL_WIDTH / 4);
        leftCornorView = addDraggerDrawable(getCircleDrawable(0xffdd0e36, INITIAL_WIDTH, INITIAL_HEIGHT));
        rightCornorView = addDraggerDrawable(getCircleDrawable(0xffdd0e36, INITIAL_WIDTH, INITIAL_HEIGHT));
        topCornorView = addDraggerDrawable(getCircleDrawable(0xffdd0e36, INITIAL_WIDTH, INITIAL_HEIGHT));
        bottomCornorView = addDraggerDrawable(getCircleDrawable(0xffdd0e36, INITIAL_WIDTH, INITIAL_HEIGHT));

        relativeLayout = new RelativeLayout(this.getContext());

        cropImageView = new ImageView(this.getContext());
        cropImageView.setAdjustViewBounds(true);
        cropImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cropImageView.setMaxWidth(getResources().getDisplayMetrics().widthPixels);
        cropImageView.setImageResource(R.drawable.demo);
        RelativeLayout.LayoutParams cropLayoutParams = createLayoutParams();
        cropImageView.setLayoutParams(cropLayoutParams);
        relativeLayout.addView(cropImageView);

        coverageImageView = new ImageView(this.getContext());
        coverageImageView.setOnTouchListener(this);
        //为啥要background才行
        if (Build.VERSION.SDK_INT < 16)
            coverageImageView.setBackgroundDrawable(cropDrawable);
        else
            coverageImageView.setBackground(cropDrawable);

        RelativeLayout.LayoutParams coverLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        int screentWidth = getScreenSize()[0];
        int screentHeight = getScreenSize()[1];

        coverLayoutParams.width = screentWidth / 2;
        coverLayoutParams.height = screentHeight / 2;

        coverLayoutParams.leftMargin = screentWidth / 4;
        coverLayoutParams.topMargin = screentHeight / 4;

        coverageImageView.setLayoutParams(coverLayoutParams);
        relativeLayout.addView(coverageImageView);

        int leftMargin = coverLayoutParams.leftMargin;
        int topMargin = coverLayoutParams.topMargin;

        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int plusPixels = INITIAL_WIDTH / 2;
        mLayoutParams.leftMargin = leftMargin - plusPixels;
        mLayoutParams.topMargin = topMargin + coverLayoutParams.height / 2 - plusPixels;
        relativeLayout.addView(leftCornorView, mLayoutParams);


        mLayoutParams = createLayoutParams();
        mLayoutParams.topMargin = topMargin - plusPixels;
        mLayoutParams.leftMargin = leftMargin + coverLayoutParams.width / 2 - plusPixels;
        relativeLayout.addView(topCornorView, mLayoutParams);

        mLayoutParams = createLayoutParams();
        mLayoutParams.leftMargin = leftMargin + coverLayoutParams.width - plusPixels;
        mLayoutParams.topMargin = topMargin + coverLayoutParams.height / 2 - plusPixels;
        relativeLayout.addView(rightCornorView, mLayoutParams);

        mLayoutParams = createLayoutParams();
        mLayoutParams.leftMargin = leftMargin + coverLayoutParams.width / 2 - plusPixels;
        mLayoutParams.topMargin = topMargin + coverLayoutParams.height - plusPixels;
        relativeLayout.addView(bottomCornorView, mLayoutParams);

        //为了item能够显示出来
        RelativeLayout.LayoutParams relLayoutParams = createLayoutParams();
        relLayoutParams.width = screentWidth + INITIAL_WIDTH;
        relLayoutParams.height = screentHeight + INITIAL_HEIGHT;
        this.addView(relativeLayout, relLayoutParams);
    }


    private RelativeLayout.LayoutParams createLayoutParams() {
        int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
        return new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    }

    private RelativeLayout.LayoutParams createLayoutParams(int w, int h) {
        int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
        return new RelativeLayout.LayoutParams(w, h);
    }

    private ImageView addDraggerDrawable(Drawable drawable) {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(drawable);
        imageView.setOnTouchListener(this);
        return imageView;
    }

    private Drawable getCropDrawable(int fillColor, int strokeColor, int strokeWidth) {
        ShapeDrawable shapeDrawable1 = new ShapeDrawable(new RectShape());
        Paint paint = shapeDrawable1.getPaint();
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        ShapeDrawable shapeDrawable2 = new ShapeDrawable(new RectShape());
        Paint paint2 = shapeDrawable2.getPaint();
        paint2.setColor(fillColor);
        paint2.setStyle(Paint.Style.FILL);

        return new LayerDrawable(new Drawable[]{shapeDrawable1, shapeDrawable2});
    }

    private Drawable getCircleDrawable(int color, int width, int height) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(color);
        shapeDrawable.setIntrinsicWidth(width);
        shapeDrawable.setIntrinsicHeight(height);
        return shapeDrawable;
    }


    float dx, dy;
    RelativeLayout.LayoutParams params;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                dx = event.getX();
                dy = event.getY();
                if (v == leftCornorView) {
                    handleMoveCover();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float leftMargin = event.getX() - dx;
                float topMargin = event.getY() - dy;
                params.leftMargin += leftMargin;
                params.topMargin += topMargin;
                if (v == coverageImageView) {
                    checkParams(params);
                    v.setLayoutParams(params);
                    handleMoveCover();
                } else {
                    handleMoveItem((ImageView) v, leftMargin, topMargin);
                }
                break;
            case MotionEvent.ACTION_UP:
                //修正手指难以滑满屏幕
                if (v == rightCornorView) {
                    int[] screenSize = getScreenSize();
                    RelativeLayout.LayoutParams layoutParams = getLayoutParams(coverageImageView);
                    int rightBorder = screenSize[0] - layoutParams.width - layoutParams.leftMargin;
                    if (dx > 0 && rightBorder <= INITIAL_WIDTH / 2) {
                        layoutParams.width += rightBorder;
                        coverageImageView.setLayoutParams(layoutParams);
                        handleMoveCover();
                    }
                }
                break;
        }

        return true;
    }


    private void handleMoveCover() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) coverageImageView.getLayoutParams();
        int width = layoutParams.width;
        int height = layoutParams.height;
        int[] margins = getMargins(coverageImageView);
        int leftMargin = margins[0];
        int topMargin = margins[1];

        int plusPixels = INITIAL_WIDTH / 2;

        leftMargin -= plusPixels;
        topMargin = topMargin + height / 2 - plusPixels;
        setMargins(leftCornorView, leftMargin, topMargin);

        leftMargin += width / 2;
        topMargin -= height / 2;
        setMargins(topCornorView, leftMargin, topMargin);

        leftMargin += width / 2;
        topMargin += height / 2;
        setMargins(rightCornorView, leftMargin, topMargin);

        leftMargin -= width / 2;
        topMargin += height / 2;
        setMargins(bottomCornorView, leftMargin, topMargin);
    }

    /**
     * @param view 目标view
     * @param dx   偏移量
     * @param dy   偏移量
     */
    private void handleMoveItem(ImageView view, float dx, float dy) {
        RelativeLayout.LayoutParams layoutParams = getLayoutParams(coverageImageView);
        int[] screenSize = getScreenSize();
        if (view == leftCornorView) {
            //没到边界或者向右滑动
            if (layoutParams.leftMargin > 0 || dx > 0) {
                layoutParams.leftMargin += dx;
                layoutParams.width += -dx;
            }
        } else if (view == rightCornorView) {
            if (layoutParams.width < screenSize[0] || dx < 0) {
                layoutParams.width += dx;
            }

            if (layoutParams.width > screenSize[0])
                layoutParams.width = screenSize[0];

        } else if (view == topCornorView) {
            if (layoutParams.topMargin > 0 || dy > 0) {
                layoutParams.topMargin += dy;
                layoutParams.height += -dy;
            }
        } else if (view == bottomCornorView) {
            int maxHeight = screenSize[1] - extraHeight;
            if (layoutParams.height < maxHeight || dy < 0) {
                layoutParams.height += dy;
            }
            if (layoutParams.height > maxHeight)
                layoutParams.height = maxHeight;
        }
        coverageImageView.setLayoutParams(layoutParams);
        handleMoveCover();
    }

    private int[] getMargins(ImageView view) {
        RelativeLayout.LayoutParams layoutParams = getLayoutParams(view);
        return new int[]{layoutParams.leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin, layoutParams.bottomMargin};
    }

    private void setMargins(ImageView view, float leftMargin, float topMargin) {
        RelativeLayout.LayoutParams layoutParams = getLayoutParams(view);
        layoutParams.leftMargin = (int) leftMargin;
        layoutParams.topMargin = (int) topMargin;
        view.setLayoutParams(layoutParams);
    }

    private void setMarginsPiexls(ImageView view, float dx, float dy) {
        RelativeLayout.LayoutParams layoutParams = getLayoutParams(view);
        layoutParams.leftMargin += dx * 0.7;
        layoutParams.topMargin += dy * 0.7;
        view.setLayoutParams(layoutParams);
    }

    private RelativeLayout.LayoutParams getLayoutParams(ImageView imageView) {
        return (RelativeLayout.LayoutParams) imageView.getLayoutParams();
    }

    private void checkParams(RelativeLayout.LayoutParams params) {
        if (params.leftMargin < 0)
            params.leftMargin = 0;
        if (params.topMargin < 0)
            params.topMargin = 0;
        int[] screenSize = getScreenSize();
        if (params.leftMargin > screenSize[0] - params.width)
            params.leftMargin = screenSize[0] - params.width;

        int topBorder = screenSize[1] - extraHeight - params.height;
        if (params.topMargin > topBorder)
            params.topMargin = topBorder;
    }

    private int[] getScreenSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screentWidth = metrics.widthPixels;
        int screentHeight = metrics.heightPixels;
        return new int[]{screentWidth, screentHeight};
    }

    /**
     * 获取状态栏的高度
     *
     * @param context 上下文
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("action_bar_default_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public ImageView getCropImageView() {
        return cropImageView;
    }

    public Rect getCoverBounds() {
        //获取crop区域相对于coverImageView的区域
        Rect cropRect = getCropBounds();

        int[] location = new int[2];
        coverageImageView.getLocationInWindow(location);
        Rect coverRect = getRect(location, coverageImageView);

        if (coverRect.left < cropRect.right && coverRect.top < cropRect.bottom) {
            if (coverRect.bottom > cropRect.bottom)
                coverRect.bottom = cropRect.bottom;
        } else return null;

        return coverRect;

    }

    public Rect getCropBounds() {
        int[] location = new int[2];
        cropImageView.getLocationInWindow(location);
        return getRect(location, cropImageView);
    }

    public void cropImage() {

    }

    private Rect getRect(int[] location, View v) {
        //去除statusBar和actionbar的高度
        location[1] -= extraHeight;
        int measureW = v.getMeasuredWidth();
        int measureH = v.getMeasuredHeight();
        return new Rect(location[0], location[1], location[0] + measureW, location[1] + measureH);
    }

    public void drawPoints(float[] location) {
        if (pointViewLinkedList.size() >= 2) {
            this.relativeLayout.removeView(pointViewLinkedList.remove(0));
            this.relativeLayout.removeView(pointViewLinkedList.remove(0));
        }
        MyPointView view = new MyPointView(this.getContext(), location);
        this.relativeLayout.addView(view);
        pointViewLinkedList.add(view);
    }

    private LinkedList<MyPointView> pointViewLinkedList = new LinkedList<MyPointView>();

}