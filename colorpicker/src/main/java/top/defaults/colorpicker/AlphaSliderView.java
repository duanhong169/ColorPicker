package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class AlphaSliderView extends ColorSliderView {

    public AlphaSliderView(Context context) {
        super(context);
    }

    public AlphaSliderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected float resolveValue(int color) {
        return Color.alpha(color) / 255.f;
    }

    protected void configurePaint(Paint colorPaint) {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        int startColor = Color.HSVToColor(0, hsv);
        int endColor = Color.HSVToColor(255, hsv);
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);
    }

    protected int assembleColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        int alpha = (int) (currentValue * 255);
        return Color.HSVToColor(alpha, hsv);
    }
}
