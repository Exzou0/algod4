package graph.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import graph.model.Edge;
import graph.model.Graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class GraphGenerator {
    private static final Random rnd = new Random(42);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();

        generateGroup("small", 3, 6, 10);
        generateGroup("medium", 3, 10, 20);
        generateGroup("large", 3, 20, 50);

        System.out.println("Graph datasets generated in /data/");
    }

    private static void generateGroup(String name, int count, int minN, int maxN) throws IOException {
        for (int i = 1; i <= count; i++) {
            int n = rnd.nextInt(maxN - minN + 1) + minN;
            List<Edge> edges = new ArrayList<>();


            for (int e = 0; e < n * 2; e++) {
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                if (u != v) {
                    edges.add(new Edge(u, v, 1 + rnd.nextInt(10)));
                }
            }


            Map<String, Object> graph = new LinkedHashMap<>();
            graph.put("directed", true);
            graph.put("n", n);
            graph.put("edges", edges);
            graph.put("source", rnd.nextInt(n));
            graph.put("weight_model", "edge");

            String filename = String.format("data/%s%d.json", name, i);
            try (FileWriter fw = new FileWriter(filename)) {
                gson.toJson(graph, fw);
            }
            System.out.printf("Generated %s (n=%d, edges=%d)%n", filename, n, edges.size());
        }
    }
}
