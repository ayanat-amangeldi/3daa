package com.aitu.mst.dto;

public class ResultEntry {
    public int graph_id;
    public InputStats input_stats = new InputStats();
    public AlgoResult prim;
    public AlgoResult kruskal;

    public static class InputStats {
        public int vertices;
        public int edges;
    }
}
