package top.defaults.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class ColorSliderView extends View implements ColorObservable {
    protected int baseColor = Color.WHITE;
    private Paint colorPaint;
    private Paint borderPaint;
    private Paint selectorPaint;

    private Path selectorPath;
    private Path currentSelectorPath = new Path();
    protected float selectorSize;
    protected float currentValue;

    private ColorObservableEmitter emitter = new ColorObservableEmitter();

    public ColorSliderView(Context context) {
        this(context, null);
    }

    public ColorSliderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
        configurePaint(colorPaint);
        selectorPath.reset();
        selectorSize = h * 0.25f;
        selectorPath.moveTo(0, 0);
        selectorPath.lineTo(selectorSize * 2, 0);
        selectorPath.lineTo(selectorSize, selectorSize);
        selectorPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height, colorPaint);
        canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height, borderPaint);
        selectorPath.offset(currentValue * (width - 2 * selectorSize), 0, currentSelectorPath);
        canvas.drawPath(currentSelectorPath, selectorPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch ( action ) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                updateValue(event.getX());
                emitter.onColor(assembleColor(), true);
                return true;
        }
        return super.onTouchEvent(event);
    }

    void setBaseColor(int color, boolean fromUser) {
        baseColor = color;
        configurePaint(colorPaint);
        if (!fromUser) {
            // if not set by user (means programmatically), resolve currentValue from color value
            currentValue = resolveValue(color);
            emitter.onColor(color, false);
        } else {
            emitter.onColor(assembleColor(), true);
        }
        invalidate();
    }

    private void updateValue(float eventX) {
        float left = selectorSize;
        float right = getWidth() - selectorSize;
        if (eventX < left) eventX = left;
        if (eventX > right) eventX = right;
        currentValue = (eventX - left) / (right - left);
        invalidate();
    }

    protected abstract float resolveValue(int color);

    protected abstract void configurePaint(Paint colorPaint);

    protected abstract int assembleColor();

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

    private ColorObserver bindListener = new ColorObserver() {
        @Override
        public void onColor(int color, boolean fromUser) {
            setBaseColor(color, fromUser);
        }
    };

    public void bind(ColorObservable colorObservable) {
        if (colorObservable != null) {
            colorObservable.subscribe(bindListener);
        }
    }

    public void unbind(ColorObservable colorObservable) {
        if (colorObservable != null) {
            colorObservable.unsubscribe(bindListener);
        }
    }
}
