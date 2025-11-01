package graph.util;

import com.google.gson.*;
import graph.model.Edge;
import graph.model.Graph;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphLoader {

    public static Graph loadFromJson(String filename) throws IOException {
        JsonObject obj = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();

        boolean directed = obj.get("directed").getAsBoolean();
        int n = obj.get("n").getAsInt();

        Graph g = new Graph(n, directed);

        JsonArray edges = obj.getAsJsonArray("edges");
        for (JsonElement e : edges) {
            JsonObject edge = e.getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();
            int w = edge.get("w").getAsInt();
            g.addEdge(u, v, w);
        }

        return g;
    }
}
