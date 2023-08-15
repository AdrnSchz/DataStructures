package project.tables;

import project.CustomList;

public class Node {
    private final CustomList<Suspect> suspects;

    public Node() {
        suspects = new CustomList<>();
    }

    public boolean isEmpty(){
        return suspects.isEmpty();
    }

    public void addSuspect(Suspect suspect){
        suspects.addLast(suspect);
    }

    public CustomList<Suspect> getSuspects() {
        return suspects;
    }

    public boolean removeSuspect(String key){
        boolean found = false;
        Suspect suspect = null;

        for (Suspect value : suspects) {
            if (key.equals(value.getName())) {
                suspect = value;
                found = true;
            }
        }
        if(found){
            suspects.remove(suspect);
            return true;
        }
        return false;
    }

    public Suspect getSuspect(String key){
        for (Suspect value : suspects) {
            if (key.equals(value.getName())) {
                return value;
            }
        }
        return null;
    }
}
