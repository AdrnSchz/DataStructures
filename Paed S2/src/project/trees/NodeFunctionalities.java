package project.trees;

import project.CustomList;
import project.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NodeFunctionalities {
    private boolean left = false;

    private final Node root;

    public NodeFunctionalities(String path){
        root = createTree(path);
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        String option;

        do{
            System.out.println("""
    
                    \tA. Add resident
                    \tB. Remove resident
                    \tC. Visual representation
                    \tD. Scientific identification
                    \tE. Witch-hunt
    
                    \tF. Go back"""
            );
            System.out.print("\nWhich functionality doth thee want to run? ");
            option = sc.nextLine().toUpperCase();
            switch (option) {
                case "A":
                    addResident(root);
                    break;
                case "B":
                    removeResident(root);
                    break;
                case "C":
                    printTree(root,false, "");
                    break;
                case "D":
                    scientificIdentification(root);
                    break;
                case "E":
                    witchHunt(root);
                    break;
                case "F":
                    return;
                default:
                    System.out.println(Main.ERROR_OPTION);
            }
        } while (true);
    }

    public Node createTree(String path) {

        Node root = new Node();

        try {
            FileReader f = new FileReader(path);
            BufferedReader bf = new BufferedReader(f);
            int numResidents = Integer.parseInt(bf.readLine());

            for (int i = 0; i < numResidents; i++) {
                String line = bf.readLine();
                StringTokenizer stn = new StringTokenizer(line, ";\n");
                int id = Integer.parseInt(stn.nextToken());
                String name = stn.nextToken();
                float weight = Float.parseFloat(stn.nextToken());
                String kingdom = stn.nextToken();

                if (i == 0){
                    root.setId(id);
                    root.setName(name);
                    root.setWeight(weight);
                    root.setKingdom(kingdom);
                }
                else {
                    Node node = new Node(id, name, weight, kingdom);
                    root.addNode(node);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void addResident(Node root) {
        Scanner sc = new Scanner(System.in);
        int id = 0;
        String name, kingdom;
        float weight = 0;
        boolean valid;

        System.out.println();
        do {
            try {
                System.out.print("Resident identifier: ");
                id = Integer.parseInt(sc.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println(Main.ERROR_OPTION);
                valid = false;
            }
        }while(!valid);

        System.out.print("Resident name: ");
        name = sc.nextLine();

        do {
            try {
                System.out.print("Resident weight: ");
                weight = Float.parseFloat(sc.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println(Main.ERROR_OPTION);
                valid = false;
            }
        }while(!valid);

        System.out.print("Resident kingdom: ");
        kingdom = sc.nextLine();

        //Add node to tree
        Node newNode = new Node(id, name, weight, kingdom);
        root.addNode(newNode);
        System.out.println("\n" + name + " now accompanies us.\n");

        root.printDebug();
    }

    private void removeResident(Node root) {
        Scanner sc = new Scanner(System.in);
        int id = 0;
        boolean valid;
        Node nodeToRemove = null;

        System.out.println();
        do {
            try {
                System.out.print("Resident identifier: ");
                id = Integer.parseInt(sc.nextLine());
                nodeToRemove = root.getNodeByID(id);
                if(nodeToRemove != null){
                    valid = true;
                }
                else{
                    valid = false;
                    System.out.println("\nError. Resident's identifier not found in the system");
                }
            }catch (NumberFormatException e){
                System.out.println(Main.ERROR_OPTION);
                valid = false;
            }
        }while(!valid);

        //Remove resident
        root.remove(nodeToRemove);
        System.out.println(nodeToRemove.getName() + " was turned into a newt.");

        root.printDebug();
    }

    private void scientificIdentification(Node root) {

        Scanner sc = new Scanner(System.in);
        String objName, objCategory;
        float objWeight;

        System.out.print("\nObject name: ");
        objName = sc.nextLine();

        do {
            try {
                System.out.print("Object weight: ");
                objWeight = Float.parseFloat(sc.nextLine());
                break;
            } catch (NumberFormatException e){
                System.out.println(Main.ERROR_OPTION);
            }
        } while (true);

        do {
            System.out.print("Object category: ");
            objCategory = sc.nextLine().toUpperCase();

            Node currentNode = root;

            boolean found = false;

            switch (objCategory) {
                case "DUCK" -> {

                    StringBuilder log = new StringBuilder();

                    root.findEqualWeight(objWeight, log);

                    int numResidents = log.toString().equals("") ? 0 : log.toString().split("\n").length;

                    if (numResidents == 0) {
                        System.out.println("\nNo witches were found.");
                    }
                    else if (numResidents == 1) {
                        System.out.println("\n1 witch was found!");
                        System.out.print(log);
                    }
                    else {
                        System.out.printf("\n%d witches were found!\n", numResidents);
                        System.out.print(log);
                    }
                }
                case "WOOD" -> {
                    while (!found && currentNode != null) {

                        if (currentNode.getWeight() < objWeight) {
                            System.out.println("\n1 witch was found!");
                            System.out.printf("\t* %s (%d, %s): %skg\n", currentNode.getName(), currentNode.getId(), currentNode.getKingdom(), currentNode.getWeight());
                            found = true;
                        } else {
                            currentNode = currentNode.getRight();
                        }
                    }
                    if (!found) {
                        System.out.println("\nNo witches were found.");
                    }
                }
                case "STONE" -> {
                    while (!found && currentNode != null) {

                        if (currentNode.getWeight() > objWeight) {
                            System.out.println("\n1 witch was found!");
                            System.out.printf("\t* %s (%d, %s): %skg\n", currentNode.getName(), currentNode.getId(), currentNode.getKingdom(), currentNode.getWeight());
                            found = true;
                        } else {
                            currentNode = currentNode.getLeft();
                        }
                    }
                    if (!found) {
                        System.out.println("\nNo witches were found.");
                    }
                }
                default -> {
                    System.out.println("\nError. Please select one of the following: DUCK, WOOD, STONE\n");
                    objCategory = null;
                }
            }
        } while (objCategory == null);
    }

    private void witchHunt(Node root) {
        Scanner sc = new Scanner(System.in);
        boolean valid;
        float min_weight = 0, max_weight = 0;
        CustomList<Node> witches = new CustomList<>();

        System.out.println();
        do {
            try {
                System.out.print("Minimum weight: ");
                min_weight = Float.parseFloat(sc.nextLine());
                valid = true;
            }catch (NumberFormatException e){
                System.out.println(Main.ERROR_OPTION);
                valid = false;
            }
        }while(!valid);

        do {
            try {
                System.out.print("Maximum weight: ");
                max_weight = Float.parseFloat(sc.nextLine());
                valid = true;
            }catch (NumberFormatException e){
                System.out.println(Main.ERROR_OPTION);
                valid = false;
            }
        }while(!valid);

        root.getNodesInRange(witches, min_weight, max_weight);

        if(witches.size() == 0) {
            System.out.println("\nSorry. No witches could be rounded up");
        }
        else if (witches.size() == 1){
            System.out.println("\n1 witch was rounded up!");
        }
        else {
            System.out.println("\n" + witches.size() + " witches were rounded up!");
        }
        for (Node witch : witches) {
            System.out.println("\t* " + witch.getName() + " (" + witch.getId() + ", " + witch.getKingdom() + "): " +
                    "" + witch.getWeight() + " kg");
        }
    }

    private void printTree(Node root, boolean isLeft, String str) {
        if (root != null) {
            String str2 = "";
            if (root.getParent() != null) {
                if (root.getParent().getParent() == null) {
                    str = "|";
                    str2 = str;
                }
                else if (isLeft && root.getParent().getParent().getLeft() == root.getParent() ||
                        !isLeft && root.getParent().getParent().getRight() == root.getParent()) {
                    if (str.length() < 5) {
                        str = "     |";
                    } else {
                        str = str.substring(0, str.length() - 5) + "         |";
                    }
                    str2 = str;
                }
                else if (left) {
                    if (isLeft) {
                        str = str + "    |";
                        str2 = str;
                    } else {
                        str = str + "    |";
                        str2 = str;
                        if (root.getRight() != null) {
                            str2 = str + "    |";
                        }
                    }
                }
                else {
                    if (isLeft) {
                        str = str + "    |";
                        str2 = str;
                        if (root.getRight() != null) {
                            str2 = str + "    |";
                        }
                    } else {
                        str2 = str;
                        str = str + "    |";
                    }
                }
            }
            printTree(root.getRight(), false, str);

            if (root.getParent() == null) {
                System.out.println("|");
                System.out.print("* " + root.getName() + " (" + root.getId() + ", " + root.getKingdom() +
                        "): " + root.getWeight() + "kg\n");
                left = true;
            } else {
                if (left && root.getParent().getParent() == null) {
                    str2 = str + "    |";
                }
                else if (root.getRight() != null && (!isLeft || root.getParent().getParent() == null)) {
                    str2 = str.substring(0, str.length() - 1) + "     |";
                }
                else if (root.getRight() != null && left) {
                    str2 = str + "    |";
                }
                else if (root.getRight() != null && !left && isLeft) {
                    str2 = str + "    |";
                }
                else if (!isLeft) {
                    str2 = str.substring(0, str.length() - 1);
                }
                System.out.println(str2);
                System.out.println(str + "--- " + root.getName() + " (" + root.getId() + ", " + root.getKingdom()
                        + "): " + root.getWeight() + "kg");
            }
            printTree(root.getLeft(), true, str);
        }
    }
}
