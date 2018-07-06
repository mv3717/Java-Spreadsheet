package common.api.monitor;

/**
 * The Tracker interface indicates a class can be updated when things of type T
 * change status.
 */
public interface Tracker<T> {

    void update(T changed);

}
