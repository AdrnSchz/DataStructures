package project.graphs;

import project.CustomList;

public class Vertex {
    private int id;
    private int position;
    private String name;
    private String kingdom;
    private String climate;
    private boolean visited;
    private final CustomList<Edge> edges;

    public Vertex(int id, int position, String name, String kingdom, String climate) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.kingdom = kingdom;
        this.climate = climate;
        edges = new CustomList<>();
    }

    public Vertex(Vertex vertex) {
        this(vertex.getId(), vertex.getPosition(), vertex.getName(), vertex.getKingdom(), vertex.getClimate());
        this.visited = vertex.isVisited();
    }

    public int getId() {
        return id;
    }

    public int getPosition() {return position;}

    public String getName() {
        return name;
    }

    public String getKingdom() {
        return kingdom;
    }

    public String getClimate() {
        return climate;
    }

    public CustomList<Edge> getEdges() {
        return edges;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdges() {
        edges.clear();
    }

    public void markVisited(){
        visited = true;
    }

    public void markAsNotVisited(){
        visited = false;
    }
}
