package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * @author Nesterkin Alexander on 28.07.2018
 */
public class CustomView extends View {

    private TextView mTextView;
    private Random mRandom = new Random();

    private Paint mPaint;
    private float x = 0;
    private float y = 0;
    private float dx;
    private float dy;
    private int mReference;

    private boolean popal = false;
    private float dragX = 0;
    private float dragY = 0;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        dx = typedArray.getFloat(R.styleable.CustomView_rect_width, 100);
        dy = typedArray.getFloat(R.styleable.CustomView_rect_height, 100);
        int color = typedArray.getColor(R.styleable.CustomView_rect_color, getResources().getColor(R.color.colorPrimary));
        mReference = typedArray.getResourceId(R.styleable.CustomView_reference_text_view, 0);
        mPaint.setColor(color);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(x, y, x + dx, y + dy, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x <= eventX && eventX <= x + dx && y <= eventY && eventY <= y + dy) {
                    popal = true;
                    dragX = eventX - x;
                    dragY = eventY - y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (popal) {
                    x = eventX - dragX;
                    y = eventY - dragY;

                    mPaint.setARGB(255, mRandom.nextInt(), mRandom.nextInt(), mRandom.nextInt());
                    for (int i = 0; i < ((ViewGroup) getParent()).getChildCount(); i++) {
                        if (((ViewGroup) getParent()).getChildAt(i).getId() == mReference) {
                            mTextView = (TextView) ((ViewGroup) getParent()).getChildAt(i);
                        }
                    }
                    mTextView.setText("x = " + x + " y = " + y);

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                popal = false;
                break;
        }

        return true;
    }
}