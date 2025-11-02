package graph.scc;

import graph.model.Graph;
import graph.util.Metrics;

import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;

    private final int[] ids;
    private final int[] low;
    private final boolean[] onStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<List<Integer>> sccs = new ArrayList<>();

    private int id = 0;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        int n = graph.size();
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        Arrays.fill(ids, -1);
    }

    public List<List<Integer>> run() {
        for (int i = 0; i < graph.size(); i++) {
            if (ids[i] == -1) dfs(i);
        }
        return sccs;
    }

    private void dfs(int at) {
        metrics.incCounter("dfs_visits");

        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = id++;

        for (var e : graph.getAdj().get(at)) {
            metrics.incCounter("dfs_edges");
            int to = e.v;
            if (ids[to] == -1) {
                dfs(to);
                low[at] = Math.min(low[at], low[to]);
            } else if (onStack[to]) {
                low[at] = Math.min(low[at], ids[to]);
            }
        }

        if (ids[at] == low[at]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                scc.add(node);
                if (node == at) break;
            }
            sccs.add(scc);
        }
    }
}
