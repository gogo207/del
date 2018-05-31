package com.delex.utility;

/**
 * Created by Shubham on 6/1/17.
 */
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

/**
 * <h>WaveDrawable</h>
 * <P>
 *     Class to drwa wave animation
 * </P>
 */
public class WaveDrawable extends Drawable
{

    private Paint wavePaint;
    private int color;
    private int number=0;
    private int radius;
    private int first_innerRadius=0;
    private int second_innerRadius=0;
    private int third_innerRadius=0;
    private long animationTime = 2000;
    protected float waveScale;
    protected int alpha;
    private Interpolator waveInterpolator;
    private Interpolator alphaInterpolator;
    private Animator animator;
    private AnimatorSet animatorSet;

    /**
     * @param color         color
     * @param radius        radius
     * @param animationTime time
     */
    public WaveDrawable(int color, int radius, long animationTime) {

        this(color, radius);
        this.animationTime = animationTime;
    }

    /**
     * @param color  colro
     * @param radius radius
     */
    public WaveDrawable(int color, int radius)
    {
        this.color = color;
        this.radius = radius;
        this.third_innerRadius=radius-35;
        this.second_innerRadius=radius-70;
        this.first_innerRadius=radius-105;
        this.waveScale = 0f;
        this.alpha = 255;
        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animatorSet = new AnimatorSet();
    }

    @Override
    public void draw(Canvas canvas)
    {
        final Rect bounds = getBounds();
        // circle
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setColor(color);
        wavePaint.setAlpha(alpha);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(),radius * waveScale, wavePaint);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(),first_innerRadius * waveScale, wavePaint);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(),second_innerRadius * waveScale, wavePaint);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(),third_innerRadius * waveScale, wavePaint);
    }

    /**
     * @param interpolator interpolator
     */
    public void setWaveInterpolator(Interpolator interpolator) {
        this.waveInterpolator = interpolator;
    }

    /**
     * @param interpolator interpolator
     */
    public void setAlphaInterpolator(Interpolator interpolator) {
        this.alphaInterpolator = interpolator;
    }

    public void startAnimation() {
        animator = generateAnimation();
        animator.start();
    }

    public void stopAnimation() {

        if (animator.isRunning()) {
            animator.end();
        }
    }


    public boolean isAnimationRunning() {
        if (animator != null) {
            return animator.isRunning();
        }
        return false;
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        wavePaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return wavePaint.getAlpha();
    }


    protected void setWaveScale(float waveScale) {
        this.waveScale = waveScale;
        invalidateSelf();
    }

    protected float getWaveScale() {
        return waveScale;
    }

    private Animator generateAnimation() {

        //Wave animation
        ObjectAnimator waveAnimator = ObjectAnimator.ofFloat(this, "waveScale", 0f, 1f);
        waveAnimator.setDuration(animationTime);
        if (waveInterpolator != null) {
            waveAnimator.setInterpolator(waveInterpolator);
        }
        //The animation is repeated
        waveAnimator.setRepeatCount(Animation.INFINITE);
        waveAnimator.setRepeatMode(Animation.INFINITE);

        //alpha animation
        ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "alpha", 255, 0);
        alphaAnimator.setDuration(animationTime);
        if (alphaInterpolator != null) {
            alphaAnimator.setInterpolator(alphaInterpolator);
        }
        alphaAnimator.setRepeatCount(Animation.INFINITE);
        alphaAnimator.setRepeatMode(Animation.INFINITE);
        animatorSet.playTogether(waveAnimator, alphaAnimator);
        return animatorSet;
    }
}