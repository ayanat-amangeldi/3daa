package com.aitu.mst.model;

import java.util.*;

public class Graph {
    private final int n; // количество вершин
    private final List<List<Edge>> adj; // список смежности
    private final List<String> labels;  // имена вершин

    public Graph(List<String> labels) {
        this.n = labels.size();
        this.labels = new ArrayList<>(labels);
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public int size() {
        return n;
    }

    public List<String> getLabels() {
        return labels;
    }

    public int indexOf(String label) {
        return labels.indexOf(label);
    }

    public void addUndirectedEdge(int u, int v, int w) {
        Edge e = new Edge(u, v, w);
        Edge e2 = new Edge(v, u, w);
        adj.get(u).add(e);
        adj.get(v).add(e2);
    }

    public List<List<Edge>> getAdj() {
        return adj;
    }

    public List<Edge> edgesAsListUnique() {
        List<Edge> all = new ArrayList<>();
        boolean[][] seen = new boolean[n][n];
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                int a = Math.min(e.u, e.v);
                int b = Math.max(e.u, e.v);
                if (!seen[a][b]) {
                    seen[a][b] = true;
                    all.add(new Edge(a, b, e.weight));
                }
            }
        }
        return all;
    }
}
