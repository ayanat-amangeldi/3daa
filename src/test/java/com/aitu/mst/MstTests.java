package com.aitu.mst;

import com.aitu.mst.algos.KruskalMST;
import com.aitu.mst.algos.PrimMST;
import com.aitu.mst.dto.InputData;
import com.aitu.mst.dto.InputEdge;
import com.aitu.mst.dto.InputGraph;
import com.aitu.mst.io.JsonIO;
import com.aitu.mst.model.Graph;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MstTests {


    private InputData load() throws Exception {
        try { return JsonIO.readInput("ass_3_input_auto.json"); }
        catch (Exception e) { return JsonIO.readInput("ass_3_input.json"); }
    }

    private Graph build(InputGraph gi) {
        Graph g = new Graph(gi.nodes);
        for (InputEdge e : gi.edges)
            g.addUndirectedEdge(g.indexOf(e.from), g.indexOf(e.to), e.weight);
        return g;
    }

    @Test
    void mst_correctness_and_perf() throws Exception {
        InputData data = load();
        for (InputGraph gi : data.graphs) {
            Graph g = build(gi);

            PrimMST.Result pr = PrimMST.run(g);
            KruskalMST.Result kr = KruskalMST.run(g);

            // 1) Совпадение стоимости MST
            assertEquals(pr.totalCost, kr.totalCost, "Total cost must match (Prim vs Kruskal)");

            // 2) В каждом MST ровно V−1 рёбер
            assertEquals(g.size() - 1, pr.edges.size(), "Prim edges = V-1");
            assertEquals(g.size() - 1, kr.edges.size(), "Kruskal edges = V-1");

            // 3) Ацикличность и связность
            assertTrue(isAcyclic(pr.edges, g.size()), "Prim MST must be acyclic");
            assertTrue(isAcyclic(kr.edges, g.size()), "Kruskal MST must be acyclic");
            assertTrue(isConnected(pr.edges, g.size()), "Prim MST must be connected");
            assertTrue(isConnected(kr.edges, g.size()), "Kruskal MST must be connected");

            // 4) Время в миллисекундах: 0 < t < 900 (требование препода)
            assertTrue(pr.timeMs > 0 && pr.timeMs < 900, "Prim time must be in (0,900) ms");
            assertTrue(kr.timeMs > 0 && kr.timeMs < 900, "Kruskal time must be in (0,900) ms");

            // 5) Счётчик операций неотрицательный
            assertTrue(pr.operations >= 0, "Prim ops must be non-negative");
            assertTrue(kr.operations >= 0, "Kruskal ops must be non-negative");
        }
    }

    @Test
    void results_are_reproducible_for_same_dataset() throws Exception {
        InputData data = load();
        for (InputGraph gi : data.graphs) {
            Graph g1 = build(gi);
            Graph g2 = build(gi);

            PrimMST.Result pr1 = PrimMST.run(g1);
            PrimMST.Result pr2 = PrimMST.run(g2);
            KruskalMST.Result kr1 = KruskalMST.run(g1);
            KruskalMST.Result kr2 = KruskalMST.run(g2);

            assertEquals(pr1.totalCost, pr2.totalCost, "Prim reproducible total cost");
            assertEquals(kr1.totalCost, kr2.totalCost, "Kruskal reproducible total cost");
        }
    }

    // ---------- helpers ----------

    private boolean isConnected(List<com.aitu.mst.model.Edge> edges, int n) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (var e : edges) {
            adj.get(e.u).add(e.v);
            adj.get(e.v).add(e.u);
        }
        boolean[] vis = new boolean[n];
        dfs(0, adj, vis);
        for (boolean b : vis) if (!b) return false;
        return true;
    }

    private void dfs(int v, List<List<Integer>> adj, boolean[] vis) {
        vis[v] = true;
        for (int to : adj.get(v)) if (!vis[to]) dfs(to, adj, vis);
    }

    private boolean isAcyclic(List<com.aitu.mst.model.Edge> edges, int n) {
        int[] p = new int[n];
        for (int i = 0; i < n; i++) p[i] = i;
        java.util.function.IntUnaryOperator find = new java.util.function.IntUnaryOperator() {
            public int applyAsInt(int x) { return p[x]==x ? x : (p[x]=applyAsInt(p[x])); }
        };
        for (var e : edges) {
            int a = find.applyAsInt(e.u), b = find.applyAsInt(e.v);
            if (a == b) return false;
            p[b] = a;
        }
        return true;
    }
}