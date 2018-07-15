package top.defaults.colorpicker;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.defaults.view.ColorPickerPopup;
import top.defaults.view.ColorPickerView;
import top.defaults.view.ColorObserver;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.colorPicker) ColorPickerView colorPickerView;
    @BindView(R.id.pickedColor) View pickedColor;
    @BindView(R.id.colorHex) TextView colorHex;

    @OnClick(R.id.resetColor)
    void resetColor() {
        colorPickerView.reset();
    }

    @OnClick(R.id.pickedColor)
    void popup(View v) {
        new ColorPickerPopup.Builder(this)
                .initialColor(colorPickerView.getColor())
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(v, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        v.setBackgroundColor(color);
                        colorPickerView.setInitialColor(color);
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        colorPickerView.subscribe((color, fromUser) -> {
            pickedColor.setBackgroundColor(color);
            colorHex.setText(colorHex(color));
        });

        colorPickerView.setInitialColor(0x7F313C93);
    }

    private String colorHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }
}
