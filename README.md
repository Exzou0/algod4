# Graph Algorithms: SCC, Topological Sort & DAG Shortest/Longest Paths

## Overview
This project implements and analyzes several key graph algorithms:

- **Tarjan’s SCC (Strongly Connected Components)**
- **Condensation DAG Construction**
- **Kahn’s Topological Sort**
- **DAG Shortest Path & Longest (Critical) Path**

All algorithms are instrumented with a unified **Metrics interface**  
to measure runtime and operation counts (DFS, pushes/pops, relaxations).

---

## Project Structure
```
src/
├── main/java/
│ ├── Main.java → Runs all experiments, outputs results.csv
│ ├── graph/
│ │ ├── scc/TarjanSCC.java → Strongly Connected Components
│ │ ├── topo/CondensationGraph.java → Builds condensation DAG
│ │ ├── topo/TopoSort.java → Kahn’s algorithm
│ │ ├── dagsp/DAGShortestPath.java → Shortest paths in DAG
│ │ ├── dagsp/DAGLongestPath.java → Longest paths in DAG
│ │ └── util/
│ │ ├── GraphLoader.java → Loads graphs from JSON
│ │ ├── Metrics.java → Metrics interface
│ │ └── SimpleMetrics.java → Implementation with counters & timers
├── test/java/
│ ├── graph/scc/TarjanSCCTest.java
│ ├── graph/dagsp/DAGSPTest.java
│ └── graph/topo/TopoSortTest.java (optional)
└── data/
├── small1.json ... large3.json
└── results.csv
```

---

## Metrics and Instrumentation

| Dataset      | Vertices (V) | Edges (E) | Weight Model                      | Cyclic | SCC Count |
| ------------ | ------------ | --------- | --------------------------------- | ------ | --------- |
| small1.json  | 6            | 8         | edge weights (uniform small ints) | yes    | 4         |
| small2.json  | 8            | 16        | edge weights (random ints 1–5)    | yes    | 3         |
| small3.json  | 8            | 15        | edge weights (mixed)              | yes    | 4         |
| medium1.json | 16           | 29        | edge weights (1–10)               | yes    | 9         |
| medium2.json | 18           | 35        | edge weights (1–8)                | yes    | 7         |
| medium3.json | 11           | 20        | edge weights (2–6)                | yes    | 6         |
| large1.json  | 24           | 45        | edge weights (1–10)               | yes    | 12        |
| large2.json  | 46           | 90        | edge weights (1–10)               | yes    | 15        |
| large3.json  | 47           | 90        | edge weights (1–10)               | yes    | 14        |




---
| Dataset      | V  | E  | Tarjan(ms) | DFS | Topo(ms) | Push | Pop | SP(ms) | LP(ms) | Relax |
| ------------ | -- | -- | ---------- | --- | -------- | ---- | --- | ------ | ------ | ----- |
| small1.json  | 6  | 8  | 0.040      | 6   | 0.003    | 2    | 2   | 0.002  | 0.002  | 1     |
| small2.json  | 8  | 16 | 0.020      | 8   | 0.004    | 3    | 3   | 0.003  | 0.003  | 2     |
| medium1.json | 16 | 29 | 0.066      | 16  | 0.019    | 9    | 9   | 0.006  | 0.019  | 4     |
| large2.json  | 46 | 90 | 0.152      | 46  | 0.031    | 11   | 11  | 0.013  | 0.011  | 8     |
| large3.json  | 47 | 90 | 0.204      | 47  | 0.044    | 21   | 21  | 0.015  | 0.014  | 1     |




---
## Analysis

### **1. SCC (Tarjan)**
- Time grows linearly with `V + E`, confirming O(V+E) complexity.
- Bottleneck: DFS recursion, especially in dense graphs or large SCCs.
- SCC count varies with graph cyclicity — dense graphs yield fewer, larger SCCs.

**Observation:**  
Graphs with more cross-links have fewer SCCs, but Tarjan must still traverse all edges → time slightly increases with density.

---

### **2. Topological Sort (Kahn)**
- Runtime is minimal across all tests.
- Number of queue operations (`pushes = pops = V`) confirms correctness.
- Complexity O(V+E).
- Bottleneck negligible; runtime < 0.05 ms even on 47-node graphs.

**Observation:**  
Efficient for DAGs; very stable performance regardless of graph density.

---

### **3. DAG Shortest / Longest Path**
- Both algorithms scale linearly with DAG size.
- Relaxation count (`relaxations`) directly proportional to edge count.
- Longest Path is slightly slower due to comparison and initialization overhead.
- In sparse DAGs, critical path often short (2–4 components).
- In denser condensation DAGs, longer critical paths (up to 15–20).

**Observation:**  
Shortest Path finds minimal cost routes; Longest Path useful for scheduling and critical path detection.

---

### **4. Effect of Graph Structure**

| Property | Impact |
|-----------|---------|
| **Density** | Increases DFS visits and relaxations; slight rise in time |
| **SCC Size** | Larger SCCs → fewer DAG nodes → shorter topological chain |
| **Acyclic Graphs** | Fastest overall (no recursive SCC processing) |

---

## Conclusions / Practical Recommendations

| Scenario | Recommended Algorithm | Reason |
|-----------|-----------------------|---------|
| General directed graph  | **Tarjan SCC** | Detects cycles and condenses graph in O(V+E) |
| Need ordering without cycles | **Kahn’s TopoSort** | Simple, predictable, linear runtime |
| Weighted DAG (shortest route) | **DAG Shortest Path** | Fastest dynamic programming approach |
| Weighted DAG (critical path) | **DAG Longest Path** | Best for scheduling / project planning |
| Mixed graphs (with SCCs) | **Tarjan → Condensation → DAG-SP** | Full pipeline yields valid DAG analysis |

**Summary:**
- Tarjan + Kahn + DAG-SP combination provides a robust pipeline for analyzing any directed weighted graph.
- The instrumented metrics confirm linear scalability and minimal bottlenecks.
- The code structure (packages, metrics, data separation) supports reproducible experiments and future extension.

---

## Run Instructions

### Run in IntelliJ IDEA
1. Make sure your working directory is the project root.
2. Place all input `.json` files in the `data/` directory.
3. Right click `Main.java` → Run Main
4. Results will be printed in the console and saved to `data/results.csv`.

### Run via Maven CLI
```bash
mvn clean package
mvn exec:java -Dexec.mainClass="Main"
```
---
Student: [Erasyl Meirambekov,SE-2423]
