package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class BrightnessSliderView extends ColorSliderView {

    public BrightnessSliderView(Context context) {
        super(context);
    }

    public BrightnessSliderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BrightnessSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected float resolveValue(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv[2];
    }

    protected void configurePaint(Paint colorPaint) {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        hsv[2] = 0;
        int startColor = Color.HSVToColor(hsv);
        hsv[2] = 1;
        int endColor = Color.HSVToColor(hsv);
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);
    }

    protected int assembleColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        hsv[2] = currentValue;
        return Color.HSVToColor(hsv);
    }
}
