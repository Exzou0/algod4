package graph.scc;

import graph.model.Graph;
import graph.util.Metrics;
import graph.util.SimpleMetrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    void detectsStronglyConnectedComponents() {
        Graph g = new Graph(8, true);
        g.addEdge(0, 1, 3);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 3, 4);
        g.addEdge(3, 1, 1);
        g.addEdge(4, 5, 2);
        g.addEdge(5, 6, 5);
        g.addEdge(6, 7, 1);

        Metrics metrics = new SimpleMetrics();
        TarjanSCC tarjan = new TarjanSCC(g, metrics);
        List<List<Integer>> comps = tarjan.run();

        assertEquals(6, comps.size());
        assertTrue(comps.stream().anyMatch(c -> c.contains(1) && c.contains(2) && c.contains(3)));
    }

    @Test
    void handlesEmptyGraph() {
        Graph g = new Graph(0, true);
        TarjanSCC tarjan = new TarjanSCC(g, new SimpleMetrics());
        List<List<Integer>> comps = tarjan.run();
        assertTrue(comps.isEmpty(), "Empty graph should have no SCCs");
    }

    @Test
    void handlesSingleVertexGraph() {
        Graph g = new Graph(1, true);
        TarjanSCC tarjan = new TarjanSCC(g, new SimpleMetrics());
        List<List<Integer>> comps = tarjan.run();
        assertEquals(1, comps.size());
        assertEquals(List.of(0), comps.get(0));
    }
}
