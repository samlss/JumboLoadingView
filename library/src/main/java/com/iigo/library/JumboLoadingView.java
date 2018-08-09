package com.iigo.library;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * @author SamLeung
 * @e-mail 729717222@qq.com
 * @github https://github.com/samlss
 * @csdn https://blog.csdn.net/Samlss
 * @description Yo Yo, another loading view.
 */
public class JumboLoadingView extends View {
    private final static int DEFAULT_COLOR = Color.BLACK;
    private final static int SHAPE_TYPE_CIRCLE = 0;
    private final static int SHAPE_TYPE_SQUARE = 1;
    private final static int SHAPE_TYPE_TRIANGLE = 2;
    private final static int SHAPE_TYPE_STAR = 3;

    private final static int SHAPE_STYLE_STROKE = 0;
    private final static int SHAPE_STYLE_FILL = 1;

    private float mCircleRadius;
    private float mShapeRadius;
    private int mStrokeWidth;
    private float mCirclePathLength;

    private boolean mShowProgress = false;
    private int mProgress = 0;
    private int mShapeColor  = DEFAULT_COLOR;
    private int mCircleColor = DEFAULT_COLOR;
    
    private int mProgressTextColor   = DEFAULT_COLOR;
    private float mProgressTextSize  = 25;
    private Rect textBoundRect;

    private int mShapeType = SHAPE_TYPE_CIRCLE;
    private int mShapeStyle = SHAPE_STYLE_STROKE;

    private Path mCirclePath;
    private Path mCircleSegmentPath;
    private Path mShapePath;
    private RectF mShapeRectF;

    private PathMeasure mCirclePathMeasure;

    private Paint mCirclePaint;
    private Paint mShapePaint;
    private Paint mTextPaint;

    private ValueAnimator mValueAnimator;
    private TimeInterpolator mTimeInterpolator = new AccelerateDecelerateInterpolator();
    private float mAnimatorValue;

    private long mCurrentAnimatorPlayTime;

    /**
     * The real position of {@link PathMeasure}.
     * */
    private float[] mRealPos = new float[2];

    /**
     * The real tan of {@link PathMeasure}
     * */
    private float[] mRealTan = new float[2];

    private float centerX;
    private float centerY;

    public JumboLoadingView(Context context) {
        this(context, null);
    }

