package com.rayzhang.android.materialdesign.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Ray on 2016/12/31.
 */

public class RZIndicatorView extends View {
    /**
     * 自訂義指示器 View
     */
    private static final String TAG = RZIndicatorView.class.getSimpleName();
    private Paint mPaint, mPaintExpand;
    // 半徑
    private float radius;
    // 直徑
    private float diameter;
    // 間距
    private float gap;
    // 數量
    private int indiCount;
    // 目前位置
    private int curPositon;
    // 紀錄總長度
    private int totalLen;
    // 選到Item動畫
    private ValueAnimator animator_expand;
    // 是否要繪製
    private boolean canDraw = false;
    // 擴散時的透明度
    private int expandAlphaValue = 255;
    // 擴散時的半徑
    private float expandRadius = 0f;
    // 動畫時間
    private static final int DURATION = 400;

    public RZIndicatorView(Context context) {
        this(context, null);
    }

    public RZIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RZIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaintExpand = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintExpand.setDither(true);
        mPaintExpand.setStyle(Paint.Style.FILL);

        gap = radius = 10f;
        diameter = radius * 2;
        curPositon = 0;
        indiCount = 0;
        setViewAnimatiorExpand();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 只有1頁，所以不需要顯示
        if (indiCount <= 1) {
            return;
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY) {
            widthSize = (int) (diameter * indiCount + radius * (indiCount + 1));
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) diameter * 2;
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            widthSize = (int) (diameter * indiCount + radius * (indiCount + 1));
            heightSize = (int) diameter * 2;
        } else {
            // don't do anything
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (indiCount <= 1) return;
        int w = getWidth();
        int h = getHeight();
        float posX = diameter;
        if (w > totalLen) posX += (w - totalLen) / 2;

        for (int i = 0; i < indiCount; i++) {
            if (curPositon == i) {
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            } else {
                mPaint.setStyle(Paint.Style.STROKE);
            }
            //canvas.drawCircle(posX + (diameter + gap) * i, h - diameter, radius, mPaint);
            // 垂直水平 居中顯示
            canvas.drawCircle(posX + (diameter + gap) * i, h / 2, radius, mPaint);
        }
        if (canDraw) {
            mPaintExpand.setARGB(expandAlphaValue, 255, 80, 80);
            canvas.drawCircle(posX + (diameter + gap) * curPositon, h / 2, expandRadius, mPaintExpand);
        }
    }

    private void setViewAnimatiorExpand() {
        animator_expand = ValueAnimator.ofFloat(0f, diameter);
        animator_expand.setDuration(DURATION);
        animator_expand.setInterpolator(new AccelerateDecelerateInterpolator());
        animator_expand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                expandRadius = (float) animation.getAnimatedValue();
                expandAlphaValue = (int) (255 - (255 * expandRadius) / diameter);
                invalidate();
            }
        });
        animator_expand.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                canDraw = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                canDraw = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setRadius(float radius) {
        this.radius = radius;
        gap = radius;
        diameter = radius * 2;
        invalidate();
    }

    public void setCurPositon(int curPositon, boolean isFirst) {
        this.curPositon = curPositon;
        invalidate();
        if (!isFirst) animator_expand.start();
    }

    public void setIndiCount(int indiCount) {
        this.indiCount = indiCount;
        totalLen = (int) (diameter * indiCount + radius * (indiCount + 1));
        invalidate();
    }

    public void setIndicatorColor(@ColorInt int color) {
        mPaint.setColor(color);
        //mPaintExpand.setColor(color);
    }

    public void setIndicatorColor(String color) {
        mPaint.setColor(Color.parseColor(color));
        //mPaintExpand.setColor(Color.parseColor(color));
    }


    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
