/*
 *
 *  * Copyright 2014-2015 Eric Liu
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package online.postboard.android.util.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import online.postboard.android.util.ColorUtils;
import online.postboard.android.R;
import online.postboard.android.util.DimenUtils;


/**
 * Created by Eric on 14/12/27.
 */
public class CircleProgressBar extends View {

    public enum StartLocation {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    public enum PaintStyle {
        STROKE,
        FILL
    }

    private static final String TAG = "ArcProgressBar";

    private static final float MAX_SWEEP_ANGLE = 360;

    private static final int DEFAULT_SIZE = 80;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#999999");
    private static final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#54bfad");
    private static final int DEFAULT_PROGRESS_END_COLOR = 0;
    private static final StartLocation DEFAULT_START_LOCATION = StartLocation.TOP;
    private static final int DEFAULT_MAX = 100;
    private float DEFAULT_STROKE_WIDTH = 6;
    private static final boolean DEFAULT_TRANSITION_ENABLE = false;
    private static final PaintStyle DEFAULT_PAINT_STYLE = PaintStyle.STROKE;

    private int mProgressBackgroundColor;
    private int mProgressColor;
    private int mProgressEndColor;
    private StartLocation mStartLocation;
    private int mMax;
    private int mProgress;
    private PaintStyle mPaintStyle;

    private static final int END_ANGLE = 360;

    private Paint mProgressPaint;
    private Paint mProgressBgPaint;

    private RectF mProgressRectF = new RectF(0, 0, 0, 0);
    private RectF mProgressBgRectF = new RectF(0, 0, 0, 0);

    private float mStrokeWith = 0;

    private float unitAngle = 0.0f;

    private float getStrokeOffset() {
        if (mPaintStyle == PaintStyle.FILL) return 0;
        return mStrokeWith / 2;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
        setUnitProgress();
        invalidate();
    }

    public int getPrgoress() {
        return mProgress;
    }

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadStyledAttr(context, attrs, defStyle);
        initPaint();
    }

    private void loadStyledAttr(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar,
                defStyle, 0);
        mProgressBackgroundColor = attributes.getColor(R.styleable.CircleProgressBar_background_color, DEFAULT_BACKGROUND_COLOR);
        mProgressColor = attributes.getColor(R.styleable.CircleProgressBar_progress_color, DEFAULT_PROGRESS_COLOR);
        mProgressEndColor = attributes.getColor(R.styleable.CircleProgressBar_progress_end_color, DEFAULT_PROGRESS_END_COLOR);

        int startLocation = attributes.getInt(R.styleable.CircleProgressBar_start_location, DEFAULT_START_LOCATION.ordinal());
        mStartLocation = StartLocation.values()[startLocation];
        mMax = attributes.getInt(R.styleable.CircleProgressBar_max, DEFAULT_MAX);
        mProgress = attributes.getInt(R.styleable.CircleProgressBar_progress, 0);

        DEFAULT_STROKE_WIDTH = DimenUtils.dp2px(getContext(), DEFAULT_STROKE_WIDTH);
        mStrokeWith = attributes.getDimension(R.styleable.CircleProgressBar_stroke_width, DEFAULT_STROKE_WIDTH);

        int paintStyle = attributes.getInt(R.styleable.CircleProgressBar_paint_style, DEFAULT_PAINT_STYLE.ordinal());
        mPaintStyle = PaintStyle.values()[paintStyle];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
        setUnitProgress();
    }

    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) DimenUtils.dp2px(getContext(), DEFAULT_SIZE);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawProgressBg(canvas);
        drawProgress(canvas);
    }

    private void initPaint() {
        //Progress
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);

        //Progress Background
        mProgressBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressBgPaint.setColor(mProgressBackgroundColor);

        if (mPaintStyle == PaintStyle.STROKE) {
            mProgressPaint.setStyle(Paint.Style.STROKE);
            mProgressPaint.setStrokeWidth(mStrokeWith);

            mProgressBgPaint.setStyle(Paint.Style.STROKE);
            mProgressBgPaint.setStrokeWidth(mStrokeWith);
        }
    }

    private void drawProgress(Canvas canvas) {
        mProgressRectF.left = getPaddingLeft() + getStrokeOffset();
        mProgressRectF.top = getPaddingTop() + getStrokeOffset();
        mProgressRectF.right = getWidth() - getPaddingLeft() - getPaddingRight() - getStrokeOffset();
        mProgressRectF.bottom = getHeight() - getStrokeOffset();
        canvas.drawArc(mProgressRectF, getStartAngle(), getSweepAngel(), getUseCenter(), mProgressPaint);
    }

    private void drawProgressBg(Canvas canvas) {
        mProgressBgRectF.left = getPaddingLeft() + getStrokeOffset();
        mProgressBgRectF.top = getPaddingTop() + getStrokeOffset();
        mProgressBgRectF.right = getWidth() - getPaddingLeft() - getPaddingRight() - getStrokeOffset();
        mProgressBgRectF.bottom = getHeight() - getStrokeOffset();
        canvas.drawArc(mProgressBgRectF, getStartAngle(), END_ANGLE, getUseCenter(), mProgressBgPaint);
    }

    private boolean getUseCenter() {
        return mPaintStyle == PaintStyle.FILL;
    }

    private void setUnitProgress() {
        unitAngle = MAX_SWEEP_ANGLE / mMax;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        if (mProgressEndColor != 0)
            onProgressChanged();
        invalidate();
    }

    public void setProgressWithAnim(int progress, long duration) {
        setProgressWithAnim(0, progress, duration);
    }

    public void setProgressWithAnim(int fromProgress, int toProgress, long duration) {
        ValueAnimator animator = ValueAnimator.ofInt(fromProgress, toProgress);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((int) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private int getSweepAngel() {
        return (int) (unitAngle * mProgress);
    }

    private int getStartAngle() {
        switch (mStartLocation) {
            case RIGHT:
                return 0;
            case BOTTOM:
                return 90;
            case LEFT:
                return 180;
            case TOP:
                return 270;
        }
        return 0;
    }

    private void onProgressChanged() {
        float fraction = (float) mProgress / (float) mMax;
        int c = (Integer) ColorUtils.evaluate(fraction, mProgressColor, mProgressEndColor);
        mProgressPaint.setColor(c);
    }

}