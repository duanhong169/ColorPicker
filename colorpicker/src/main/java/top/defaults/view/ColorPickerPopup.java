package top.defaults.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class ColorPickerPopup {

    private Context context;
    private int initialColor;
    private boolean enableAlpha;

    private ColorPickerPopup(Builder builder) {
        this.context = builder.context;
        this.initialColor = builder.initialColor;
        this.enableAlpha = builder.enableAlpha;
    }

    public void show(View anchor, ColorObserver observer) {
        ColorPickerView colorPickerView = new ColorPickerView(context);
        PopupWindow popupWindow = new PopupWindow(colorPickerView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        colorPickerView.setInitialColor(initialColor);
        colorPickerView.setEnabledAlpha(enableAlpha);
        colorPickerView.subscribe(observer);

        if(Build.VERSION.SDK_INT >= 21){
            popupWindow.setElevation(10.0f);
        }

        popupWindow.showAsDropDown(anchor);
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
}
