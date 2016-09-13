// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.iGap.R;


/**
 * 
 * make dialog sun for show waiting dialog in program
 *
 */

public class SunBabyLoadingView extends View {

    private static final int    DEFAULT_DIAMETER_SIZE    = 180;
    private static final float  RATIO_LINE_START_X       = 5 / 6.f;
    private static final float  RATIO_LINE_START_Y       = 3 / 4.f;
    private static final float  RATIO_ARC_START_X        = 2 / 5.f;
    private static final float  SUNSHINE_SEPARATIO_ANGLE = 45;
    private static final String PAINT_COLOR              = "#7A6021";
    private static final String BG_COLOR                 = "#f2b33d";
    private static final float  SPACE_SUNSHINE           = 12;
    private static final float  SUNSHINE_LINE_LENGTH     = 15;
    private static final float  SUNSHINE_RISE_HEIGHT     = 10;
    private static final float  SUN_EYES_RADIUS          = 6;
    private static final int    DEFAULT_OFFSET_Y         = 20;
    private float               lineStartX, lineStartY, lineLength;
    private float               textX, textY;
    private float               sunRadius;
    private float               maxEyesTurn;
    private float               turnOffsetX;
    private float               orectLeft, orectTop, orectRight, orectBottom;
    private float               offsetY                  = DEFAULT_OFFSET_Y, offsetSpin, offsetAngle;
    private float               tempOffsetY              = offsetY;
    private boolean             once                     = true;
    private boolean             isDrawEyes               = true;
    private double              sunshineStartX, sunshineStartY, sunshineStopX, sunshineStopY;
    private Paint               mPaint, sunPaint, eyePaint, bgPaint;
    private TextPaint           mTextPaint;
    private RectF               rectF;


    public SunBabyLoadingView(Context context) {
        this(context, null);
    }


