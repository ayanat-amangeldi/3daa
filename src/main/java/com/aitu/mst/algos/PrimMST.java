package com.aitu.mst.algos;

import com.aitu.mst.model.Edge;
import com.aitu.mst.model.Graph;
import java.util.*;

public class PrimMST {

    public static class Result {
        public final List<Edge> edges;
        public final int totalCost;
        public final long operations;
        public final double timeMs;

        public Result(List<Edge> edges, int totalCost, long operations, double timeMs) {
            this.edges = edges;
            this.totalCost = totalCost;
            this.operations = operations;
            this.timeMs = timeMs;
        }
    }

    public static Result run(Graph g) {
        long ops = 0;
        long start = System.nanoTime();

        int n = g.size();
        boolean[] used = new boolean[n];
        int[] minEdge = new int[n];
        int[] parent = new int[n];
        Arrays.fill(minEdge, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        minEdge[0] = 0;
        int total = 0;

        for (int i = 0; i < n; i++) {
            int v = -1;
            for (int j = 0; j < n; j++) {
                ops++;
                if (!used[j] && (v == -1 || minEdge[j] < minEdge[v])) v = j;
            }
            if (minEdge[v] == Integer.MAX_VALUE) break;
            used[v] = true;
            if (parent[v] != -1) total += minEdge[v];

            for (Edge e : g.getAdj().get(v)) {
                ops++;
                if (!used[e.v] && e.weight < minEdge[e.v]) {
                    minEdge[e.v] = e.weight;
                    parent[e.v] = v;
                }
            }
        }

        List<Edge> mst = new ArrayList<>();
        for (int v = 1; v < n; v++) {
            if (parent[v] != -1)
                mst.add(new Edge(parent[v], v, minEdge[v]));
        }

        double elapsed = (System.nanoTime() - start) / 1_000_000.0;
        return new Result(mst, total, ops, elapsed);
    }
}
