package project.r_trees;

import project.CustomList;

import java.awt.*;
import java.util.Random;

public class Rectangle extends Figure {

    // min is bottom left
    private Point min;

    //max is top right
    private Point max;

    private CustomList<Figure> children;

    private static final Color[] colorsF =  {
            new Color(255, 0, 0, 40),       // redF
            new Color(0, 0, 255, 40),       // blueF
            new Color(255, 255, 0, 40),     // yellowF
            new Color(255, 165, 0, 40),     // orangeF
            new Color(255, 192, 203, 40),   // pinkF
            new Color(128, 0, 128, 40),     // purpleF
            new Color(165, 42, 42, 40)      // brownF
    };

    private static final Color[] colors = {
            new Color(255, 0, 0, 128),      // redS
            new Color(0, 0, 255, 128),      // blueS
            new Color(255, 255, 0, 128),    // yellowS
            new Color(255, 165, 0, 128),    // orangeS
            new Color(255, 192, 203, 128),  // pinkS
            new Color(128, 0, 128, 128),    // purpleS
            new Color(165, 42, 42, 128)     // brownS
    };

    public Rectangle(Point min, Point max) {
        this.min = min;
        this.max = max;
        this.children = new CustomList<>();
    }

    public Rectangle() {
        super();
        this.min = new Point();
        this.max = new Point();
        this.children = new CustomList<>();
    }

    @Override
    Point getCenter() {
        Point center = new Point();
        center.setX((max.getX() + min.getX())/ 2);
        center.setY((max.getY() + min.getY())/ 2);
        return center;
    }

    @Override
    public Point getMin() {
        return min;
    }

    @Override
    public Point getMax() {
        return max;
    }

    public CustomList<Figure> getChildren() {
        return children;
    }

    public void setMin(Point min) {
        this.min = min;
    }

    public void setMax(Point max) {
        this.max = max;
    }

    private CustomList<Figure> popChildren(){

        CustomList<Figure> children = this.children.clone();
        this.children = new CustomList<>();
        this.max = new Point();
        this.min = new Point();
        return children;
    }

    private double getPerimeter(){
        double width = this.max.getX() - this.min.getX();
        double height = this.max.getY() - this.min.getY();
        return 2*width + 2*height;
    }

    public void modifyPoints(Point point1, Point point2){

        this.min.setX(point1.getX());
        this.min.setY(point1.getY());

        this.max.setX(point2.getX());
        this.max.setY(point2.getY());
    }

    @Override
    protected void changeExtremes(Figure figure){

        if (figure.getMin().getX() < this.min.getX()){
            this.min.setX(figure.getMin().getX());
        }
        if (figure.getMin().getY() < this.min.getY()){
            this.min.setY(figure.getMin().getY());
        }
        if (figure.getMax().getX() > this.max.getX()){
            this.max.setX(figure.getMax().getX());
        }
        if (figure.getMax().getY() > this.max.getY()){
            this.max.setY(figure.getMax().getY());
        }
    }

    private double extraArea(Point rectangleMin, Point rectangleMax, Figure figure){
        Rectangle rectangle = new Rectangle(rectangleMin, rectangleMax);
        Rectangle possibleRectangle = new Rectangle(rectangleMin, rectangleMax);
        possibleRectangle.changeExtremes(figure);

        return rectangle.getPerimeter() - possibleRectangle.getPerimeter();
    }

    public Rectangle addShrub(Shrub shrub){
        Rectangle parentOfRoot = new Rectangle(this.min, this.max);
        parentOfRoot.children.addLast(this);
        addShrubReal(shrub, parentOfRoot);

        if (parentOfRoot.children.size() > 1){
            return parentOfRoot;
        }
        return this;
    }

