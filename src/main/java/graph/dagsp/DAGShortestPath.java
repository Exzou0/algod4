package graph.dagsp;

import graph.model.Edge;
import graph.model.Graph;
import graph.util.Metrics;

import java.util.*;

public class DAGShortestPath {

    private final Graph dag;
    private final int source;
    private final double[] dist;
    private final int[] parent;
    private final Metrics metrics;

    public DAGShortestPath(Graph dag, int source, Metrics metrics) {
        this.dag = dag;
        this.source = source;
        this.metrics = metrics;
        this.dist = new double[dag.size()];
        this.parent = new int[dag.size()];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
    }

    public void compute(List<Integer> topoOrder) {
        metrics.startTimer();
        dist[source] = 0;

        for (int u : topoOrder) {
            if (dist[u] == Double.POSITIVE_INFINITY) continue;

            for (Edge e : dag.getAdj().get(u)) {
                double newDist = dist[u] + e.w;
                if (newDist < dist[e.v]) {
                    dist[e.v] = newDist;
                    parent[e.v] = u;
                    metrics.incCounter("relaxations");
                }
            }
        }
        metrics.stopTimer();
    }

    public double[] getDistances() {
        return dist;
    }

    public List<Integer> reconstructPath(int target) {
        if (dist[target] == Double.POSITIVE_INFINITY) return List.of();
        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = parent[at]) path.add(at);
        Collections.reverse(path);
        return path;
    }

    public long getRelaxations() {
        return metrics.getCounter("relaxations");
    }
}
