package graph.topo;

import graph.model.Edge;
import graph.model.Graph;

import java.util.*;

public class CondensationGraph {

    private final Graph original;
    private final List<List<Integer>> sccs;
    private final Map<Integer, Integer> nodeToComponent = new HashMap<>();
    private final Graph dag;

    public CondensationGraph(Graph original, List<List<Integer>> sccs) {
        this.original = original;
        this.sccs = sccs;

        for (int compId = 0; compId < sccs.size(); compId++) {
            for (int node : sccs.get(compId)) {
                nodeToComponent.put(node, compId);
            }
        }
        this.dag = buildCondensationGraph();
    }

    private Graph buildCondensationGraph() {
        int n = sccs.size();
        Graph g = new Graph(n, true);

        Set<String> addedEdges = new HashSet<>();

        for (int u = 0; u < original.size(); u++) {
            for (Edge e : original.getAdj().get(u)) {
                int compU = nodeToComponent.get(u);
                int compV = nodeToComponent.get(e.v);
                if (compU != compV) {
                    String key = compU + "->" + compV;
                    if (addedEdges.add(key)) {
                        g.addEdge(compU, compV, e.w);
                    }
                }
            }
        }
        return g;
    }

    public Graph getDAG() {
        return dag;
    }

    public Map<Integer, Integer> getNodeToComponent() {
        return nodeToComponent;
    }
}
