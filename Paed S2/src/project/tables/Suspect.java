package project.tables;

public class Suspect {
    private final String name;
    private final int numRabbits;
    private final String occupation;
    private boolean heretic;

    public Suspect(String name, int numRabbits, String occupation) {
        this.name = name;
        this.numRabbits = numRabbits;
        this.occupation = occupation;
        if (numRabbits > 1975 && !occupation.equals("KING") && !occupation.equals("QUEEN") && !occupation.equals("CLERGYMAN")) {
            heretic = true;
        } else {
            heretic = false;
        }
    }

    public String getName() {
        return name;
    }

    public int getNumRabbits() {
        return numRabbits;
    }

    public String getOccupation() {
        return occupation;
    }

    public boolean isHeretic() {
        return heretic;
    }

    public void markAsHeretic(boolean heretic) {
        this.heretic = heretic;
    }
}
