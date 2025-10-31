package graph.scc;

import graph.model.Graph;
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

        TarjanSCC tarjan = new TarjanSCC(g);
        List<List<Integer>> comps = tarjan.run();

        assertEquals(6, comps.size());
        assertTrue(comps.stream().anyMatch(c -> c.contains(1) && c.contains(2) && c.contains(3)));
    }
}
