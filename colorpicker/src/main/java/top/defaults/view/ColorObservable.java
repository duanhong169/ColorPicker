package top.defaults.view;

public interface ColorObservable {

    void registerListener(OnColorListener listener);

    void unregisterListener(OnColorListener listener);
}
