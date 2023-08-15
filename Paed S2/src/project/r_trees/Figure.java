package project.r_trees;

import project.CustomList;
import java.awt.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public abstract class Figure {

    public Figure(){
    }

    abstract Point getCenter();

    double getDistanceTo(Figure figure){
        Point point1 = this.getCenter();
        Point point2 = figure.getCenter();

        return sqrt(pow(point2.getX() - point1.getX(), 2) + pow(point2.getY() - point1.getY(), 2));
    }

    public abstract Point getMax();

    public abstract Point getMin();

    protected abstract void changeExtremes(Figure figure);

    public abstract Rectangle addShrub(Shrub shrub);

    protected abstract void addShrubReal(Shrub shrub, Rectangle parentRectangle);

    public abstract CustomList<Figure> getChildren();

    public abstract void drawFigure(Graphics2D g2d);

    protected void searchArea(Point firstPoint, Point secondPoint, CustomList<Shrub> shrubs) {
    }

    protected double getX() {
        return 0;
    }

    protected double getY() {
        return 0;
    }

    protected boolean inRange(Point firstPoint, Point secondPoint) {
        return false;
    }
}
