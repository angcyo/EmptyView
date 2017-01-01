package com.angcyo.emptyview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by angcyo on 2017-01-01 10:46.
 */
public class EmptyView extends View {
    Paint mPaint;

    /**
     * 默认的颜色
     */
    @ColorInt
    int defaultColor = Color.parseColor("#80E3E3E3");

    /**
     * 多少个组,每一组由一个大的三个小的组成
     */
    int mGroupCount = 3;

    int mDefaultGroupHeight = 100;//px

    /**
     * 横向空隙大小
     */
    int mHSpace = 10;//px
    /**
     * 竖向空隙大小
     */
    int mVSpace = 20;//px


    /**
     * 圆角大小
     */
    int mRoundRadius = 10;//px

    RectF mRectF;
    RectF mRectFLittle;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDefaultGroupHeight *= getDensity();
        mHSpace *= getDensity();
        mVSpace *= getDensity();
        mRoundRadius *= getDensity();

        if (!isInEditMode()) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);

            defaultColor = array.getColor(R.styleable.EmptyView_ev_default_color, defaultColor);
            mGroupCount = array.getInt(R.styleable.EmptyView_ev_group_count, mGroupCount);
            mDefaultGroupHeight = array.getDimensionPixelSize(R.styleable.EmptyView_ev_group_height, mDefaultGroupHeight);
            mHSpace = array.getDimensionPixelSize(R.styleable.EmptyView_ev_h_space, mHSpace);
            mVSpace = array.getDimensionPixelSize(R.styleable.EmptyView_ev_v_space, mVSpace);
            mRoundRadius = array.getDimensionPixelSize(R.styleable.EmptyView_ev_round_radius, mRoundRadius);

            array.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(defaultColor);


        mRectF = new RectF();
        mRectFLittle = new RectF();
    }

    private float getDensity() {
        return getResources().getDisplayMetrics().density;
    }

    /**
     * 每一个组的高度
     *
     * @param defaultGroupHeight px高度
     */
    public void setDefaultGroupHeight(int defaultGroupHeight) {
        mDefaultGroupHeight = defaultGroupHeight;
    }

    /**
     * 多少个组
     */
    public void setGroupCount(int groupCount) {
        mGroupCount = groupCount;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = getPaddingTop() + getPaddingBottom() + mDefaultGroupHeight * mGroupCount + mVSpace * (mGroupCount - 1);
        } else {
            mGroupCount = (int) Math.ceil(heightSize * 1.f / (mDefaultGroupHeight + mVSpace));
        }

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) (getPaddingLeft() + getPaddingRight() + mDefaultGroupHeight + mHSpace + mDefaultGroupHeight * 1.5f);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        mRectF.set(0, 0, mDefaultGroupHeight, mDefaultGroupHeight);
        for (int i = 1; i <= mGroupCount; i++) {
            drawBigRect(canvas, i);
        }
        canvas.restore();
    }

    /**
     * 绘制大矩形
     */
    private void drawBigRect(Canvas canvas, int i) {
        canvas.save();
        canvas.translate(0, (i - 1) * mDefaultGroupHeight + (i - 1) * mVSpace);
        drawRect(canvas);
        drawLittleRect(canvas);
        canvas.restore();
    }

    private void drawRect(Canvas canvas) {
        canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);
    }

    private void drawLittleRect(Canvas canvas) {
        int height = (mDefaultGroupHeight - 2 * mVSpace) / 3;
        final int right = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();

        mRectFLittle.set(mDefaultGroupHeight + mHSpace, 0, right, height);
        canvas.drawRoundRect(mRectFLittle, mRoundRadius, mRoundRadius, mPaint);

        mRectFLittle.set(mDefaultGroupHeight + mHSpace, height + mVSpace,
                getPaddingLeft() + mDefaultGroupHeight / 2 + mHSpace / 2 + right / 2,
                2 * height + mVSpace);
        canvas.drawRoundRect(mRectFLittle, mRoundRadius, mRoundRadius, mPaint);

        mRectFLittle.set(mDefaultGroupHeight + mHSpace,
                2 * height + 2 * mVSpace, right,
                3 * height + 2 * mVSpace);
        canvas.drawRoundRect(mRectFLittle, mRoundRadius, mRoundRadius, mPaint);
    }
}
