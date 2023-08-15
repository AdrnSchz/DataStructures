package project.tables.UI;

import javax.swing.*;

public class Controller {
    private final JFrame frame;
    private final Histogram histogram;
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 800;

    public Controller(int minstrel, int knight, int peasant, int shrubber, int enchanter) {
        histogram = new Histogram(minstrel, knight, peasant, shrubber, enchanter);
        frame = new JFrame("Histogram");
        frame.add(histogram);
        frame.setSize(WIDTH, HEIGHT);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
