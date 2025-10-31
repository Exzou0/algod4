package graph.topo;

import graph.model.Edge;
import graph.model.Graph;
import java.util.*;

public class TopoSort {

    public static List<Integer> kahnSort(Graph dag) {
        int n = dag.size();
        int[] indegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (Edge e : dag.getAdj().get(u)) {
                indegree[e.v]++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indegree[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (Edge e : dag.getAdj().get(u)) {
                indegree[e.v]--;
                if (indegree[e.v] == 0) q.add(e.v);
            }
        }

        if (order.size() != n)
            throw new IllegalStateException("Graph is not a DAG (cycle)");

        return order;
    }
}
