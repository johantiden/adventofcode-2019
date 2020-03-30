package com.github.johantiden.adventofcode2019;

import java.util.ArrayList;
import java.util.List;

public class Six {


    public static List<Edge> parseInput(String input) {
        List<Edge> edges = new ArrayList<>();

        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] split = line.split("\\)");
            Edge edge = new Edge(split[1], split[0]);
            edges.add(edge);
        }

        return edges;
    }

    public static int hash(List<Edge> edges) {

        int sum = 0;
        for (Edge edge : edges) {
            int lengthToCOM = getLengthToCOM(edges, edge);
            sum += lengthToCOM;
        }

        return sum;
    }

    private static int getLengthToCOM(List<Edge> edges, Edge edge) {
        if (edge.to.equals("COM")) {
            return 1;
        } else {
            Edge to = findEdgeUsingFrom(edges, edge.to);
            return getLengthToCOM(edges, to) + 1;
        }
    }

    private static Edge findEdgeUsingFrom(List<Edge> edges, String from) {
        return edges.stream()
                .filter(edge -> edge.from.equals(from))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("could not find '"+from+"'"));
    }


    static class Edge {
        private final String from;
        private final String to;

        Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }



}
