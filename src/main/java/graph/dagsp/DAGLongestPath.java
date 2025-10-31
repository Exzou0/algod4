package graph.dagsp;

import graph.model.Edge;
import graph.model.Graph;

import java.util.*;

public class DAGLongestPath {

    private final Graph dag;
    private final double[] dist;
    private final int[] parent;
    private long relaxations = 0;

    public DAGLongestPath(Graph dag) {
        this.dag = dag;
        this.dist = new double[dag.size()];
        this.parent = new int[dag.size()];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(parent, -1);
    }

    public void compute(List<Integer> topoOrder, int source) {
        dist[source] = 0;
        for (int u : topoOrder) {
            if (dist[u] == Double.NEGATIVE_INFINITY) continue;
            for (Edge e : dag.getAdj().get(u)) {
                double newDist = dist[u] + e.w;
                if (newDist > dist[e.v]) {
                    dist[e.v] = newDist;
                    parent[e.v] = u;
                    relaxations++;
                }
            }
        }
    }

    public double getMaxDistance() {
        return Arrays.stream(dist).max().orElse(Double.NEGATIVE_INFINITY);
    }

    public int getCriticalEndVertex() {
        double max = getMaxDistance();
        for (int i = 0; i < dist.length; i++)
            if (dist[i] == max) return i;
        return -1;
    }

    public List<Integer> reconstructCriticalPath() {
        int end = getCriticalEndVertex();
        if (end == -1) return List.of();

        List<Integer> path = new ArrayList<>();
        for (int at = end; at != -1; at = parent[at]) path.add(at);
        Collections.reverse(path);
        return path;
    }

    public long getRelaxations() {
        return relaxations;
    }
}
