import graph.model.Graph;
import graph.model.Edge;
import graph.scc.TarjanSCC;
import graph.topo.CondensationGraph;
import graph.topo.TopoSort;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.DAGLongestPath;
import graph.util.GraphLoader;
import graph.util.Metrics;
import graph.util.SimpleMetrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        File dataDir = new File("data");
        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            System.err.println("No .json files found in /data/");
            return;
        }

        try (FileWriter csv = new FileWriter("data/results.csv")) {
            csv.write("Dataset,V,E,Tarjan(ms),DFS,Topo(ms),Push,Pop,SP(ms),LP(ms),Relax\n");

            System.out.printf("%-15s | %8s | %6s | %10s | %4s | %9s | %5s | %5s | %7s | %7s | %6s%n",
                    "Dataset", "V", "E", "Tarjan(ms)", "DFS", "Topo(ms)",
                    "Push", "Pop", "SP(ms)", "LP(ms)", "Relax");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            for (File file : files) {
                Graph g = GraphLoader.loadFromJson(file.getPath());
                int V = g.size();
                int E = g.edgeCount();

                // Tarjan SCC
                Metrics sccMetrics = new SimpleMetrics();
                TarjanSCC tarjan = new TarjanSCC(g, sccMetrics);
                sccMetrics.startTimer();
                List<List<Integer>> sccs = tarjan.run();
                sccMetrics.stopTimer();

                // Condensation + Topo
                CondensationGraph cond = new CondensationGraph(g, sccs);
                Graph dag = cond.getDAG();

                Metrics topoMetrics = new SimpleMetrics();
                List<Integer> topo = TopoSort.kahnSort(dag, topoMetrics);

                // Autosource
                int[] indeg = new int[dag.size()];
                for (int u = 0; u < dag.size(); u++) {
                    for (Edge e : dag.getAdj().get(u)) indeg[e.v]++;
                }
                int source = 0;
                for (int i = 0; i < indeg.length; i++)
                    if (indeg[i] == 0) { source = i; break; }

                // DAG Shortest Path
                Metrics spMetrics = new SimpleMetrics();
                DAGShortestPath sp = new DAGShortestPath(dag, source, spMetrics);
                sp.compute(topo);

                // DAG Longest Path
                Metrics lpMetrics = new SimpleMetrics();
                DAGLongestPath lp = new DAGLongestPath(dag, lpMetrics);
                lp.compute(topo, source);

                System.out.printf("%-15s | %8d | %6d | %10.3f | %4d | %9.3f | %5d | %5d | %7.3f | %7.3f | %6d%n",
                        file.getName(), V, E,
                        sccMetrics.getTimeMs(),
                        sccMetrics.getCounter("dfs_visits"),
                        topoMetrics.getTimeMs(),
                        topoMetrics.getCounter("pushes"),
                        topoMetrics.getCounter("pops"),
                        spMetrics.getTimeMs(),
                        lpMetrics.getTimeMs(),
                        spMetrics.getCounter("relaxations"));

                csv.write(String.format(Locale.US,
                        "%s,%d,%d,%.3f,%d,%.3f,%d,%d,%.3f,%.3f,%d%n",
                        file.getName(), V, E,
                        sccMetrics.getTimeMs(),
                        sccMetrics.getCounter("dfs_visits"),
                        topoMetrics.getTimeMs(),
                        topoMetrics.getCounter("pushes"),
                        topoMetrics.getCounter("pops"),
                        spMetrics.getTimeMs(),
                        lpMetrics.getTimeMs(),
                        spMetrics.getCounter("relaxations")));
            }


            System.out.println("\nResults saved to results.csv");
        } catch (IOException e) {
            System.err.println("Error writing to results.csv: " + e.getMessage());
        }
    }
}
