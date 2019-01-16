package top.defaults.colorpicker;

import java.util.ArrayList;
import java.util.List;

class ColorObservableEmitter implements ColorObservable {

    private List<ColorObserver> observers = new ArrayList<>();
    private int color;

    @Override
    public void subscribe(ColorObserver observer) {
        if (observer == null) return;
        observers.add(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        if (observer == null) return;
        observers.remove(observer);
    }

    @Override
    public int getColor() {
        return color;
    }

    void onColor(int color, boolean fromUser, boolean shouldPropagate) {
        this.color = color;
        for (ColorObserver observer : observers) {
            observer.onColor(color, fromUser, shouldPropagate);
        }
    }

}
