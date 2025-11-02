package graph.util;

public interface Metrics {
    void startTimer();
    void stopTimer();
    double getTimeMs();

    void incCounter(String name);
    long getCounter(String name);
}