    public JumboLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JumboLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JumboLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        parseAttrs(attrs);
        init();
    }

    private void parseAttrs(AttributeSet attrs) {
        if (attrs == null){
            return;
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.JumboLoadingView);
        mCircleColor = typedArray.getColor(R.styleable.JumboLoadingView_circleColor, DEFAULT_COLOR);
        mShapeColor  = typedArray.getColor(R.styleable.JumboLoadingView_shapeColor, DEFAULT_COLOR);
        mShapeType   = typedArray.getInt(R.styleable.JumboLoadingView_shapeType, SHAPE_TYPE_CIRCLE);
        mShapeStyle  = typedArray.getInt(R.styleable.JumboLoadingView_shapeStyle, SHAPE_STYLE_STROKE);

        mShowProgress        = typedArray.getBoolean(R.styleable.JumboLoadingView_showProgress, false);
        mProgress            = typedArray.getInt(R.styleable.JumboLoadingView_progress, 0);
        mProgressTextColor   = typedArray.getColor(R.styleable.JumboLoadingView_progressTextColor, DEFAULT_COLOR);
        mProgressTextSize    = typedArray.getDimensionPixelSize(R.styleable.JumboLoadingView_progressTextSize, 25);

        int interpolatorValue = typedArray.getInt(R.styleable.JumboLoadingView_interpolator, 0);
        switch(interpolatorValue) {
            case 0:
                this.mTimeInterpolator = new AccelerateDecelerateInterpolator();
                break;
            case 1:
                this.mTimeInterpolator = new AccelerateInterpolator();
                break;
            case 2:
                this.mTimeInterpolator = new DecelerateInterpolator();
                break;
            case 3:
                this.mTimeInterpolator = new BounceInterpolator();
                break;
            case 4:
                this.mTimeInterpolator = new CycleInterpolator(0.5F);
                break;
            case 5:
                this.mTimeInterpolator = new LinearInterpolator();
                break;
            case 6:
                this.mTimeInterpolator = new AnticipateOvershootInterpolator();
                break;
            case 7:
                this.mTimeInterpolator = new AnticipateInterpolator();
                break;
            case 8:
                this.mTimeInterpolator = new OvershootInterpolator();
        }

        typedArray.recycle();
    }

    private void init() {
        mShapeRectF = new RectF();

        mCirclePath = new Path();
        mShapePath  = new Path();

        mCircleSegmentPath = new Path();
        mCirclePathMeasure = new PathMeasure();

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShapePaint.setColor(mShapeColor);
        mShapePaint.setStyle(mShapeStyle == SHAPE_STYLE_STROKE ? Paint.Style.STROKE : Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setTextSize(mProgressTextSize);
        textBoundRect = new Rect();
    }

    private void initStarPath(){
        List<PointF> starPoints = new ArrayList<>();

        float R = mShapeRadius;
        float r = mShapeRadius / 2f;

        for (int i = 0; i < 5; i++){
            //outer
            double outerDoc = (18 + 72 * i) / 180d * Math.PI;
            PointF pointF1 = new PointF((float) (Math.cos(outerDoc) * R) ,
                    - (float) (Math.sin(outerDoc) * R));

            //inner
            double innerDoc = (54 + 72 * i) / 180d * Math.PI;
            PointF pointF2 = new PointF((float)(Math.cos(innerDoc) * r) ,
                    -(float) (Math.sin(innerDoc) * r));

            starPoints.add(pointF1);
            starPoints.add(pointF2);
        }

        mShapePath.moveTo(starPoints.get(0).x, starPoints.get(0).y);
        mShapePath.lineTo(starPoints.get(1).x, starPoints.get(1).y);
        mShapePath.lineTo(starPoints.get(2).x, starPoints.get(2).y);
        mShapePath.lineTo(starPoints.get(3).x, starPoints.get(4).y);
        mShapePath.lineTo(starPoints.get(4).x, starPoints.get(4).y);
        mShapePath.lineTo(starPoints.get(5).x, starPoints.get(5).y);
        mShapePath.lineTo(starPoints.get(6).x, starPoints.get(6).y);
        mShapePath.lineTo(starPoints.get(7).x, starPoints.get(7).y);
        mShapePath.lineTo(starPoints.get(8).x, starPoints.get(8).y);
        mShapePath.lineTo(starPoints.get(9).x, starPoints.get(9).y);
        mShapePath.lineTo(starPoints.get(0).x, starPoints.get(0).y);
    }

    private void initTrianglePath() {
        mShapePath.moveTo(mShapeRadius, 0);
        mShapePath.lineTo(-mShapeRadius / 2f, -mShapeRadius / 2f);
        mShapePath.lineTo(-mShapeRadius / 2f, mShapeRadius /2f);
        mShapePath.lineTo(mShapeRadius, 0);
    }

    private void setupAnimator(){
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setInterpolator(mTimeInterpolator);
        mValueAnimator.setDuration(2000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeWidth  = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //没有指定宽高,使用了wrap_content,则手动指定宽高为MATCH_PARENT
        // (No width or height is specified, wrap_content is used, and the width and height are manually specified as MATCH_PARENT)
        if (modeWidth == AT_MOST && modeHeight == AT_MOST){
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        release();

        centerX = w / 2;
        centerY = h / 2;

        int minSize = Math.min(w, h);

        mStrokeWidth = minSize / 50;
        mShapeRadius = minSize / 10f;

        mCircleRadius = (minSize - 2 * mStrokeWidth - mShapeRadius * 2) / 2;

        mCirclePath.reset();
        mShapePath.reset();

        mCirclePaint.setStrokeWidth(mStrokeWidth);
        mShapePaint.setStrokeWidth(mStrokeWidth);

        mCirclePath.addCircle(centerX, centerY, mCircleRadius, Path.Direction.CW);
        mCirclePathMeasure.setPath(mCirclePath, true);
        mCirclePathLength = mCirclePathMeasure.getLength();

        if (mShapeType == SHAPE_TYPE_STAR){
            initStarPath();
        }else if (mShapeType == SHAPE_TYPE_TRIANGLE){
            initTrianglePath();
        }

        setupAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float realDistance = mAnimatorValue * mCirclePathLength;
        mCircleSegmentPath.reset();
        mCirclePathMeasure.getSegment(0,realDistance, mCircleSegmentPath, true);
        mCirclePathMeasure.getPosTan(realDistance, mRealPos, mRealTan);

        canvas.drawPath(mCircleSegmentPath, mCirclePaint);
        drawShape(canvas, mRealPos, mRealTan);

        if (isShowProgress()) {
            drawProgress(canvas);
        }
    }

    /**
     * Now draw the shape.
     *
     * @param canvas The {@link Canvas} of this View.
     * @param pos The position information.
     * @param tan The tan angel information.
     * */
    private void drawShape(Canvas canvas, float[] pos, float[] tan){
        canvas.save();
        canvas.translate(pos[0], pos[1]);

        if (mShapeType != SHAPE_TYPE_CIRCLE) {
            float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
            canvas.rotate(degree);
        }

        switch (mShapeType){
            case SHAPE_TYPE_CIRCLE:
                canvas.drawCircle(0, 0, mShapeRadius, mShapePaint);
                break;

            case SHAPE_TYPE_SQUARE:
                mShapeRectF.set( - mShapeRadius, - mShapeRadius, mShapeRadius, mShapeRadius);
                canvas.drawRect(mShapeRectF, mShapePaint);
                break;

            case SHAPE_TYPE_STAR:
                canvas.drawPath(mShapePath, mShapePaint);
                break;

            case SHAPE_TYPE_TRIANGLE:
                canvas.drawPath(mShapePath, mShapePaint);
                break;

            default: break;
        }

        canvas.restore();
    }

    /**
     * Now draw the progress.
     *
     * @param canvas The {@link Canvas} of this View.
     * */
    private void drawProgress(Canvas canvas){
        if (mProgress <= 0){
            mProgress = 0;
        } else if (mProgress > 100){
            mProgress = 100;
        }

        String text = mProgress + "%";

        mTextPaint.getTextBounds(text, 0, text.length(), textBoundRect);
        canvas.drawText(text, centerX - textBoundRect.width() / 2, centerY + textBoundRect.height() / 2, mTextPaint);
    }

    /**
     * Check if show the progress.
     *
     * @return True is show, otherwise is not.
     * */
    public boolean isShowProgress() {
        return mShowProgress;
    }

    /**
     * Set whether to show progress.
     * */
    public void setShowProgress(boolean showProgress) {
        this.mShowProgress = showProgress;
        invalidate();
    }

    /**
     * Set the progress 'TextSize' in pixels.
     * */
    public void setProgressTextSize(float progressTextSizeInPixels) {
        this.mProgressTextSize = progressTextSizeInPixels;
        mTextPaint.setTextSize(progressTextSizeInPixels);
        invalidate();
    }

    /**
     * Set the color of progress text.
     * */
    public void setProgressTextColor(int progressTextColor) {
        this.mProgressTextColor = progressTextColor;
        mTextPaint.setColor(progressTextColor);
        invalidate();
    }

    /**
     * Set the progress.
     *
     * @param progress The progress value, show be greater than or equal to 0, less than or equal to 100.
     * */
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    /**
     * Get the current progress.
     * */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Set color of the circle.
     * */
    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
        mCirclePaint.setColor(circleColor);
        invalidate();
    }

    /**
     * Set color of the shape.
     * */
    public void setShapeColor(int shapeColor) {
        this.mShapeColor = shapeColor;
        mShapePaint.setColor(shapeColor);
        invalidate();
    }

    /**
     * Start animation
     * */
    public void start(){
        if (mValueAnimator != null
                && !mValueAnimator.isRunning()){
            mValueAnimator.setCurrentPlayTime(mCurrentAnimatorPlayTime);
            mValueAnimator.start();
        }
    }

    /**
     * Stop animation
     * */
    public void stop(){
        if (mValueAnimator != null
                && mValueAnimator.isRunning()){
            mCurrentAnimatorPlayTime = mValueAnimator.getCurrentPlayTime();
            mValueAnimator.cancel();
        }
    }

    /**
     * Release this view when you do not need it again.
     * */
    public void release(){
        stop();

        if (mValueAnimator != null){
            mValueAnimator.removeAllUpdateListeners();
        }
    }
}