    //this method is run on the root rectangle and will then be called recursively
    protected void addShrubReal(Shrub shrub, Rectangle parentRectangle){

        if (this.children.size() == 0){
            this.children.addLast(shrub);
            this.modifyPoints(shrub, shrub);
            return;
        }
        else if (this.children.get(0) instanceof Rectangle){

            double leastExtraArea = Integer.MAX_VALUE;
            int LEAIndex = Integer.MAX_VALUE;

            //we add the shrub to rectangle that has to expand the least
            for (int i=0; i<this.children.size(); i++){
                double extraArea = extraArea(this.children.get(i).getMin(), this.children.get(i).getMax(), shrub);
                if (extraArea < leastExtraArea){
                    leastExtraArea = extraArea;
                    LEAIndex = i;
                }
            }
            this.children.get(LEAIndex).changeExtremes(shrub);
            this.children.get(LEAIndex).addShrubReal(shrub,this);

        }
        else {
            this.children.addLast(shrub);
            this.changeExtremes(shrub);
        }
        if (this.children.size() <= RTreesFunctionalities.MAX_CHILDREN){
            return;
        }

        CustomList<Figure> poppedChildren = this.popChildren();
        Rectangle newRectangle = new Rectangle();
        //we set these figures to random ones as creating new ones would take up a lot of space
        Figure furthest1 = poppedChildren.get(0);
        Figure furthest2 = poppedChildren.get(0);

        //we find the furthest two figures
        double largestDistance = 0;
        for (int i=0; i<poppedChildren.size(); i++){
            for (int j=i+1; j<poppedChildren.size(); j++){
                double distance = poppedChildren.get(i).getDistanceTo(poppedChildren.get(j));
                if (distance > largestDistance){
                    furthest1 = poppedChildren.get(i);
                    furthest2 = poppedChildren.get(j);
                    largestDistance = distance;
                }
            }
        }
//we create new rectangles around the furthest figures, add them, remove them from poppedChildren
        this.modifyPoints(furthest1.getMin(), furthest1.getMax());
        this.children.addLast(furthest1);
        poppedChildren.remove(furthest1);
        newRectangle.modifyPoints(furthest2.getMin(), furthest2.getMax());
        newRectangle.children.addLast(furthest2);
        poppedChildren.remove(furthest2);

        for (Figure poppedChild : poppedChildren) {

            double expandedArea1 = extraArea(this.min, this.max, poppedChild);
            double expandedArea2 = extraArea(newRectangle.min, newRectangle.max, poppedChild);

            if (expandedArea1 < expandedArea2) {
                this.children.addLast(poppedChild);
                this.changeExtremes(poppedChild);
            } else {
                newRectangle.children.addLast(poppedChild);
                newRectangle.changeExtremes(poppedChild);
            }
        }
        parentRectangle.children.addLast(newRectangle);
        parentRectangle.changeExtremes(newRectangle);
    }

    protected boolean removeShrub(double x, double y, CustomList<Rectangle> parents) {

        // If no shrubs available
        if (this.children.size() == 0) {
            return false;
        }
        else if (this.children.get(0) instanceof Rectangle) {

            // Recursive call to the rectangle that probably contains shrub
            for (Figure child : children) {
                Rectangle rectangle = (Rectangle) child;
                if (x >= rectangle.getMin().getX() && y >= rectangle.getMin().getY()) {
                    if (x <= rectangle.getMax().getX() && y <= rectangle.getMax().getY()) {
                        parents.add(this);
                        if (rectangle.removeShrub(x, y, parents)) {
                            return true;
                        }
                        else {
                            parents.popLast();
                        }
                    }
                }
            }
        }
        else {
            // Find shrub and remove if exists, then update tree (resizing & minimum values check)
            for (int i = 0; i < children.size(); i++) {
                Point point = (Point) children.get(i);
                if (point.getX() == x && point.getY() == y) {
                    children.removeByIndex(i);
                    updateTree(parents);
                    return true;
                }
            }
        }
        return false;
    }

