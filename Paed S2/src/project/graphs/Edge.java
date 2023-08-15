package project.graphs;

public class Edge implements Cloneable {
    private float timeE;
    private float timeA;
    private float distance;
    private Vertex destination;

    public Edge(float timeE, float timeA, float distance) {
        this.timeE = timeE;
        this.timeA = timeA;
        this.distance = distance;
    }

    public Edge(Edge edge, Vertex destination) {
        this(edge.getTimeE(), edge.getTimeA(), edge.getDistance());
        this.destination = destination;
    }

    public float getTimeE() {
        return timeE;
    }

    public float getTimeA() {
        return timeA;
    }

    public float getDistance() {
        return distance;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setTimeE(float timeE) {
        this.timeE = timeE;
    }

    public void setTimeA(float timeA) {
        this.timeA = timeA;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    @Override
    public Edge clone() {
        try {
            return (Edge) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}