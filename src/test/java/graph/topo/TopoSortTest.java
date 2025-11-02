package graph.topo;

import graph.model.Graph;
import graph.util.SimpleMetrics;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TopoSortTest {
    @Test
    void testKahnSortOrder() {
        Graph dag = new Graph(5, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(0, 2, 1);
        dag.addEdge(1, 3, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 4, 1);

        List<Integer> order = TopoSort.kahnSort(dag, new SimpleMetrics());

        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(3) < order.indexOf(4));
    }
}
