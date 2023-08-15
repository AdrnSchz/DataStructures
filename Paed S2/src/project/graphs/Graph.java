package project.graphs;

import project.CustomList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.StringTokenizer;

public class Graph {

    private final CustomList<Vertex> pointsInterest;

    public Graph() {
        pointsInterest = new CustomList<>();
    }

    public Graph(String path) {

        this();

        try {
            FileReader f = new FileReader(path);
            BufferedReader bf = new BufferedReader(f);
            int V = Integer.parseInt(bf.readLine());

            for (int i = 0; i < V; i++) {
                String line = bf.readLine();
                StringTokenizer stn = new StringTokenizer(line, ";\n");
                int id = Integer.parseInt(stn.nextToken());
                String name = stn.nextToken();
                String kingdom = stn.nextToken();
                String climate = stn.nextToken();
                Vertex vert = new Vertex(id, i, name, kingdom, climate);
                pointsInterest.addLast(vert);
            }

            int E = Integer.parseInt(bf.readLine());

            for (int i = 0; i < E; i++) {
                String line = bf.readLine();
                StringTokenizer stn = new StringTokenizer(line, ";\n");
                int pointA = Integer.parseInt(stn.nextToken());
                int pointB = Integer.parseInt(stn.nextToken());
                float timeE = Float.parseFloat(stn.nextToken());
                float timeA = Float.parseFloat(stn.nextToken());
                float distance = Float.parseFloat(stn.nextToken());
                Edge edge = new Edge(timeE, timeA, distance);
                int indexA = 0, indexB = 0;

                for (int j = 0; j < V; j++) {
                    if (pointsInterest.get(j).getId() == pointA) {
                        indexA = j;
                    } else if (pointsInterest.get(j).getId() == pointB) {
                        indexB = j;
                    }
                }
                addEdge(indexA, indexB, edge);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Graph(Graph graph) {

        this();

        CustomList<Vertex> nodes = graph.getPointsInterest();
        for (int i = 0; i < nodes.size(); i++) {
            Vertex vertex = nodes.get(i);
            pointsInterest.addLast(new Vertex(vertex));
            CustomList<Edge> edges = vertex.getEdges();
            for (Edge edge : edges) {
                pointsInterest.get(i).addEdge(new Edge(edge, edge.getDestination()));
            }
        }

        for (Vertex vertex : pointsInterest) {
            CustomList<Edge> edges = vertex.getEdges();
            for (Edge edge : edges) {
                edge.setDestination(pointsInterest.get(nodes.indexOf(edge.getDestination())));
            }
        }
    }

    private void addEdge(int indexA, int indexB, Edge edge) {
        edge.setDestination(pointsInterest.get(indexB));
        pointsInterest.get(indexA).addEdge(edge.clone());

        edge.setDestination(pointsInterest.get(indexA));
        pointsInterest.get(indexB).addEdge(edge);
    }

    public CustomList<Vertex> getPointsInterest() {
        return pointsInterest;
    }

    public CustomList<Vertex> getAdjacent(int vertexID) {
        Vertex vertex = getVertexById(vertexID);
        int n = vertex.getEdges().size();
        CustomList<Vertex> adjacent = new CustomList<>(n);

        for (int i = 0; i < n; i++) {
            adjacent.addLast(vertex.getEdges().get(i).getDestination());
        }
        return adjacent;
    }

    public CustomList<Edge> getAdjacent(int vertexPos, boolean coco, String bird) {

        Vertex vertex = getVertexByPosition(vertexPos);
        int n = vertex.getEdges().size();
        CustomList<Edge> edges = vertex.getEdges();
        CustomList<Edge> adjacent = new CustomList<>(n);

        for (int i = 0; i < n; i++) {
            if (coco) {
                if (edges.get(i).getDistance() <= 50) {
                    if (!Objects.equals(edges.get(i).getDestination().getClimate(), "POLAR") && bird == "A"
                            || !Objects.equals(edges.get(i).getDestination().getClimate(), "TROPICAL") && bird == "E") {
                        adjacent.addLast(edges.get(i));
                    }
                }
            }
            else {
                if (!Objects.equals(edges.get(i).getDestination().getClimate(), "POLAR") && bird == "A"
                        || !Objects.equals(edges.get(i).getDestination().getClimate(), "TROPICAL") && bird == "E") {
                    adjacent.addLast(edges.get(i));
                }
            }
        }
        return adjacent;
    }

    public Vertex getVertexById(int id){
        for (Vertex vertex : pointsInterest) {
            if (vertex.getId() == id) {
                return vertex;
            }
        }
        return null;
    }

    public Vertex getVertexByPosition(int position){return pointsInterest.get(position);}

    public int getNumNodes() {
        return pointsInterest.size();
    }
}