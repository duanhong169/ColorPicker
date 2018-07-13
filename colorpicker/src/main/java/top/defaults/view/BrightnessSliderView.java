package top.defaults.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BrightnessSliderView extends View {

    private int baseColor = Color.WHITE;
    private Paint brightnessPaint;
    private Paint borderPaint;
    private Paint selectorPaint;

    private Path selectorPath;
    private Path currentSelectorPath = new Path();
    private float selectorSize;
    private float currentBrightness;

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
        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setColor(Color.BLACK);
        selectorPath = new Path();
        selectorPath.setFillType(Path.FillType.WINDING);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        applyChanges();
        selectorPath.reset();
        selectorSize = h * 0.33f;
        selectorPath.moveTo(0, 0);
        selectorPath.lineTo(selectorSize * 2, 0);
        selectorPath.lineTo(selectorSize, selectorSize);
        selectorPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        canvas.drawRect(selectorSize, height * 0.33f, width - selectorSize, height, brightnessPaint);
        canvas.drawRect(selectorSize, height * 0.33f, width - selectorSize, height, borderPaint);
        selectorPath.offset(currentBrightness * (width - 2 * selectorSize), 0, currentSelectorPath);
        canvas.drawPath(currentSelectorPath, selectorPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch ( action ) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                updateBrightness(event.getX());
                notifyColor(getColor(), true);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setBaseColor(int color) {
        baseColor = color;
        applyChanges();
        invalidate();
        notifyColor(getColor(), false);
    }

    public void applyChanges() {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        hsv[2] = 0;
        int startColor = Color.HSVToColor(hsv);
        hsv[2] = 1;
        int endColor = Color.HSVToColor(hsv);
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        brightnessPaint.setShader(shader);
    }

    private void updateBrightness(float eventX) {
        float left = selectorSize;
        float right = getWidth() - selectorSize;
        if (eventX < left) eventX = left;
        if (eventX > right) eventX = right;
        currentBrightness = eventX / (right - left);
        invalidate();
    }

    private int getColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        hsv[2] = currentBrightness;
        return Color.HSVToColor(hsv);
    }

    private OnColorListener listener;

    public void setListener(OnColorListener listener) {
        this.listener = listener;
    }

    private void notifyColor(int color, boolean fromUser) {
        if (listener != null) {
            listener.onColor(color, fromUser);
        }
    }

    public void bind(ColorWheelView colorWheelView) {
        if (colorWheelView != null) {
            colorWheelView.setListener(new OnColorListener() {
                @Override
                public void onColor(int color, boolean fromUser) {
                    setBaseColor(color);
                }
            });
        }
    }
}
