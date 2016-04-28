package com.andela.movit.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.andela.movit.R;

public class CircleOverlay extends View {

    private float radius;

    private int strokeColor;

    private float width;

    private float height;

    private Paint paint;

    private float strokeWidth;

    public CircleOverlay(Context context) {
        super(context);
    }

    public CircleOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRadius(context, attrs);
        init();
    }

    public CircleOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRadius(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    private void setRadius(Context context, AttributeSet attrs) {
        TypedArray typedArray
                = context.obtainStyledAttributes(attrs, R.styleable.Custom);
        try {
            radius = typedArray.getFloat(R.styleable.Custom_radius, 0.0f);
            radius = dpToPx(radius);
            strokeColor = typedArray.getColor(R.styleable.Custom_stroke_color, Color.GRAY);
            strokeWidth = typedArray.getFloat(R.styleable.Custom_stroke_width, 0.5f);
            strokeWidth = dpToPx(strokeWidth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    public float dpToPx(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float centerX = width / 2;
        float centerY = height / 2;
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(strokeColor);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = MeasureSpec.getSize(widthMeasureSpec);
        this.height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
