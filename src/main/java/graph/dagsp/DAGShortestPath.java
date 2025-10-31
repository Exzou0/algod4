package graph.dagsp;

import graph.model.Edge;
import graph.model.Graph;

import java.util.*;

public class DAGShortestPath {

    private final Graph dag;
    private final int source;
    private final double[] dist;
    private final int[] parent;
    private long relaxations = 0;

    public DAGShortestPath(Graph dag, int source) {
        this.dag = dag;
        this.source = source;
        this.dist = new double[dag.size()];
        this.parent = new int[dag.size()];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
    }

    public void compute(List<Integer> topoOrder) {
        dist[source] = 0;
        for (int u : topoOrder) {
            if (dist[u] == Double.POSITIVE_INFINITY) continue;
            for (Edge e : dag.getAdj().get(u)) {
                double newDist = dist[u] + e.w;
                if (newDist < dist[e.v]) {
                    dist[e.v] = newDist;
                    parent[e.v] = u;
                    relaxations++;
                }
            }
        }
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
        return relaxations;
    }
}
