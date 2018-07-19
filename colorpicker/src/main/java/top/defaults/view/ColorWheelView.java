package top.defaults.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * HSV color wheel
 */
public class ColorWheelView extends View implements ColorObservable {

    private float radius;
    private float centerX;
    private float centerY;

    private Paint huePaint;
    private Paint saturationPaint;
    private Paint selectorPaint;

    private static final int SELECTOR_RADIUS_DP = 9;
    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 3;

    private PointF currentPoint = new PointF();
    private int currentColor = Color.MAGENTA;

    private ColorObservableEmitter emitter = new ColorObservableEmitter();

    public ColorWheelView(Context context) {
        this(context, null);
    }

    public ColorWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        huePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        saturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setColor(Color.BLACK);
        selectorPaint.setStyle(Paint.Style.STROKE);
        selectorPaint.setStrokeWidth(2);

        selectorRadiusPx = SELECTOR_RADIUS_DP * getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        width = height = Math.min(maxWidth, maxHeight);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * 0.5f - selectorRadiusPx;
        if (radius < 0) return;
        centerX = netWidth * 0.5f;
        centerY = netHeight * 0.5f;
        setColor(currentColor);

        Shader hueShader = new SweepGradient(centerX, centerY,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED},
                null);
        huePaint.setShader(hueShader);

        Shader saturationShader = new RadialGradient(centerX, centerY, radius,
                Color.WHITE, 0x00FFFFFF, Shader.TileMode.CLAMP);
        saturationPaint.setShader(saturationShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, huePaint);
        canvas.drawCircle(centerX, centerY, radius, saturationPaint);
        canvas.drawLine(currentPoint.x - selectorRadiusPx, currentPoint.y,
                currentPoint.x + selectorRadiusPx, currentPoint.y, selectorPaint);
        canvas.drawLine(currentPoint.x, currentPoint.y - selectorRadiusPx,
                currentPoint.x, currentPoint.y + selectorRadiusPx, selectorPaint);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 0.66f, selectorPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                emitter.onColor(getColorAtPoint(x, y), true);
                updateSelector(x, y);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int getColorAtPoint(float eventX, float eventY) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        float[] hsv = {0, 0, 1};
        hsv[0] = (float) (Math.atan2(y, -x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (r / radius)));
        return Color.HSVToColor(hsv);
    }

    public void setColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float r = hsv[1] * radius;
        float radian = (float) (hsv[0] / 180f * Math.PI);
        updateSelector((float) (r * Math.cos(radian) + centerX), (float) (-r * Math.sin(radian) + centerY));
        currentColor = color;
        emitter.onColor(color, false);
    }

    private void updateSelector(float eventX, float eventY) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        if (r > radius) {
            x *= radius / r;
            y *= radius / r;
        }
        currentPoint.x = x + centerX;
        currentPoint.y = y + centerY;
        invalidate();
    }

    @Override
    public void subscribe(ColorObserver observer) {
        emitter.subscribe(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        emitter.unsubscribe(observer);
    }

    @Override
    public int getColor() {
        return emitter.getColor();
    }
}
