
import graph.model.Graph;
import graph.model.Edge;
import graph.scc.TarjanSCC;
import graph.topo.CondensationGraph;
import graph.topo.TopoSort;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.DAGLongestPath;
import graph.util.GraphLoader;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("data/medium1.json");
        System.out.println("File: " + file.getName());

        //Load graph
        Graph g = GraphLoader.loadFromJson(file.getPath());
        System.out.println("Graph loaded. Vertices: " + g.size());

        // Run Tarjan SCC
        TarjanSCC tarjan = new TarjanSCC(g);
        long t1 = System.nanoTime();
        List<List<Integer>> sccs = tarjan.run();
        long t2 = System.nanoTime();
        System.out.printf("SCCs found: %d (time: %.3f ms)%n", sccs.size(), (t2 - t1) / 1e6);
        for (int i = 0; i < sccs.size(); i++) {
            System.out.printf("  Component %d: %s%n", i, sccs.get(i));
        }

        // Build condensation DAG
        CondensationGraph cond = new CondensationGraph(g, sccs);
        Graph dag = cond.getDAG();
        System.out.println("\nCondensation DAG built with " + dag.size() + " nodes");

        // Print DAG edges
        System.out.println("DAG edges:");
        for (int u = 0; u < dag.size(); u++) {
            for (Edge e : dag.getAdj().get(u)) {
                System.out.printf("  %d → %d (w=%d)%n", e.u, e.v, e.w);
            }
        }

        int[] indeg = new int[dag.size()];
        for (int u = 0; u < dag.size(); u++) {
            for (Edge e : dag.getAdj().get(u)) {
                indeg[e.v]++;
            }
        }

        int source = -1;
        for (int i = 0; i < dag.size(); i++) {
            if (indeg[i] == 0) {
                source = i;
                break;
            }
        }
        if (source == -1) source = 0;
        System.out.println("\nAuto-selected source component: " + source);

        // Topological order
        List<Integer> order = TopoSort.kahnSort(dag);
        System.out.println("Topological order: " + order);

        // Shortest Path in DAG
        long startSP = System.nanoTime();
        DAGShortestPath sp = new DAGShortestPath(dag, source);
        sp.compute(order);
        long endSP = System.nanoTime();

        System.out.println("\nShortest paths from component " + source + ":");
        double[] dist = sp.getDistances();
        for (int i = 0; i < dist.length; i++) {
            System.out.printf("  to %d = %s%n", i, (Double.isInfinite(dist[i]) ? "∞" : dist[i]));
        }
        System.out.printf("Relaxations: %d, Time: %.3f ms%n", sp.getRelaxations(), (endSP - startSP) / 1e6);

        //  Longest Path
        long startLP = System.nanoTime();
        DAGLongestPath lp = new DAGLongestPath(dag);
        lp.compute(order, source);
        long endLP = System.nanoTime();

        System.out.println("\nCritical path: " + lp.reconstructCriticalPath());
        System.out.println("Critical length: " + lp.getMaxDistance());
        System.out.printf("Relaxations: %d, Time: %.3f ms%n", lp.getRelaxations(), (endLP - startLP) / 1e6);


        double totalMs = (t2 - t1 + endSP - startSP + endLP - startLP) / 1e6;
        System.out.printf("%n✅ Summary:%n");
        System.out.printf("  SCC time: %.3f ms%n", (t2 - t1) / 1e6);
        System.out.printf("  Shortest Path: %.3f ms%n", (endSP - startSP) / 1e6);
        System.out.printf("  Longest Path: %.3f ms%n", (endLP - startLP) / 1e6);
        System.out.printf("  Total time: %.3f ms%n", totalMs);
    }
}
