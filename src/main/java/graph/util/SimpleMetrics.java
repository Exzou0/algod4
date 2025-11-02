package graph.util;

import java.util.HashMap;
import java.util.Map;

public class SimpleMetrics implements Metrics {
    private long startTime;
    private long endTime;
    private final Map<String, Long> counters = new HashMap<>();

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        endTime = System.nanoTime();
    }

    @Override
    public double getTimeMs() {
        return (endTime - startTime) / 1e6;
    }

    @Override
    public void incCounter(String name) {
        counters.put(name, counters.getOrDefault(name, 0L) + 1);
    }

    @Override
    public long getCounter(String name) {
        return counters.getOrDefault(name, 0L);
    }
}
