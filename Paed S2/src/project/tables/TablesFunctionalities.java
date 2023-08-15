package project.tables;

import project.CustomList;
import project.Main;
import project.tables.UI.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TablesFunctionalities {
    public static int R;
    public Node[] table;
    public String[] occupations = {"MINSTREL", "KNIGHT", "KING", "QUEEN", "PEASANT", "SHRUBBER", "CLERGYMAN", "ENCHANTER"};
    public Scanner sc = new Scanner(System.in);

    public TablesFunctionalities(String path) {
        createTable(path);
    }

    public void createTable(String path){
        try {
            FileReader f = new FileReader(path);
            BufferedReader bf = new BufferedReader(f);
            int numSuspects = Integer.parseInt(bf.readLine());
            initializeTable(numSuspects);

            for (int i = 0; i < numSuspects; i++) {
                String line = bf.readLine();
                StringTokenizer stn = new StringTokenizer(line, ";\n");

                String name = stn.nextToken();
                int numRabbits = Integer.parseInt(stn.nextToken());
                String occupation = stn.nextToken();

                Suspect suspect = new Suspect(name, numRabbits, occupation);
                table[hash(name)].addSuspect(suspect);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeTable(int size){
        R = size;
        table = new Node[R];
        for (int i = 0; i < table.length; i++) {
            table[i] = new Node();
        }
    }

    private int hash(String key) {
        int pos = 0;
        for (int i = 0; i < key.length(); i++) {
            pos += key.charAt(i);
        }
        return pos % R;
    }

    private int advancedHash(String key) {

        // Conversion from String to integer value using character value with bit shifting summation
        long pos = 0;
        for (int i = 0; i < key.length(); i++) {
            pos += (long) key.charAt(i) << i;
        }

        // Generate table position with value square and obtention of log(R) central digits.
        pos = pos * pos;

        int logR = (int) Math.round(Math.log10(R));

        String posStr = String.valueOf(pos);

        if (posStr.length() <= logR) {
            return (int) pos;
        }

        int posHalfIndex = posStr.length() / 2;

        posStr = posStr.substring(
                (int) (posHalfIndex - Math.floor(logR / 2F)),
                posHalfIndex + Math.round(logR / 2F)
        );

        // R modulus to avoid value out of bounds
        return Integer.parseInt(posStr) % R;
    }

    public void menu() {

        String option;
        do {
            System.out.println("""
    
                    \tA. Add suspect
                    \tB. Remove suspect
                    \tC. Edict of Grace
                    \tD. Final judgement (one suspect)
                    \tE. Final judgement (range)
                    \tF. Histogram by occupation
    
                    \tG. Go back"""
            );

            System.out.print("\nWhich functionality doth thee want to run? ");
            option = sc.nextLine().toUpperCase();
            switch (option) {
                case "A":
                    addSuspect();
                    break;
                case "B":
                    removeSuspect();
                    break;
                case "C":
                    edictOfGrace();
                    break;
                case "D":
                    finalOneSuspect();
                    break;
                case "E":
                    finalRange();
                    break;
                case "F":
                    histogramByOccupation();
                    break;
                case "G":
                    return;
                default:
                    System.out.print(Main.ERROR_OPTION);
                    System.out.println();
            }
        } while (true);
    }

    public void addSuspect() {
        String name = getName("\nSuspect's name: ");
        int numRabbits = getRabbits("Number of rabbits seen: ");
        String occupation;
        do {
            occupation = getName("Suspect’s occupation: ");
        } while (!validOccupation(occupation));

        table[advancedHash(name)].addSuspect(new Suspect(name, numRabbits, occupation.toUpperCase()));
    }

    private void printDebug(){
        for (int i = 0; i < table.length; i++) {
            System.out.println(i + ":");
            if(!table[i].isEmpty()){
                for (int j = 0; j < table[i].getSuspects().size(); j++) {
                    System.out.println("\t" + j + ": " + table[i].getSuspects().get(j).getName());
                }
            }
        }
    }

    private boolean validOccupation(String occupation){
        for (String s : occupations) {
            if (s.equals(occupation.toUpperCase())) {
                return true;
            }
        }
        System.out.println("Please enter one of the following: MINSTREL, KNIGHT, KING, QUEEN, PEASANT, " +
                "SHRUBBER, CLERGYMAN or ENCHANTER");
        return false;
    }

    public void removeSuspect() {
        String name = getName("\nSuspect's name: ");

        if(table[advancedHash(name)].removeSuspect(name)){
            System.out.println("\n" + name + " has been publicly executed.");
        }
        else{
            System.out.println("\nSorry, " + name + " isn't a suspect we know.");
        }
    }

    public void edictOfGrace() {
        String name = getName("\nSuspect's name: ");
        String heretic = "N";

        do {
            if (!heretic.equals("Y") && !heretic.equals("N")) {
                System.out.println("\nPlease enter a valid option.\n");
            }
            System.out.print("Mark as heretic (Y/N)? ");
            heretic = sc.nextLine().toUpperCase();
        }while(!heretic.equals("Y") && !heretic.equals("N"));

        long begin = System.nanoTime();
        long beginM = System.currentTimeMillis();
        Suspect suspect = table[hash(name)].getSuspect(name);
        long end = System.nanoTime();
        long endM = System.currentTimeMillis();
        System.out.print("Time in nanoseconds = ");
        System.out.println((end - begin));
        System.out.print("Time in milliseconds = ");
        System.out.println((endM - beginM));

        if (suspect != null) {
            if (heretic.equals("Y")) {
                if (!suspect.getOccupation().equals("KING") && !suspect.getOccupation().equals("QUEEN") &&
                        !suspect.getOccupation().equals("CLERGYMAN")) {
                    suspect.markAsHeretic(true);
                    System.out.println("\nThe Spanish Inquisition concluded that " + name + " is a heretic.");
                } else {
                    System.out.println("\nSorry, " + name + " can't be a heretic.");
                }
            } else {
                suspect.markAsHeretic(false);
                System.out.println("\nThe Spanish Inquisition concluded that " + name + " is not a heretic.");
            }
        } else {
            System.out.println("\nSorry, " + name + " isn't a suspect we know.");
        }
    }

    public void finalOneSuspect() {
        String name = getName("\nSuspect's name: ");

        Suspect suspect = table[advancedHash(name)].getSuspect(name);
        if(suspect != null){
            showSuspect(suspect);
        }
        else{
            System.out.println("\nSorry, " + name + " isn't a suspect we know.");
        }
    }

    public void finalRange() {
        CustomList<Suspect> suspects;
        int minRabbits, maxRabbits;
        boolean valid;

        minRabbits = getRabbits("\nMinimum number of rabbits: ");
        do {
            maxRabbits = getRabbits("Maximum number of rabbits: ");
            valid = true;
            if(maxRabbits <= minRabbits){
                valid = false;
                System.out.println("Please enter a number greater than the previous one.");
            }
        }while(!valid);

        suspects = searchRange(minRabbits, maxRabbits);

        if(suspects.size() != 0) {
            for (Suspect suspect : suspects) {
                showSuspect(suspect);
            }
        }
        else{
            System.out.println("\nSorry, we couldn't find any suspects who have seen this amount of rabbits.");
        }
    }

    private CustomList<Suspect> searchRange(int minRabbits, int maxRabbits) {
        CustomList<Suspect> inRange = new CustomList<>();

        for (Node node : table) {
            if (!node.isEmpty()) {
                CustomList<Suspect> suspects = node.getSuspects();
                for (Suspect suspect : suspects) {
                    if (suspect.getNumRabbits() >= minRabbits
                            && suspect.getNumRabbits() <= maxRabbits) {
                        inRange.addLast(suspect);
                    }
                }
            }
        }
        return inRange;
    }

    public void histogramByOccupation() {
        //{"MINSTREL", "KNIGHT", "KING", "QUEEN", "PEASANT", "SHRUBBER", "CLERGYMAN", "ENCHANTER"}
        int minstrel = 0, knight = 0, peasant = 0, shrubber = 0, enchanter = 0;

        for (Node node : table) {
            if (!node.isEmpty()) {
                CustomList<Suspect> suspects = node.getSuspects();
                for (Suspect suspect : suspects) {
                    if (suspect.isHeretic()) {

                        switch (suspect.getOccupation()) {
                            case "MINSTREL" -> minstrel++;
                            case "KNIGHT" -> knight++;
                            case "PEASANT" -> peasant++;
                            case "SHRUBBER" -> shrubber++;
                            case "ENCHANTER" -> enchanter++;
                        }
                    }
                }
            }
        }
        new Controller(minstrel, knight, peasant, shrubber, enchanter);
    }

    private String getName(String message){
        String name;
        boolean validInput;

        do {
            System.out.print(message);
            name = sc.nextLine();
            validInput = true;
            if(name.equals("")){
                validInput = false;
                System.out.println("Please enter a valid name");
            }
        } while (!validInput);

        return name;
    }

    private int getRabbits(String message){
        int num = 0;
        boolean validInput;

        do {
            try {
                System.out.print(message);
                num = Integer.parseInt(sc.nextLine());
                validInput = true;
            } catch (NumberFormatException | NoSuchElementException e) {
                validInput = false;
                System.out.println("Please enter a valid input");
            }
        } while (!validInput);

        return num;
    }

    private void showSuspect(Suspect suspect) {
        System.out.println("\nRegister for “"+ suspect.getName() +"” :");
        System.out.println("\t* Number of rabbits seen: " + suspect.getNumRabbits());
        System.out.println("\t* Occupation: " + suspect.getOccupation());
        System.out.print("\t* Heretic? ");
        if(suspect.isHeretic()){
            System.out.println("Yes");
        }
        else{
            System.out.println("No");
        }
    }
}
