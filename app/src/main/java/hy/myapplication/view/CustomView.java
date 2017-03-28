package hy.myapplication.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import hy.myapplication.R;
import hy.myapplication.utils.CommonUtils;

/**
 * Created by huangyue on 2017/3/27.
 */

public class CustomView extends View{
    /*判断滑动的最小距离*/
    private static final int MIN_MOVE_DIS = 5;
    /*画笔*/
    private Paint mPaint;
    private int mScreenWidth;
    private int mScreenHeight;
    /*记录路径中相对的上一个点的坐标*/
    private float perX;
    private float perY;
    /*前景图(灰色的蒙版)*/
    private Bitmap mFGBitmap;
    /*背景图(灰色蒙版下的图片)*/
    private Bitmap mBGBitmap;
    /*操作前景图的画布*/
    private Canvas mCanvas;
    /*手指滑动路径*/
    private Path mPath;

    public CustomView(Context context) {
        super(context,null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = CommonUtils.getDeviceSize(context)[0];
        mScreenHeight = CommonUtils.getDeviceSize(context)[1];
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*画背景图(真正的图片)*/
        canvas.drawBitmap(mBGBitmap,0,0,null);
        /*画前景图(蒙版)*/
        canvas.drawBitmap(mFGBitmap,0,0,null);
        /*将手指的路径画到前景图上*/
        mCanvas.drawPath(mPath,mPaint);
    }



    private void init(Context context){
        mPath = new Path();
        /*设置抗锯齿和抗抖动*/
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        /*设置透明的画笔，画出透明的路径*/
        mPaint.setARGB(128,0,0,0);
        /*设置混合模式为DST_IN，当路径画到前景图上时用路径。。。不好描述，百度吧。。。*/
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        /*设置画笔为描边*/
        mPaint.setStyle(Paint.Style.STROKE);
        /*语言匮乏。。。不理解请百度。。。*/
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        /*设置线图的末端为半圆的*/
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        /*路径宽度*/
        mPaint.setStrokeWidth(50);
        /*生成前景图*/
        mFGBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight, Bitmap.Config.ARGB_8888);
        /*将前景图注入画布，此后mCanvas的操作全部在mFGBitmap上*/
        mCanvas = new Canvas(mFGBitmap);
        /*蒙版颜色*/
        mCanvas.drawColor(0xff808080);
        /*生成背景图*/
        mBGBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test);
        /*将背景图拉伸为屏幕大小*/
        mBGBitmap = Bitmap.createScaledBitmap(mBGBitmap,mScreenWidth,mScreenHeight,true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x,y);
                perX = x;
                perY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x-perX);
                float dy = Math.abs(y-perY);
                if(dx>=MIN_MOVE_DIS || dy>=MIN_MOVE_DIS){
                    /*二阶贝塞尔曲线*/
                    mPath.quadTo(perX,perY,(x+perX)/2,(y+perY)/2);
                    perX = x;
                    perY = y;
                }
                break;
        }
        invalidate();
        return true;
    }
}
