package graph.model;

import java.util.*;

public class Graph {
    private final int n;
    private final boolean directed;
    private final Map<Integer, List<Edge>> adj;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new HashMap<>();
        for (int i = 0; i < n; i++) adj.put(i, new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) adj.get(v).add(new Edge(v, u, w));
    }

    public int size() {
        return n;
    }

    public Map<Integer, List<Edge>> getAdj() {
        return adj;
    }

    public boolean isDirected() {
        return directed;
    }
}
