package project.r_trees.UI;

import project.r_trees.Rectangle;

import javax.swing.*;

public class UIController {
    private final JFrame frame;
    private final RTreeUI rTreeUI;
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 800;

    public UIController(Rectangle root) {
        rTreeUI = new RTreeUI(root);
        frame = new JFrame("R-Tree Visualization");
        frame.add(rTreeUI);
        frame.setSize(WIDTH, HEIGHT);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
