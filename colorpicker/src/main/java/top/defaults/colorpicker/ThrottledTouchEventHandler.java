package top.defaults.colorpicker;

import android.view.MotionEvent;

class ThrottledTouchEventHandler {

    private int minInterval = Constants.EVENT_MIN_INTERVAL;
    private Updatable updatable;
    private long lastPassedEventTime = 0;

    ThrottledTouchEventHandler(Updatable updatable) {
        this(Constants.EVENT_MIN_INTERVAL, updatable);
    }

    private ThrottledTouchEventHandler(int minInterval, Updatable updatable) {
        this.minInterval = minInterval;
        this.updatable = updatable;
    }

    void onTouchEvent(MotionEvent event) {
        if (updatable == null) return;
        long current = System.currentTimeMillis();
        if (current - lastPassedEventTime <= minInterval) {
            return;
        }
        lastPassedEventTime = current;
        updatable.update(event);
    }
}
