package project.r_trees;

import project.CustomList;
import java.awt.*;

public class Point extends Figure{
    private double x;
    private double y;

    public Point(){
    }

    public Point(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    Point getCenter() {
        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public Point getMax() {
        return this;
    }
    @Override
    public Point getMin() {
        return this;
    }
    @Override
    protected void changeExtremes(Figure figure) {
    }
    @Override
    public Rectangle addShrub(Shrub shrub) {
        return null;
    }
    @Override
    protected void addShrubReal(Shrub shrub, Rectangle parentRectangle) {
    }
    public CustomList<Figure> getChildren() {
        return new CustomList<Figure>();
    }
    public void drawFigure(Graphics2D g2d) {
        g2d.drawLine(0,0,1000,700);
    }

    public boolean inRange(Point firstPoint, Point secondPoint) {
        return (x >= Math.min(firstPoint.getX(), secondPoint.getX()) &&
                x <= Math.max(firstPoint.getX(), secondPoint.getX()) &&
                y >= Math.min(firstPoint.getY(), secondPoint.getY()) &&
                y <= Math.max(firstPoint.getY(), secondPoint.getY()));
    }
}