    public SunBabyLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SunBabyLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initRes();
    }


    private void initRes() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.parseColor(BG_COLOR));

        sunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunPaint.setStyle(Paint.Style.FILL);
        sunPaint.setStrokeWidth(10);
        sunPaint.setColor(Color.parseColor(BG_COLOR));

        eyePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eyePaint.setStyle(Paint.Style.FILL);
        eyePaint.setStrokeCap(Paint.Cap.ROUND);
        eyePaint.setStrokeJoin(Paint.Join.ROUND);
        eyePaint.setStrokeWidth(1);
        eyePaint.setColor(Color.parseColor(PAINT_COLOR));

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        Typeface RobotoLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        mTextPaint.setTypeface(RobotoLight);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setStrokeJoin(Paint.Join.ROUND);
        bgPaint.setStrokeWidth(1);
        bgPaint.setColor(Color.parseColor(BG_COLOR));

        rectF = new RectF();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize;
        int heightSize;

        Resources r = Resources.getSystem();
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DIAMETER_SIZE, r.getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DIAMETER_SIZE, r.getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int width = getWidth();
        final int height = getHeight();

        lineLength = width * RATIO_LINE_START_X;

        lineStartX = (width - lineLength) * .5f;
        lineStartY = height * RATIO_LINE_START_Y;

        textX = width * .5f;
        textY = lineStartY + (height - lineStartY) * .5f + Math.abs(mTextPaint.descent() + mTextPaint.ascent()) * .5f;

        sunRadius = (lineLength - lineLength * RATIO_ARC_START_X) * .5f;

        maxEyesTurn = (sunRadius + sunPaint.getStrokeWidth() * .5f) * .5f;

        calcAndSetRectPoint();
        calcOffsetAngle();
        initAnimaDriver();
    }


    private void calcOffsetAngle() {
        offsetAngle = (float) (Math.asin(offsetY / sunRadius) * 180 / Math.PI);
    }


    private void calcAndSetRectPoint() {
        float rectLeft = lineStartX + lineLength * .5f - sunRadius;
        float rectTop = lineStartY - sunRadius + offsetY;
        float rectRight = lineLength - rectLeft + 2 * lineStartX;
        float rectBottom = rectTop + 2 * sunRadius;

        rectF.set(rectLeft, rectTop, rectRight, rectBottom);
    }


    private void initAnimaDriver() {
        startSpinAnima();

        final ValueAnimator rise1SlowAnima = initRise1Animator();
        rise1SlowAnima.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                final ValueAnimator riseFastAnima = initRiseFastAnimator();
                riseFastAnima.start();

                riseFastAnima.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        playRisedEyesAnimator();
                        playRisedCyclingAnimator(rise1SlowAnima);
                    }
                });
            }
        });

        rise1SlowAnima.start();
    }


    private void playRisedCyclingAnimator(final ValueAnimator rise1SlowAnima) {
        final ValueAnimator rise2SlowAnima = initRise2SlowAnimator();
        rise2SlowAnima.start();

        rise2SlowAnima.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                final ValueAnimator sinkAnima = initSinkAnimator();
                sinkAnima.start();

                sinkAnima.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rise1SlowAnima.start();
                    }
                });
            }
        });
    }


    private void playRisedEyesAnimator() {
        final ValueAnimator blink2Anima = initBlink2Animator();
        blink2Anima.start();

        blink2Anima.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                final ValueAnimator turnEyesRightAnima = initTurnEyesRightAnimator();
                turnEyesRightAnima.start();

                turnEyesRightAnima.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        final ValueAnimator blink1Anima = initBlink1Animator();

                        blink1Anima.addListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                final ValueAnimator turnEyesLeftAnima = initTurnEyesLeftAnimator();
                                turnEyesLeftAnima.start();
                            }
                        });
                        blink1Anima.start();
                    }
                });
            }
        });
    }


    private ValueAnimator initSinkAnimator() {
        final float endValue = DEFAULT_OFFSET_Y - tempOffsetY;

        final float middleValue = endValue * .5f;

        orectLeft = rectF.left;
        orectTop = rectF.top;
        orectRight = rectF.right;
        orectBottom = rectF.bottom;

        ValueAnimator sinkAnima = ValueAnimator.ofFloat(0, endValue);
        sinkAnima.setDuration(200);
        sinkAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        sinkAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animaValue = Float.parseFloat(animation.getAnimatedValue().toString());

                float ratioValue;
                if (animaValue < middleValue) {
                    ratioValue = animaValue * .5f;
                    rectF.set(orectLeft + ratioValue, orectTop + animaValue, orectRight - ratioValue, orectBottom + animaValue);
                } else {
                    if (once) {
                        orectLeft = rectF.left;
                        orectTop = rectF.top;
                        orectRight = rectF.right;
                        orectBottom = rectF.bottom;
                        once = false;
                    }
                    ratioValue = (animaValue - middleValue) * .5f;
                    rectF.set(orectLeft - ratioValue, orectTop + animaValue, orectRight + ratioValue, orectBottom + animaValue);
                }

                offsetY = tempOffsetY + animaValue;
                calcOffsetAngle();
                postInvalidate();
            }
        });
        sinkAnima.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                once = true;
            }
        });
        return sinkAnima;
    }


    private ValueAnimator initRise2SlowAnimator() {
        ValueAnimator rise2SlowAnima = ValueAnimator.ofFloat(0, SUNSHINE_RISE_HEIGHT * 1.5f);
        rise2SlowAnima.setDuration(2500);
        rise2SlowAnima.setInterpolator(new LinearInterpolator());
        rise2SlowAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animaValue = Float.parseFloat(animation.getAnimatedValue().toString());
                offsetY = tempOffsetY - animaValue;
                calcAndSetRectPoint();
                calcOffsetAngle();
                postInvalidate();
            }
        });
        rise2SlowAnima.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                tempOffsetY = offsetY;
            }
        });
        return rise2SlowAnima;
    }


    private ValueAnimator initTurnEyesLeftAnimator() {
        ValueAnimator turnEyesLeftAnima = ValueAnimator.ofFloat(maxEyesTurn, 0);
        turnEyesLeftAnima.setStartDelay(800);
        turnEyesLeftAnima.setDuration(200);
        turnEyesLeftAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                turnOffsetX = Float.parseFloat(animation.getAnimatedValue().toString());
                postInvalidate();
            }
        });
        return turnEyesLeftAnima;
    }


    private ValueAnimator initBlink1Animator() {
        ValueAnimator blink1Anima = ValueAnimator.ofInt(0, 1);
        blink1Anima.setInterpolator(new LinearInterpolator());
        blink1Anima.setStartDelay(700);
        blink1Anima.setDuration(200);
        blink1Anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animaValue = Integer.parseInt(animation.getAnimatedValue().toString());
                if (animaValue == 0) {
                    isDrawEyes = false;
                } else if (animaValue == 1) {
                    isDrawEyes = true;
                }
                postInvalidate();
            }
        });
        return blink1Anima;
    }


    private ValueAnimator initTurnEyesRightAnimator() {
        ValueAnimator turnEyesRightAnima = ValueAnimator.ofFloat(0, maxEyesTurn);
        turnEyesRightAnima.setStartDelay(500);
        turnEyesRightAnima.setDuration(250);
        turnEyesRightAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                turnOffsetX = Float.parseFloat(animation.getAnimatedValue().toString());
                postInvalidate();
            }
        });
        return turnEyesRightAnima;
    }


    private ValueAnimator initBlink2Animator() {
        ValueAnimator blink2Anima = ValueAnimator.ofInt(0, 1, 0, 1);
        blink2Anima.setInterpolator(new LinearInterpolator());
        blink2Anima.setStartDelay(600);
        blink2Anima.setDuration(500);
        blink2Anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animaValue = Integer.parseInt(animation.getAnimatedValue().toString());
                if (animaValue == 0) {
                    isDrawEyes = false;
                } else if (animaValue == 1) {
                    isDrawEyes = true;
                }
                postInvalidate();
            }
        });
        return blink2Anima;
    }


    private ValueAnimator initRiseFastAnimator() {

        orectLeft = rectF.left;
        orectTop = rectF.top;
        orectRight = rectF.right;
        orectBottom = rectF.bottom;

        final float endValue = SUNSHINE_RISE_HEIGHT * 2.5f;
        final float middleValue = endValue * .5f;
        ValueAnimator riseFastAnima = ValueAnimator.ofFloat(0, endValue);
        riseFastAnima.setDuration(200);
        riseFastAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        riseFastAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animaValue = Float.parseFloat(animation.getAnimatedValue().toString());
                if (animaValue < middleValue) {
                    rectF.set(orectLeft - animaValue, orectTop + animaValue, orectRight + animaValue, orectBottom - animaValue);
                } else {
                    if (once) {
                        orectLeft = rectF.left;
                        orectTop = rectF.top;
                        orectRight = rectF.right;
                        orectBottom = rectF.bottom;
                        once = false;
                    }
                    rectF.set(orectLeft + (animaValue - middleValue), orectTop - (animaValue - middleValue), orectRight - (animaValue - middleValue), orectBottom + (animaValue - middleValue));
                }

                offsetY = tempOffsetY - animaValue;
                calcOffsetAngle();
                postInvalidate();
            }
        });
        riseFastAnima.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                tempOffsetY = offsetY;
                once = true;
            }
        });
        return riseFastAnima;
    }


    private ValueAnimator initRise1Animator() {
        final ValueAnimator rise1SlowAnima = ValueAnimator.ofFloat(0, SUNSHINE_RISE_HEIGHT);
        rise1SlowAnima.setDuration(2700);
        rise1SlowAnima.setInterpolator(new LinearInterpolator());
        rise1SlowAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animaValue = Float.parseFloat(animation.getAnimatedValue().toString());
                offsetY = DEFAULT_OFFSET_Y - animaValue;
                calcAndSetRectPoint();
                calcOffsetAngle();
                postInvalidate();
            }
        });
        rise1SlowAnima.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                tempOffsetY = offsetY;
            }
        });
        return rise1SlowAnima;
    }


    private void startSpinAnima() {
        ValueAnimator spinAnima = ValueAnimator.ofFloat(0, 360);
        spinAnima.setRepeatCount( -1);
        spinAnima.setDuration(24 * 1000);
        spinAnima.setInterpolator(new LinearInterpolator());
        spinAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetSpin = Float.parseFloat(animation.getAnimatedValue().toString());
                postInvalidate();
            }
        });
        spinAnima.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(lineStartX, lineStartY, lineStartX + lineLength, lineStartY, mPaint);
        canvas.drawArc(rectF, -180 + offsetAngle, 180 - offsetAngle * 2, false, sunPaint);

        if (isDrawEyes)
            drawSunEyes(canvas);

        drawSunshine(canvas);

        drawUnderLineView(canvas);
    }


    private void drawUnderLineView(Canvas canvas) {
        canvas.save();
        canvas.drawText(getContext().getString(R.string.please_wait_en), textX, textY, mTextPaint);
        canvas.restore();
    }


    private void drawSunshine(Canvas canvas) {
        for (int a = 0; a <= 360; a += SUNSHINE_SEPARATIO_ANGLE) {
            sunshineStartX = Math.cos(Math.toRadians(a + offsetSpin)) * (sunRadius + SPACE_SUNSHINE + sunPaint.getStrokeWidth()) + getWidth() * .5f;
            sunshineStartY = Math.sin(Math.toRadians(a + offsetSpin)) * (sunRadius + SPACE_SUNSHINE + sunPaint.getStrokeWidth()) + offsetY + lineStartY;

            sunshineStopX = Math.cos(Math.toRadians(a + offsetSpin)) * (sunRadius + SPACE_SUNSHINE + SUNSHINE_LINE_LENGTH + sunPaint.getStrokeWidth()) + getWidth() * .5f;
            sunshineStopY = Math.sin(Math.toRadians(a + offsetSpin)) * (sunRadius + SPACE_SUNSHINE + SUNSHINE_LINE_LENGTH + sunPaint.getStrokeWidth()) + offsetY + lineStartY;
            if (sunshineStartY <= lineStartY && sunshineStopY <= lineStartY) {
                canvas.drawLine((float) sunshineStartX, (float) sunshineStartY, (float) sunshineStopX, (float) sunshineStopY, mPaint);
            }
        }
    }


    private void drawSunEyes(Canvas canvas) {
        float lcx = getWidth() * .5f - (sunRadius + sunPaint.getStrokeWidth() * .5f) * .5f + turnOffsetX;
        float lcy = lineStartY + offsetY - SUN_EYES_RADIUS;

        if (lcy + SUN_EYES_RADIUS >= lineStartY)
            return;

        float rcx = getWidth() * .5f + turnOffsetX;
        float rcy = lcy;

        canvas.drawCircle(lcx, lcy, SUN_EYES_RADIUS, eyePaint);
        canvas.drawCircle(rcx, rcy, SUN_EYES_RADIUS, eyePaint);
    }

}