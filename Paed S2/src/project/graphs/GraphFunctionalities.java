package project.graphs;

import project.CustomList;
import project.Main;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class GraphFunctionalities {

    private final Graph graph;

    public GraphFunctionalities(String path){
        graph = new Graph(path);
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        String option;

        do{
            System.out.println("""
    
                    \tA. Kingdom exploration
                    \tB. Detecting common journeys
                    \tC. Premium messaging
    
                    \tD. Go back"""
            );
            System.out.print("\nWhich functionality doth thee want to run? ");
            option = sc.nextLine().toUpperCase();
            switch (option) {
                case "A" -> kingdomExploration();
                case "B" -> {
                    Graph mst = commonJourneys(new Graph(graph));
                    printMST(mst);
                }
                case "C" -> premiumMessaging();
                case "D" -> {
                    return;
                }
                default -> System.out.println(Main.ERROR_OPTION);
            }
        } while (true);
    }

    public void kingdomExploration() {
        Scanner sc = new Scanner(System.in);
        boolean error;
        int vertexID;
        Vertex vertex;

        do {
            try {
                System.out.print("\nWhat place is to be explored? ");
                vertexID = Integer.parseInt(sc.nextLine());

                vertex = graph.getVertexById(vertexID);
                if(vertex != null){
                    error = false;
                    bfs(graph, vertex);
                }else{
                    error = true;
                    System.out.println("\nError. ID not found");
                }
            } catch (NumberFormatException e) {
                System.out.println(Main.ERROR_OPTION);
                error = true;
            }
        }while (error);
    }

    public void bfs(Graph graph, Vertex vertex) {
        CustomList<Vertex> q = new CustomList<>();
        CustomList<Vertex> adjacent;
        boolean reachable = false;
        boolean noneReached = true;
        int totalNodes = graph.getNumNodes();

        //Evaluate first vertex
        Vertex current;
        String kingdom = vertex.getKingdom();
        q.addLast(vertex);
        vertex.markVisited();

        while (!q.isEmpty()) {
            current = q.pop();
            adjacent = graph.getAdjacent(current.getId());

            for (Vertex value : adjacent) {
                if (!value.isVisited()) {
                    value.markVisited();
                    //Prioritize nodes from same Kingdom
                    if (value.getKingdom().equals(kingdom)) {
                        if(noneReached){
                            System.out.println("\nIts reachable places in the Kingdom of " + kingdom + " are the following:\n");
                            noneReached = false;
                        }
                        reachable = true;
                        System.out.println(value.getId() + " – " + value.getName() + ", kingdom of "
                                + value.getKingdom() + " (" + value.getClimate() + " Climate)");
                        q.addFirst(value);
                    } else {
                        q.addLast(value);
                    }
                }
            }
        }
        if (!reachable) {
            System.out.println("\nThere aren't any reachable places in the kingdom of  " + kingdom);
        }
        //Unmark nodes
        for (int i = 0; i < totalNodes; i++) {
            graph.getPointsInterest().get(i).markAsNotVisited();
        }
    }

    public Graph commonJourneys(Graph graph) {

        Graph mst = new Graph();

        CustomList<Vertex> pointsInterest = graph.getPointsInterest();
        CustomList<Vertex> mstPointsInterest = mst.getPointsInterest();

        int i = 0;
        while (mstPointsInterest.size() < pointsInterest.size()) {

            Vertex point = pointsInterest.get(i);

            if (point.isVisited()) {
                i++;
                continue;
            }

            CustomList<Edge> edges = point.getEdges();

            float bestDistance = Float.MAX_VALUE;
            Edge bestEdge = edges.get(0);
            for (Edge edge : edges) {
                if (edge.getDistance() < bestDistance) {
                    bestDistance = edge.getDistance();
                    bestEdge = edge;
                }
            }

            if (!point.isVisited()) {
                point.removeEdges();
            }
            point.addEdge(bestEdge);

            point.markVisited();
            mstPointsInterest.add(point);

            Edge secondEdge = bestEdge.clone();
            secondEdge.setDestination(point);

            point = bestEdge.getDestination();

            if (!point.isVisited()) {
                point.removeEdges();
            }
            point.addEdge(secondEdge);

            if (!point.isVisited()) {
                point.markVisited();
                mstPointsInterest.add(point);
            }

            i++;
        }
        return mst;
    }

    public void printMST(Graph mst) {

        System.out.println("\nThese are the most common journeys:");

        for (Vertex origin : mst.getPointsInterest()) {

            for (Edge edge : origin.getEdges()) {

                Vertex destination = edge.getDestination();

                if (!destination.isVisited()) {
                    continue;
                }

                System.out.println(
                        "\n" +
                                origin.getName() +
                                ", Kingdom of " +
                                origin.getKingdom() +
                                " (" +
                                origin.getClimate() +
                                " Climate)"
                );
                System.out.print(
                        "\t| " +
                                "Distance: " +
                                edge.getDistance() +
                                "\n\t|-----------------> "
                );
                System.out.println(
                        destination.getName() +
                                ", Kingdom of " +
                                destination.getKingdom() +
                                " (" +
                                destination.getClimate() +
                                " Climate)"
                );
            }
            origin.markAsNotVisited();
        }
    }


    public CustomList<Vertex> premiumMessaging(){

        Scanner sc = new Scanner(System.in);

        int originId = 0;
        boolean originOkay = false;
        do {
            try {
                System.out.print("What is thy origin? ");
                originId = sc.nextInt();
                if (graph.getVertexById(originId) == null)
                    System.out.println("ERROR: id needs to correspond to a vertex");
                else
                    originOkay = true;
            }
            catch (Exception e){
                System.out.println("ERROR: id needs to correspond to a vertex");
                sc.nextLine();
            }
        } while (!originOkay);

        int destinationId = 0;
        boolean destinationOkay = false;
        do {
            try {
                System.out.print("What is thy destination? ");
                destinationId = sc.nextInt();
                if (graph.getVertexById(destinationId) == null || destinationId == originId)
                    System.out.println("ERROR: id needs to correspond to a vertex that exists ot that is not the origin");
                else
                    destinationOkay = true;
            }
            catch (Exception e){
                System.out.println("ERROR: id needs to correspond to a vertex");
                sc.nextLine();
            }
        } while (!destinationOkay);

        sc.nextLine();
        String cocoString;
        do {
            System.out.print("Does the swallow carry a coconut? ");
            cocoString = sc.nextLine();
            if (!cocoString.equalsIgnoreCase("yes") && !cocoString.equalsIgnoreCase("no"))
                System.out.println("ERROR: input needs to be either yes or no");
        } while (!cocoString.equalsIgnoreCase("yes") && !cocoString.equalsIgnoreCase("no"));

        boolean coco = cocoString.equalsIgnoreCase("yes");

        CustomList<Integer>[] walksA = new CustomList[graph.getNumNodes()];
        CustomList<Integer>[] walksE = new CustomList[graph.getNumNodes()];

        float[] timeA = new float[graph.getNumNodes()];
        float[] timeE = new float[graph.getNumNodes()];
        float[] distanceA = new float[graph.getNumNodes()];
        float[] distanceE = new float[graph.getNumNodes()];
        float newTime;
        float newDistance;

        int origin = graph.getVertexById(originId).getPosition();
        int destination = graph.getVertexById(destinationId).getPosition();

        Arrays.fill(timeA, Float.MAX_VALUE/2);
        Arrays.fill(timeE, Float.MAX_VALUE/2);
        timeA[origin] = 0;
        timeE[origin] = 0;

        Arrays.fill(distanceA, Float.MAX_VALUE/2);
        Arrays.fill(distanceE, Float.MAX_VALUE/2);
        distanceA[origin] = 0;
        distanceE[origin] = 0;

        Vertex current = graph.getVertexByPosition(origin);
        float minTime = 0;

        if (Objects.equals(current.getClimate(), "POLAR")){
            minTime = Float.MAX_VALUE/2;
        }
        while (minTime != Float.MAX_VALUE/2){

            CustomList<Edge> adjacentEdges = graph.getAdjacent(current.getPosition(), coco, "A");

            for (Edge adjacentEdge : adjacentEdges){

                Vertex adjacentVertex = adjacentEdge.getDestination();

                if (!adjacentVertex.isVisited()){
                    newTime = timeA[current.getPosition()] + adjacentEdge.getTimeA();
                    newDistance = distanceA[current.getPosition()] + adjacentEdge.getDistance();

                    if (newTime < timeA[adjacentVertex.getPosition()] && newTime < timeA[destination]){

                        timeA[adjacentVertex.getPosition()] = newTime;
                        distanceA[adjacentVertex.getPosition()] = newDistance;

                        //updates walk to adjacent on walks array
                        int size;
                        try {
                            size = walksA[current.getPosition()].size();
                        } catch (NullPointerException e){
                            size = 0;
                        }
                        CustomList<Integer> walkAux = new CustomList<>();
                        for (int i=0; i<size; i++) {
                            walkAux.add(walksA[current.getPosition()].get(i));
                        }
                        walkAux.add(current.getPosition());
                        walksA[adjacentVertex.getPosition()] = walkAux;
                    }
                }
            }
            current.markVisited();

            //update current to the shortest time non-visited node
            minTime = Float.MAX_VALUE/2;
            int timeLength = timeA.length;
            for (int i=0; i<timeLength; i++){

                if (timeA[i] < minTime && !graph.getVertexByPosition(i).isVisited()){

                    current = graph.getVertexByPosition(i);
                    minTime = timeA[i];
                }
            }
        }

        for (int i = 0; i < graph.getNumNodes(); i++) {
            graph.getPointsInterest().get(i).markAsNotVisited();
        }

        current = graph.getVertexByPosition(origin);
        minTime = 0;

        if (Objects.equals(current.getClimate(), "TROPICAL")){
            minTime = Float.MAX_VALUE/2;
        }
        while (minTime != Float.MAX_VALUE/2){

            CustomList<Edge> adjacentEdges = graph.getAdjacent(current.getPosition(), coco, "E");

            for (Edge adjacentEdge : adjacentEdges){

                Vertex adjacentVertex = adjacentEdge.getDestination();

                if (!adjacentVertex.isVisited()){
                    newTime = timeE[current.getPosition()] + adjacentEdge.getTimeE();
                    newDistance = distanceE[current.getPosition()] + adjacentEdge.getDistance();

                    if (newTime < timeE[adjacentVertex.getPosition()] && newTime < timeE[destination]){

                        timeE[adjacentVertex.getPosition()] = newTime;
                        distanceE[adjacentVertex.getPosition()] = newDistance;

                        //updates walk to adjacent on walks array
                        int size;
                        try {
                            size = walksE[current.getPosition()].size();
                        } catch (NullPointerException e){
                            size = 0;
                        }
                        CustomList<Integer> walkAux = new CustomList<>();
                        for (int i=0; i<size; i++) {
                            walkAux.add(walksE[current.getPosition()].get(i));
                        }
                        walkAux.add(current.getPosition());
                        walksE[adjacentVertex.getPosition()] = walkAux;
                    }
                }
            }
            current.markVisited();

            //update current to the shortest time non-visited node
            minTime = Float.MAX_VALUE/2;
            int timeLength = timeE.length;
            for (int i=0; i<timeLength; i++){

                if (timeE[i] < minTime && !graph.getVertexByPosition(i).isVisited()){

                    current = graph.getVertexByPosition(i);
                    minTime = timeE[i];
                }
            }
        }

        for (int i = 0; i < graph.getNumNodes(); i++) {
            graph.getPointsInterest().get(i).markAsNotVisited();
        }

        CustomList<Vertex> path = new CustomList<>();
        String bird;
        float distance;
        float time;

        if (timeA[destination] == Float.MAX_VALUE/2 && timeE[destination] == Float.MAX_VALUE/2){
            System.out.println("There is no path that leads to the destination from your chosen origin\n");
            return null;
        }
        else if (timeA[destination] < timeE[destination]){

            bird = "an African";
            for (int i=0; i<walksA[destination].size(); i++){
                path.add(graph.getVertexByPosition(walksA[destination].get(i)));
            }
            time = timeA[destination];
            distance = distanceA[destination];
        }
        else {

            bird = "a European";
            for (int i=0; i<walksE[destination].size(); i++){
                path.add(graph.getVertexByPosition(walksE[destination].get(i)));
            }
            time = timeE[destination];
            distance = distanceE[destination];
        }

        System.out.println("The most efficient option would be to send " + bird + " swallow.");
        System.out.println("    Time: " + time + " minutes");
        System.out.println("    Distance: " + distance + " kilometers");
        System.out.println("    Path: ");

        for (int i=0; i<path.size(); i++){

            System.out.println("        POI " + (i+1) + ": " + path.get(i).getName() + "    ID: " + path.get(i).getId());
        }

        return path;
    }
}
