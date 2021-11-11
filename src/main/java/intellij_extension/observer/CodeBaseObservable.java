package intellij_extension.observer;

public interface CodeBaseObservable
{
    void notifyObservers();

    void registerObserver(CodeBaseObserver observer);

    void unregisterObserver(CodeBaseObserver observer);
}
