package project.r_trees;

import project.CustomList;
import project.Main;
import project.r_trees.UI.UIController;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RTreesFunctionalities {

    public static final int MAX_CHILDREN = 3;
    public static final int MIN_CHILDREN = Math.max((int) Math.round(MAX_CHILDREN * 0.3), 1); // 30% of MAX_CHILDREN
    public Scanner sc = new Scanner(System.in);
    private static Rectangle root;

    public static double xMulti, alocateX;
    public static double yMulti, alocateY;
    public static int scaleX, scaleY;
    Shrub[] shrubs;
    Point point;

    public RTreesFunctionalities (String path){
        root = new Rectangle();
        root = createTree(path);
    }

    public void menu() {
        String option;

        do {
            System.out.println("""
    
                    \tA. Add shrub
                    \tB. Remove shrub
                    \tC. Visualization
                    \tD. Search by area
                    \tE. Aesthetic optimization
    
                    \tF. Go back"""
            );
            System.out.print("\nWhich functionality doth thee want to run? ");
            option = sc.nextLine().toUpperCase();
            System.out.println();

            switch (option) {
                case "A":
                    addShrub();
                    break;
                case "B":
                    removeShrub();
                    break;
                case "C":
                    visualizeTree();
                    break;
                case "D":
                    searchByArea();
                    break;
                case "E":
                    aestheticOptimisation();
                    break;
                case "F":
                    return;
                default:
                    System.out.print(Main.ERROR_OPTION);
            }
        } while (true);
    }

    public Rectangle createTree(String path) {
        try {
            FileReader f = new FileReader(path);
            BufferedReader bf = new BufferedReader(f);
            int numShrubs = Integer.parseInt(bf.readLine());

            for (int i = 0; i < numShrubs; i++) {
                String line = bf.readLine();
                StringTokenizer stn = new StringTokenizer(line, ";\n");

                String type = stn.nextToken();
                double size = Double.parseDouble(stn.nextToken());
                double x = Double.parseDouble(stn.nextToken());
                double y = Double.parseDouble(stn.nextToken());
                Color color = Color.decode(stn.nextToken());

                Shrub shrub = new Shrub(type, size, x, y, color);
                root = root.addShrub(shrub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void addShrub() {
        String shrubType;
        do {
            System.out.print("Shrub type: ");
            shrubType = sc.nextLine();
            if (!(shrubType.equalsIgnoreCase("circle") || shrubType.equalsIgnoreCase("square")))
                System.out.println("ERROR: shrub type must be a circle or a square");
        } while (!(shrubType.equalsIgnoreCase("circle") || shrubType.equalsIgnoreCase("square")));

        double shrubSize = 0;
        boolean shrubSizeFine = false;
        do {
            try {
                System.out.print("Shrub size: ");
                shrubSize = sc.nextFloat();
                if (shrubSize <= 0)
                    System.out.println("ERROR: shrub size must be higher than 0");
                else
                    shrubSizeFine = true;
            }
            catch (Exception e){
                System.out.println("ERROR: shrub size must be higher than 0");
                sc.nextLine();
            }
        } while (!shrubSizeFine);

        double latitude = 0;
        boolean latitudeFine = false;
        do {
            try {
                System.out.print("Shrub latitude: ");
                latitude = sc.nextFloat();
                latitudeFine = true;
            }
            catch (Exception e){
                sc.nextLine();
                System.out.println("ERROR: latitude must be a number");
            }
        } while (!latitudeFine);

        double longitude = 0;
        boolean longitudeFine = false;
        do {
            try {
                System.out.print("Shrub longitude: ");
                longitude = sc.nextFloat();
                longitudeFine = true;
            }
            catch (Exception e){
                System.out.println("ERROR: longitude must be a number");
                sc.nextLine();
            }
        } while (!longitudeFine);

        String shrubColour;
        shrubColour = sc.nextLine();
        do {
            System.out.print("Shrub colour: ");
            shrubColour = sc.nextLine();
            if (shrubColour.charAt(0) != '#' && shrubColour.length() != 7){
                System.out.println("ERROR: input needs to follow format");
            }
        } while (shrubColour.charAt(0) != '#' && shrubColour.length() != 7);

        Shrub newShrub = new Shrub(shrubType, shrubSize, latitude, longitude, Color.decode(shrubColour));
        root.addShrub(newShrub);

        System.out.println("\nA new shrub appeared in Britain.");
    }

    private void removeShrub() {

        double latitude, longitude;
        do {
            try {
                System.out.print("Shrub latitude: ");
                latitude = Double.parseDouble(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("\nERROR: latitude must be a number\n");
            }
        }  while (true);

        do {
            try {
                System.out.print("Shrub longitude: ");
                longitude = Double.parseDouble(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("\nERROR: longitude must be a number\n");
            }
        }  while (true);

        if (root.removeShrub(latitude, longitude, new CustomList<>())) {
            System.out.println("\nThe shrub was removed to be merged into a border.");
        } else {
            System.out.println("\nNo matching shrub was found.");
        }
    }

    private void visualizeTree() {

        System.out.println("Let there be a visualization...");

        xMulti = (UIController.WIDTH - 50) / (root.getMax().getX() - root.getMin().getX());
        yMulti = (UIController.HEIGHT - 50) / (root.getMax().getY() - root.getMin().getY());
        alocateX = (root.getMin().getX() - 53.8) * xMulti - 10;
        alocateY = (root.getMin().getY() + 4.1) * yMulti - 10;
        scaleX = (int) Math.round((UIController.WIDTH - 20) / (xMulti * (root.getMax().getX() - root.getMin().getX())));
        scaleY = (int) Math.round((UIController.HEIGHT - 20) / (yMulti * (root.getMax().getY() - root.getMin().getY()) * -1));
        new UIController(root);
    }

    private void searchByArea() {
        String input;
        StringTokenizer stn;
        Point firstPoint = null, secondPoint = null;
        CustomList<Shrub> shrubs = new CustomList<>();
        double lat, lon;
        boolean validInput;

        do {
            try {
                System.out.print("Enter the area’s first point (lat,long): ");
                input = sc.nextLine();
                stn = new StringTokenizer(input, ",\n");
                lat = Double.parseDouble(stn.nextToken());
                lon = Double.parseDouble(stn.nextToken());
                firstPoint = new Point(lat, lon);
                validInput = true;
            } catch (NumberFormatException | NoSuchElementException e) {
                validInput = false;
                System.out.println("Please enter a valid input");
            }
        } while (!validInput);

        do {
            try {
                System.out.print("Enter the area’s second point (lat,long): ");
                input = sc.nextLine();
                stn = new StringTokenizer(input, ",\n");
                lat = Double.parseDouble(stn.nextToken());
                lon = Double.parseDouble(stn.nextToken());
                secondPoint = new Point(lat, lon);
                validInput = true;
            } catch (NumberFormatException | NoSuchElementException e) {
                validInput = false;
                System.out.println("Please enter a valid input");
            }
        } while (!validInput);

        //Search for shrubs
        root.searchArea(firstPoint, secondPoint, shrubs);

        //Print result
        if (shrubs.size() == 0) {
            System.out.println("\nNo shrubs were found in the area.");
        } else if (shrubs.size() == 1) {
            System.out.println("\nThe following shrub was found in the area: ");
        } else {
            System.out.println("\n" + shrubs.size() + " shrubs were found in the area:");
        }

        for (Shrub shrub : shrubs) {
            System.out.print("\t* " + shrub.getX() + ", " + shrub.getY() + ": " + shrub.getType());
            if (shrub.getType().equals("CIRCLE")) {
                System.out.print(" (r = " + shrub.getSize() + "m) ");
            } else {
                System.out.print(" (s = " + shrub.getSize() + "m) ");
            }
            System.out.printf("#%06X%n", (0xFFFFFF & shrub.getColor().getRGB()));
        }
    }

    public void aestheticOptimisation(){
        String point;
        double x = 0;
        double y = 0;
        boolean pointOkay = false;
        do {
            try {
                System.out.print("Enter the point to search (lat,long): ");
                point = sc.nextLine();
                StringTokenizer stn = new StringTokenizer(point, ",");
                x = Double.parseDouble(stn.nextToken());
                y = Double.parseDouble(stn.nextToken());
                pointOkay = true;
            }
            catch (Exception e) {
                System.out.println("ERROR: Input format needs to be followed");
            }
        } while (!pointOkay);

        this.point = new Point(x, y);

        int k = 0;
        do {
            try {
                System.out.print("Enter the amount of shrubs to consider (K): ");
                k = sc.nextInt();
                if (k < 1)
                    System.out.println("ERROR: k needs to be an integer above 0");
            }
            catch (Exception e){
                System.out.println("ERROR: k needs to be an integer above 0");
                sc.nextLine();
            }
        } while (k < 1);
        sc.nextLine();

        shrubs = new Shrub[k];
        for (int i = 0; i < shrubs.length; i++){
            shrubs[i] = new Shrub();
            shrubs[i].setX(Double.MAX_VALUE);
            shrubs[i].setY(Double.MAX_VALUE);
        }
        searchK(root);

        int numCircles = 0;
        int numSquares = 0;
        for (int i = 0; i < shrubs.length; i++){
            if (shrubs[i].getType().equalsIgnoreCase("CIRCLE")) {
                numCircles++;
            }
            else {
                numSquares++;
            }
        }
        System.out.println();
        System.out.print("Most common type: ");
        if (numSquares > numCircles){
            System.out.println("SQUARES");
        }
        else if (numSquares < numCircles){
            System.out.println("CIRCLES");
        }
        else {
            System.out.println("SQUARES and CIRCLES");
        }
        int[] colour = new int[3];
        for (int i = 0; i < shrubs.length; i++){
            colour[0] += shrubs[i].getColor().getRed();
            colour[1] += shrubs[i].getColor().getGreen();
            colour[2] += shrubs[i].getColor().getBlue();
        }
        colour[0] /= shrubs.length;
        colour[1] /= shrubs.length;
        colour[2] /= shrubs.length;

        String hex = String.format("#%02x%02x%02x", colour[0], colour[1], colour[2]);
        System.out.println("Average colour: " + hex);
    }

    private void searchK(Figure rectangle){

        boolean[] childrenVisited = new boolean[rectangle.getChildren().size()];

        if (rectangle.getChildren().get(0) instanceof Rectangle){
            double shortestDistance = Double.MAX_VALUE;
            int indexShortest = -1;

            for (int i = 0; i < rectangle.getChildren().size(); i++){
                if (point.getDistanceTo(rectangle.getChildren().get(i)) < shortestDistance){
                    shortestDistance = point.getDistanceTo(rectangle.getChildren().get(i));
                    indexShortest = i;
                }
            }
            childrenVisited[indexShortest] = true;
            searchK(rectangle.getChildren().get(indexShortest));
        }

        if (rectangle.getChildren().get(0) instanceof Point) {

            for (int i = 0; i < shrubs.length; i++) {
                for (int j = 0; j < rectangle.getChildren().size(); j++) {
                    if (shrubs[i].getDistanceTo(point) > rectangle.getChildren().get(j).getDistanceTo(point)
                        && !childrenVisited[j]) {
                        childrenVisited[j] = true;
                        shrubs[i] = (Shrub) rectangle.getChildren().get(j);
                    }
                }
            }
        } else {
            for (int i = 0; i < shrubs.length; i++) {
                for (int j = 0; j < rectangle.getChildren().size(); j++) {
                    if (shrubs[i].getDistanceTo(point) > minDistanceBetween(rectangle.getChildren().get(j),point)
                        && !childrenVisited[j]) {
                        childrenVisited[j] = true;
                        searchK(rectangle.getChildren().get(j));
                    }
                }
            }
        }
    }
    //method to get the shortest distance between a rectangle and a point
    private double minDistanceBetween(Figure rectangle, Point point){

        double distanceX = Math.max(rectangle.getMin().getX() - point.getX(), 0);
        distanceX = Math.max(point.getX() - rectangle.getMax().getX(), distanceX);

        double distanceY = Math.max(rectangle.getMin().getY() - point.getY(), 0);
        distanceY = Math.max(point.getY() - rectangle.getMax().getY(), distanceY);

        return Math.sqrt(distanceY*distanceY + distanceX*distanceX);
    }


    public static Rectangle getRoot() {
        return root;
    }
}