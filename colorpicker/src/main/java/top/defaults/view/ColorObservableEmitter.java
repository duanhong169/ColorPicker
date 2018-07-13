package top.defaults.view;

import java.util.ArrayList;
import java.util.List;

class ColorObservableEmitter implements ColorObservable {

    private List<ColorObserver> listeners = new ArrayList<>();

    @Override
    public void subscribe(ColorObserver observer) {
        listeners.add(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        listeners.remove(observer);
    }

    void onColor(int color, boolean fromUser) {
        for (ColorObserver listener : listeners) {
            listener.onColor(color, fromUser);
        }
    }

}
