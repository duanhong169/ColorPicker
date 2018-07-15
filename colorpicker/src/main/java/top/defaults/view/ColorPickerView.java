package top.defaults.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import top.defaults.logger.Logger;

public class ColorPickerView extends LinearLayout implements ColorObservable {

    private ColorWheelView colorWheelView;
    private BrightnessSliderView brightnessSliderView;
    private AlphaSliderView alphaSliderView;
    private ColorObservable observableOnDuty;

    private int initialColor = Color.BLACK;

    private int sliderMargin;
    private int sliderHeight;

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
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (8 * density);
        sliderMargin = 2 * margin;
        sliderHeight = (int) (24 * density);

        {
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(colorWheelView, params);
        }

        {
            brightnessSliderView = new BrightnessSliderView(context);
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight);

            params.topMargin = sliderMargin;
            addView(brightnessSliderView, params);
            brightnessSliderView.bind(colorWheelView);

            setEnabledAlpha(enableAlpha);
        }

        setPadding(margin, margin, margin, margin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (BuildConfig.DEBUG) {
            Logger.d("maxWidth: %d, maxHeight: %d", maxWidth, maxHeight);
        }

        int desiredWidth = maxHeight - (getPaddingTop() + getPaddingBottom()) + (getPaddingLeft() + getPaddingRight());
        if (alphaSliderView == null) {
            desiredWidth -= (sliderMargin + sliderHeight);
        } else {
            desiredWidth -= 2 * (sliderMargin + sliderHeight);
        }

        if (BuildConfig.DEBUG) {
            Logger.d("desiredWidth: %d", desiredWidth);
        }

        int width = Math.min(maxWidth, desiredWidth);
        int height = width - (getPaddingLeft() + getPaddingRight()) + (getPaddingTop() + getPaddingBottom());
        if (alphaSliderView == null) {
            height += (sliderMargin + sliderHeight);
        } else {
            height += 2 * (sliderMargin + sliderHeight);
        }

        if (BuildConfig.DEBUG) {
            Logger.d("width: %d, height: %d", width, height);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    public void setInitialColor(int color) {
        initialColor = color;
        colorWheelView.setColor(color);
    }

    public void setEnabledAlpha(boolean enable) {
        if (enable) {
            if (alphaSliderView == null) {
                alphaSliderView = new AlphaSliderView(getContext());
                LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight);
                params.topMargin = sliderMargin;
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
