package com.aitu.mst.model;

public class Edge implements Comparable<Edge> {
    public final int u;
    public final int v;
    public final int weight;

    public Edge(int u, int v, int weight) {
        if (u == v) throw new IllegalArgumentException("No self-loops allowed");
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return "(" + u + "-" + v + ", w=" + weight + ")";
    }
}
