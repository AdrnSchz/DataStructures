package project.r_trees.UI;

import project.CustomList;
import project.r_trees.Figure;
import project.r_trees.Rectangle;

import javax.swing.*;
import java.awt.*;

public class RTreeUI extends Canvas {
    private Graphics2D graphics2D;
    private final project.r_trees.Rectangle root;

    public RTreeUI(project.r_trees.Rectangle root) {
        this.root = root;

        //this.setOpaque(false);
        this.setPreferredSize(new Dimension(1000, 500));
    }

    public void paint(Graphics graphics) {
        graphics2D = (Graphics2D) graphics;

        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, UIController.WIDTH, UIController.HEIGHT);

        drawTree(graphics2D);
    }

    private void drawTree(Graphics2D graphics2D) { // breadth first traversation from root
        CustomList<Figure> queue = new CustomList<>();
        queue.add(root);
        Figure current;

        while (!queue.isEmpty()) {
            current = queue.getAndRemove(0);
            current.drawFigure(graphics2D);

            if (current.getChildren().size() != 0) {
                for (int i = 0; i < current.getChildren().size(); i++) {
                    queue.add(current.getChildren().get(i));
                }
            }
        }
    }
}