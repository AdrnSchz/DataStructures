package project.r_trees;

import java.awt.*;

public class Shrub extends Point{
    private Color color;
    private String type;
    private double size;

    public Shrub(String type, double size, double x, double y, Color color) {
        super(x, y);
        this.color = color;
        this.type = type;
        this.size = size;
    }

    public Shrub() {

    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public double getSize() {
        return size;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public void drawFigure(Graphics2D g2d) {
        double multX = RTreesFunctionalities.xMulti;
        double multY = RTreesFunctionalities.yMulti;
        double alX = RTreesFunctionalities.alocateX, alY = RTreesFunctionalities.alocateY;
        int scX = RTreesFunctionalities.scaleX, scY = RTreesFunctionalities.scaleY;
        if (scY < 0) {
            scY *= -1;
        }
        int x = (int) ((((getX() - 53.8) * multX) - alX) * scX);
        int y = (int) ((((getY() + 4.1) * multY) - alY) * scY);
        g2d.setColor(color);
        //g2d.drawLine(x, y, x + 3, y + 3);
        g2d.drawOval(x, y, 3, 3);
    }
}
