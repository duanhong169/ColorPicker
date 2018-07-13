package top.defaults.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BrightnessSliderView extends View {

    private int color = Color.WHITE;
    private Paint brightnessPaint;
    private Paint borderPaint;

    public BrightnessSliderView(Context context) {
        this(context, null);
    }

    public BrightnessSliderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrightnessSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        brightnessPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(0);
        borderPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        applyChanges();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), brightnessPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), borderPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch ( action ) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setColor(int color) {
        this.color = color;
        applyChanges();
        invalidate();
    }

    public void applyChanges() {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = 0;
        int startColor = Color.HSVToColor(hsv);
        hsv[2] = 1;
        int endColor = Color.HSVToColor(hsv);
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        brightnessPaint.setShader(shader);
    }
}
