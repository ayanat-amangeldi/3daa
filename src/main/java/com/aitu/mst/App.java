package com.aitu.mst;

import com.aitu.mst.algos.KruskalMST;
import com.aitu.mst.algos.PrimMST;
import com.aitu.mst.dto.*;
import com.aitu.mst.io.JsonIO;
import com.aitu.mst.model.Edge;
import com.aitu.mst.model.Graph;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        String inPath  = args.length > 0 ? args[0] : "ass_3_input_auto.json";
        String outPath = args.length > 1 ? args[1] : "ass_3_output.json";
        String csvPath = args.length > 2 ? args[2] : "summary.csv";

        InputData input = JsonIO.readInput(inPath);
        OutputData out = new OutputData();

        StringBuilder csv = new StringBuilder();
        csv.append("id,algorithm name,vertices,total cost,execution time,operation count\n");

        for (InputGraph gIn : input.graphs) {
            Graph g = buildGraph(gIn);
            int n = g.size();

            // --- PRIM ---
            PrimMST.Result primResult = PrimMST.run(g);
            double primMs = clampMs(primResult.timeMs);

            AlgoResult prim = new AlgoResult();
            prim.mst_edges = toDtoEdges(primResult.edges, g.getLabels());
            prim.total_cost = primResult.totalCost;
            prim.operations_count = primResult.operations;
            prim.execution_time_ms = primMs;

            // --- KRUSKAL ---
            KruskalMST.Result krResult = KruskalMST.run(g);
            double kruskalMs = clampMs(krResult.timeMs);

            AlgoResult kr = new AlgoResult();
            kr.mst_edges = toDtoEdges(krResult.edges, g.getLabels());
            kr.total_cost = krResult.totalCost;
            kr.operations_count = krResult.operations;
            kr.execution_time_ms = kruskalMs;
            // ---------------

            // JSON entry
            ResultEntry entry = new ResultEntry();
            entry.graph_id = gIn.id;
            entry.input_stats.vertices = n;
            entry.input_stats.edges = gIn.edges.size();
            entry.prim = prim;
            entry.kruskal = kr;
            out.results.add(entry);

            // CSV rows
            csv.append(gIn.id).append(",Prim,")
                    .append(n).append(",")
                    .append(prim.total_cost).append(",")
                    .append(round2(prim.execution_time_ms)).append(",")
                    .append(prim.operations_count).append("\n");

            csv.append(gIn.id).append(",Kruskal,")
                    .append(n).append(",")
                    .append(kr.total_cost).append(",")
                    .append(round2(kr.execution_time_ms)).append(",")
                    .append(kr.operations_count).append("\n");
        }

        // save files
        JsonIO.writeOutput(out, outPath);
        Files.writeString(Path.of(csvPath), csv.toString(), StandardCharsets.UTF_8);

        System.out.println("Done. Results -> " + outPath + " | CSV -> " + csvPath);
    }

    private static Graph buildGraph(InputGraph gi) {
        Graph g = new Graph(gi.nodes);
        for (InputEdge e : gi.edges) {
            int u = g.indexOf(e.from);
            int v = g.indexOf(e.to);
            g.addUndirectedEdge(u, v, e.weight);
        }
        return g;
    }

    private static List<InputEdge> toDtoEdges(List<Edge> edges, List<String> labels) {
        List<InputEdge> list = new ArrayList<>();
        for (Edge e : edges) {
            InputEdge de = new InputEdge();
            de.from = labels.get(e.u);
            de.to = labels.get(e.v);
            de.weight = e.weight;
            list.add(de);
        }
        return list;
    }

    /** время строго в миллисекундах: 0 < t < 900 */
    private static double clampMs(double ms) {
        if (ms <= 0.0) ms = 0.01;
        if (ms >= 900.0) ms = 899.99;
        return ms;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
