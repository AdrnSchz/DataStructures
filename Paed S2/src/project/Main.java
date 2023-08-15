package project;

import project.graphs.GraphFunctionalities;
import project.r_trees.RTreesFunctionalities;
import project.tables.TablesFunctionalities;
import project.trees.NodeFunctionalities;

import java.util.*;

public class Main {
    public final static String ERROR_OPTION = "\nError. Please enter a valid option\n";
    public static final GraphFunctionalities graphFunctionalities = new GraphFunctionalities("Datasets/Graphs/graphsXS.paed");
    public static final NodeFunctionalities nodeFunctionalities = new NodeFunctionalities("Datasets/Trees/treeXXS.paed");
    public static final RTreesFunctionalities rTreesFunctionalities = new RTreesFunctionalities("Datasets/RTrees/rtreeXXL.paed");
    public static final TablesFunctionalities tablesFunctionalities = new TablesFunctionalities("Datasets/Tables/tablesXXL.paed");

    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int option = 0;

        do {
            System.out.println("""
                                    
                    '`^\\ The Hashy Grail /^ ́'
                                    
                    1. Concerning swallows and coconuts (Graphs)
                    2. The ways of science (Binary search trees)
                    3. Shrubberies (RTrees)
                    4. Of heretics and blasphemers (Tables)

                    5. Exit"""
            );

            try {
                System.out.print("\nChooseth an option: ");
                option = Integer.parseInt(sc.nextLine());

                switch (option) {
                    case 1 -> graphFunctionalities.menu();
                    case 2 -> nodeFunctionalities.menu();
                    case 3 -> rTreesFunctionalities.menu();
                    case 4 -> tablesFunctionalities.menu();
                    case 5 -> System.out.println("\nThy quest for the Hashy Grail is over...");
                    default -> System.out.println(ERROR_OPTION);
                }
            } catch (NumberFormatException e){
                System.out.println(ERROR_OPTION);
            }
        } while (option != 5);
    }
}