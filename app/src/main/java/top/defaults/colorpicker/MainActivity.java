package top.defaults.colorpicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.defaults.view.BrightnessSliderView;
import top.defaults.view.ColorWheelView;
import top.defaults.view.OnColorListener;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.colorWheel) ColorWheelView colorWheel;
    @BindView(R.id.brightnessSlider) BrightnessSliderView brightnessSlider;
    @BindView(R.id.pickedColor) View pickedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        brightnessSlider.bind(colorWheel);
        brightnessSlider.setListener(new OnColorListener() {
            @Override
            public void onColor(int color, boolean fromUser) {
                pickedColor.setBackgroundColor(color);
            }
        });
    }
}
