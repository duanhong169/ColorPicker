package top.defaults.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ColorPickerPopup {

    private Context context;
    private int initialColor;
    private boolean enableAlpha;

    private ColorPickerPopup(Builder builder) {
        this.context = builder.context;
        this.initialColor = builder.initialColor;
        this.enableAlpha = builder.enableAlpha;
    }

    public void show(View parent, final ColorPickerObserver observer) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;

        @SuppressLint("InflateParams")
        View layout = inflater.inflate(R.layout.top_defaults_view_color_picker_popup, null);

        View rootView = parent.getRootView();
        int width = rootView.getWidth();
        int popupWidth = (int) (width - 2 * 16 * context.getResources().getDisplayMetrics().density);

        final ColorPickerView colorPickerView = layout.findViewById(R.id.colorPickerView);
        final PopupWindow popupWindow = new PopupWindow(layout, popupWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        colorPickerView.setInitialColor(initialColor);
        colorPickerView.setEnabledAlpha(enableAlpha);
        colorPickerView.subscribe(observer);
        layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        layout.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (observer != null) {
                    observer.onColorPicked(colorPickerView.getColor());
                }
            }
        });
        final View colorIndicator = layout.findViewById(R.id.colorIndicator);
        final TextView colorHex = layout.findViewById(R.id.colorHex);

        colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser) {
                colorIndicator.setBackgroundColor(color);
                colorHex.setText(colorHex(color));
            }
        });

        if(Build.VERSION.SDK_INT >= 21){
            popupWindow.setElevation(10.0f);
        }

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public static class Builder {

        private Context context;
        private int initialColor = Color.MAGENTA;
        private boolean enableAlpha = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder initialColor(int color) {
            initialColor = color;
            return this;
        }

        public Builder enableAlpha(boolean enable) {
            enableAlpha = enable;
            return this;
        }

        public ColorPickerPopup build() {
            return new ColorPickerPopup(this);
        }
    }

    private String colorHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }

    public interface ColorPickerObserver extends ColorObserver {
        void onColorPicked(int color);
    }
}
