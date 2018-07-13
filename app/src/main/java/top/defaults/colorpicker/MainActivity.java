package top.defaults.colorpicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.defaults.view.BrightnessSliderView;
import top.defaults.view.ColorWheelView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.colorWheel) ColorWheelView colorWheel;
    @BindView(R.id.brightnessSlider) BrightnessSliderView brightnessSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        colorWheel.setListener(new ColorWheelView.OnColorListener() {
            @Override
            public void onColor(int color, boolean fromUser) {
                brightnessSlider.setColor(color);
            }
        });
    }
}
