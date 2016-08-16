package com.example.ruijie.myplay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ruijie on 2016/8/16.
 * 自己写字体  加载ttf
 *
 *
 */
public class MyTextView extends TextView {


    private TextPaint textPaint;

    public MyTextView(Context context) {
        super(context);
        initView(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       initView(context);
    }

    private void initView(Context context) {
        //        标志位意指抗锯齿的
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "moben.ttf");
        textPaint.setTypeface(typeface);
        textPaint.setTextSize(200);
        textPaint.setColor(Color.parseColor("#FA7199"));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("我的视频播放器",getWidth()/5,getHeight()/2,textPaint);
    }
}
