package top.defaults.view;

import java.util.ArrayList;
import java.util.List;

class ColorObservableSource implements ColorObservable {

    private List<OnColorListener> listeners = new ArrayList<>();

    @Override
    public void registerListener(OnColorListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(OnColorListener listener) {
        listeners.remove(listener);
    }

    void notifyColor(int color, boolean fromUser) {
        for (OnColorListener listener : listeners) {
            listener.onColor(color, fromUser);
        }
    }

}