    private void updateTree(CustomList<Rectangle> parents) {

        CustomList<Figure> leafFigures = this.getChildren();

        // Check if MIN_CHILDREN is met
        if (leafFigures.size() < RTreesFunctionalities.MIN_CHILDREN) {
            Rectangle parent  = parents.popLast();
            parent.getChildren().remove(this);
            for (Figure shrubToReInsert : leafFigures) {
                RTreesFunctionalities.getRoot().addShrub((Shrub) shrubToReInsert);
            }
            parent.updateTree(parents);
        }
        else {
            double minFurthestX = this.max.getX(), minFurthestY = this.max.getY(),
                    maxFurthestX = this.min.getX(), maxFurthestY = this.min.getY();

            // Resize leafs' parent rectangle if possible
            for (Figure leafShrub : leafFigures) {

                Point leaf = (Point) leafShrub;
                if (leaf.getX() < minFurthestX) {
                    minFurthestX = leaf.getX();
                }
                if (leaf.getY() < minFurthestY) {
                    minFurthestY = leaf.getY();
                }
                if (leaf.getX() > maxFurthestX) {
                    maxFurthestX = leaf.getX();
                }
                if (leaf.getY() > maxFurthestY) {
                    maxFurthestY = leaf.getY();
                }
            }

            this.min.setX(minFurthestX);
            this.min.setY(minFurthestY);
            this.max.setX(maxFurthestX);
            this.max.setY(maxFurthestY);
        }
    }

    @Override
    public void drawFigure(Graphics2D g2d) {
        int i = new Random().nextInt(colors.length);

        g2d.setColor(colors[i]);
        g2d.draw(getRectangle());
        g2d.setColor(colorsF[i]);
        g2d.fill(getRectangle());
    }

    private java.awt.Rectangle getRectangle(){
        double multX = RTreesFunctionalities.xMulti, multY = RTreesFunctionalities.yMulti;
        double alX = RTreesFunctionalities.alocateX, alY = RTreesFunctionalities.alocateY;
        int scX = RTreesFunctionalities.scaleX, scY = RTreesFunctionalities.scaleY;
        int  y = (int) (((min.getY() + 4.1) * multY) - alY), x = (int) (((min.getX() - 53.8) * multX) - alX);
        int width = (int) (scX * multX * (max.getX() - min.getX()));
        int height = (int) (scY * multY * (max.getY() - min.getY()) * -1);

        return new java.awt.Rectangle(x, y, width, height);
    }

    public boolean inRange(Point firstPoint, Point secondPoint) {
        double minX =  Math.min(firstPoint.getX(), secondPoint.getX());
        double minY = Math.min(firstPoint.getY(), secondPoint.getY());
        double maxX = Math.max(firstPoint.getX(), secondPoint.getX());
        double maxY = Math.max(firstPoint.getY(), secondPoint.getY());

        return (minX >= min.getX() && minX <= max.getX() && minY >= min.getY() && minY <= max.getY()) ||
                (maxX >= min.getX() && maxX <= max.getX() && maxY >= min.getY() && maxY <= max.getY())||
                (maxX >= min.getX() && maxX <= max.getX() && minY >= min.getY() && minY <= max.getY())||
                (minX >= min.getX() && minX <= max.getX() && maxY >= min.getY() && maxY <= max.getY())||
                (minX <= min.getX() && maxX >= max.getX() && minY <= min.getY() && maxY >= max.getY());
    }

    public void searchArea(Point firstPoint, Point secondPoint, CustomList<Shrub> shrubs) {
        //Check all children
        for (Figure child : children) {
            //Leaf
            if (child instanceof Shrub && child.inRange(firstPoint, secondPoint)) {
                shrubs.addLast((Shrub) child);
            }
            //Non-leaf
            else if (child.inRange(firstPoint, secondPoint)) {
                //Check all rectangles
                child.searchArea(firstPoint, secondPoint, shrubs);
            }
        }
    }
}