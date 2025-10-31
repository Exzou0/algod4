package graph.dagsp;

import graph.model.Graph;
import graph.topo.TopoSort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAGSPTest {

    @Test
    void testShortestAndLongestPaths() {
        Graph dag = new Graph(6, true);
        dag.addEdge(0, 1, 5);
        dag.addEdge(0, 2, 3);
        dag.addEdge(1, 3, 6);
        dag.addEdge(1, 2, 2);
        dag.addEdge(2, 4, 4);
        dag.addEdge(2, 5, 2);
        dag.addEdge(2, 3, 7);
        dag.addEdge(3, 5, 1);
        dag.addEdge(3, 4, -1);
        dag.addEdge(4, 5, -2);

        List<Integer> topo = TopoSort.kahnSort(dag);

        DAGShortestPath sp = new DAGShortestPath(dag, 0);
        sp.compute(topo);

        DAGLongestPath lp = new DAGLongestPath(dag);
        lp.compute(topo, 0);

        assertTrue(sp.getDistances()[5] < Double.POSITIVE_INFINITY);
        assertTrue(lp.getMaxDistance() > Double.NEGATIVE_INFINITY);
    }
}
