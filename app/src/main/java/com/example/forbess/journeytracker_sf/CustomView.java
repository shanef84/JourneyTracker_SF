package com.example.forbess.journeytracker_sf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by forbess on 01/05/2016.
 */
public class CustomView extends View {
    //private fields for rendering the view
    private final int boardH = 70;
    private final int boardW = 30;
    private int cellWidth, cellHeight, index;
    private Paint white, red, green;
    private float average;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure) {
        super.onMeasure(widthMeasure, heightMeasure);
        int size =0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthPadding = width - getPaddingLeft() - getPaddingRight();
        int heightPadding = height - getPaddingTop() - getPaddingBottom();
        //set dimensions
        if (widthPadding > heightPadding) {
            size = heightPadding;
        } else {
            size = widthPadding;
        }
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    // refactored init method as most of this code is shared by all the constructors
    private void init() {

        //set colors
        white = new Paint(Paint.ANTI_ALIAS_FLAG); white.setColor(Color.WHITE);white.setStrokeWidth(2);
        red = new Paint(Paint.ANTI_ALIAS_FLAG); red.setColor(Color.RED);red.setStrokeWidth(2);
        green = new Paint(Paint.ANTI_ALIAS_FLAG); green.setColor(Color.GREEN);green.setStrokeWidth(2);
        green.setStyle(Paint.Style.STROKE); green.setAntiAlias(true);green.setShadowLayer(4, 2, 2, 0x80000000);


    }

    // public method that needs to be overridden to draw the contents of this widget
    @Override
    public void onDraw(Canvas canvas) {
        // call the superclass method
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        //draw lines
        for (int i = 0; i < boardH; i=i+10) {
            canvas.drawLine(0, i * (getHeight() / boardH), getWidth(), i * (getHeight() / boardH), white);
        }
        average = boardH - MainActivity.getAverage();
        canvas.drawLine(0, average * (getHeight() / boardH), getWidth(), average * (getHeight() / boardH), red);
        Path path = new Path();
        path.moveTo(getX(0f, boardW), getY(0f, boardH));

        for (int i = 0; i < boardW; i++){
            path.lineTo(getX(i, boardW), getY(MainActivity.tracker[i], boardH));
        }
        canvas.drawPath(path, green);
    }

    private float getY(float value, float max){
        float height = getHeight() - getPaddingTop() - getPaddingBottom();
        value = (value / boardH) * height;
        value = height - value;
        value += getPaddingTop();
        return value;
    }

    private float getX(float value, float max){
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        value = (value / boardW) * width;
        return value;
    }

}
