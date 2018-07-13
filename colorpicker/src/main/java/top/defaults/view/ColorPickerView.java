package top.defaults.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ColorPickerView extends LinearLayout implements ColorObservable {

    private ColorWheelView colorWheelView;
    private BrightnessSliderView brightnessSliderView;
    private AlphaSliderView alphaSliderView;
    private ColorObservable observableOnDuty;

    private int initialColor = Color.BLACK;
    private float density;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
        boolean enableAlpha = typedArray.getBoolean(R.styleable.ColorPickerView_enableAlpha, false);
        typedArray.recycle();

        colorWheelView = new ColorWheelView(context);
        density = getResources().getDisplayMetrics().density;
        int margin = (int) (8 * density);

        {
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(colorWheelView, params);
        }

        {
            brightnessSliderView = new BrightnessSliderView(context);
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (24 * density));
            params.topMargin = margin * 2;
            addView(brightnessSliderView, params);
            brightnessSliderView.bind(colorWheelView);

            setEnabledAlpha(enableAlpha);
        }

        setPadding(margin, margin, margin, margin);
    }

    public void setInitialColor(int color) {
        initialColor = color;
        colorWheelView.setColor(color);
    }

    public void setEnabledAlpha(boolean enable) {
        if (enable) {
            if (alphaSliderView == null) {
                alphaSliderView = new AlphaSliderView(getContext());
                LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (24 * density));
                params.topMargin = (int) (16 * density);
                addView(alphaSliderView, params);
                alphaSliderView.bind(brightnessSliderView);
            }
            observableOnDuty = alphaSliderView;
        } else {
            if (alphaSliderView != null) {
                alphaSliderView.unbind(brightnessSliderView);
                removeView(alphaSliderView);
                alphaSliderView = null;
            }
            observableOnDuty = brightnessSliderView;
        }
    }

    public void reset() {
        colorWheelView.setColor(initialColor);
    }

    @Override
    public void subscribe(ColorObserver observer) {
        observableOnDuty.subscribe(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        observableOnDuty.unsubscribe(observer);
    }

    @Override
    public int getColor() {
        return observableOnDuty.getColor();
    }
}
