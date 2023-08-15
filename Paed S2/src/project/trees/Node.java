package project.trees;

import project.CustomList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Node {

    private int id;
    private int height;
    private String name;
    private float weight;
    private String kingdom;
    private Node left;
    private Node right;
    private Node parent;

    public Node(int id, String name, float weight, String kingdom) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.kingdom = kingdom;
        this.height = 0;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
    public Node(){
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public Node(String path) {
        this();

        try {
            FileReader f = new FileReader(path);
            BufferedReader bf = new BufferedReader(f);
            int numResidents = Integer.parseInt(bf.readLine());

            for (int i = 0; i < numResidents; i++) {
                String line = bf.readLine();
                StringTokenizer stn = new StringTokenizer(line, ";\n");
                int id = Integer.parseInt(stn.nextToken());
                String name = stn.nextToken();
                float weight = Float.parseFloat(stn.nextToken());
                String kingdom = stn.nextToken();

                if (i == 0){
                    this.id = id;
                    this.name = name;
                    this.weight = weight;
                    this.kingdom = kingdom;
                }
                else {
                    Node node = new Node(id, name, weight, kingdom);
                    this.addNode(node);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNode(Node newNode) {
        //Add node to the left
        if (this.weight < newNode.weight){
            if (this.right == null){
                this.right = newNode;
                newNode.parent = this;
            }
            else {
                this.right.addNode(newNode);
            }
        }
        //Add node to the right
        else {
            if (this.left == null){
                this.left = newNode;
                newNode.parent = this;
            }
            else {
                this.left.addNode(newNode);
            }
        }
        //Update heights
        updateHeight();
        //Balance
        checkBalance();
    }

    private void checkBalance(){
        //Check if node is unbalanced
        if(getBalanceFactor() >= 2){
            right.balance();
        }
        else if (getBalanceFactor() <= -2){
            left.balance();
        }
        //Check if parent is still unbalanced (deletion case)
        if(parent != null){
            parent.checkBalance();
        }
    }
    private void updateHeight(){
        this.height = Integer.max(getHeight(this.right), getHeight(this.left)) + 1;
    }

    private void balance() {
        //RR/LL case
        if ((parent.getBalanceFactor() >= 2 && getBalanceFactor() == 1) ||
                (parent.getBalanceFactor() <= -2 && getBalanceFactor() == -1)) {
            parent.rotateLeft();
        }
        //RL/LR case
        else if ((parent.getBalanceFactor() >= 2 && getBalanceFactor() == -1) ||
                (parent.getBalanceFactor() <= -2 && getBalanceFactor() == 1)) {
            this.rotateRight();
        }
        //Deletion case (bf = 2 and children bf = 0)
        else if (parent.getBalanceFactor() >= 2 || parent.getBalanceFactor() <= -2){
            parent.rotateLeft();
        }
    }
    private void rotateRight() {
        //RL case
        if(parent.getBalanceFactor() >= 2){
            Node tmp = left.right;
            parent.right = left;
            left.parent = parent;
            parent = left;
            left.right = this;
            left = tmp;
            if(tmp != null) {
                tmp.parent = this;
            }
        }
        //LR case
        else{
            Node tmp = right.left;
            parent.left = right;
            right.parent = parent;
            parent = right;
            right.left = this;
            right = tmp;
            if(tmp != null) {
                tmp.parent = this;
            }
        }
        //Update heights
        this.updateHeight();
        parent.updateHeight();
        //Continue rotating
        parent.parent.rotateLeft();
    }

    private void rotateLeft() {
        boolean rootRotated = false;

        //RR case
        if(getBalanceFactor() >= 2) {
            //Update parent
            if(parent != null) {
                if (parent.right == this) {
                    parent.right = right;
                } else {
                    parent.left = right;
                }
            }
            else{
                rootRotated = true;
            }
            //Update node
            Node tmp = right.left;
            right.parent = parent;
            right.left = this;
            parent = right;
            right = tmp;
            if (tmp != null) {
                tmp.parent = this;
            }
        }
        //LL case
        else {
            //Update parent
            if(parent != null) {
                if (parent.right == this) {
                    parent.right = left;
                } else {
                    parent.left = left;
                }
            }
            else{
                rootRotated = true;
            }
            //Update node
            Node tmp = left.right;
            left.parent = parent;
            left.right = this;
            parent = left;
            left = tmp;
            if (tmp != null) {
                tmp.parent = this;
            }
        }
        //Check if root was rotated
        if(rootRotated){
            switchRoot(parent);
        }
        //Update heights
        this.updateHeight();
        if(parent != null) {
            parent.updateHeight();
        }
    }

    private int getHeight(Node node){
        //Leaf
        if(node == null){
            return -1;
        }
        //Non-leaf
        else{
            return node.height;
        }
    }

    public CustomList<Node> getChildren(){
        CustomList<Node> children = new CustomList<>();
        if(left != null){
            children.addLast(left);
        }
        if(right != null){
            children.addLast(right);
        }
        return children;
    }

    public Node getNodeByID(int id){
        Node rightNode = null;
        Node leftNode = null;

        if(this.id == id){
            return this;
        }
        if(right != null){
            rightNode = right.getNodeByID(id);
        }
        if(left != null){
            leftNode = left.getNodeByID(id);
        }

        if (leftNode != null && leftNode.id == id){
            return leftNode;
        }
        else if (rightNode != null && rightNode.id == id){
            return rightNode;
        }
        else{
            return null;
        }
    }

    public void printDebug(){
        System.out.println("Node: " + (int)weight + " (" + height + ") bf: " + getBalanceFactor());

        if(left != null){
            System.out.println("\tLeft: " + (int)left.weight + " (" + getHeight(left) + ") bf: " + left.getBalanceFactor() + " – (" + (int)left.parent.weight + ")");
        }
        if(right != null){
            System.out.println("\tRight: " + (int)right.weight + " (" + getHeight(right) + ") bf: " + right.getBalanceFactor() + " – (" + (int)right.parent.weight + ")");
        }
        if(left!=null){
            left.printDebug();
        }
        if(right!= null){
            right.printDebug();
        }

    }
    private void updateHeights(){
        this.height = Integer.max(getHeight(right) + 1, getHeight(left) + 1);
        if(this.parent != null){
            parent.updateHeights();
        }
    }
    public void remove (Node nodeToRemove) {
        CustomList<Node> children = nodeToRemove.getChildren();
        Node parent = nodeToRemove.getParent();
        Node predecessor;

        //Remove root
        if(parent == null){
            predecessor = this.getPredecessor();
            //Connect predecessor's child to its parent
            if(predecessor.left != null){
                predecessor.left.parent = predecessor.parent;
                predecessor.left.updateHeights();
            }
            //Connect predecessor's parent to its possible child
            if(predecessor.parent.right == predecessor) {
                predecessor.parent.right = predecessor.left;
            }
            else{
                predecessor.parent.left = predecessor.left;
            }
            //Swap predecessor and root
            copyContent(predecessor);
            //Update heights
            predecessor.parent.updateHeights();
            //Balance
            predecessor.checkBalance();
        }
        //Remove regular node
        else {
            //Case 0: Simple remove
            if (children.size() == 0) {
                //Disconnect parent
                if (parent.left != null && parent.left.id == nodeToRemove.id) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
                //Update heights
                parent.updateHeights();
                //Balance
                parent.checkBalance();
            }
            //Case 1: Remove and substitute with child
            else if (children.size() == 1) {
                //Connect child to parent
                if (parent.left != null && parent.left.id == nodeToRemove.id) {
                    parent.left = children.get(0);
                } else {
                    parent.right = children.get(0);
                }
                children.get(0).parent = parent;
                //Update height
                children.get(0).updateHeights();
            }
            //Case 2: Substitute with predecessor
            else if (children.size() == 2) {
                predecessor = nodeToRemove.getPredecessor();
                //Connect parent to predecessor
                if (parent.left != null && parent.left.id == nodeToRemove.id) {
                    parent.left = predecessor;
                } else {
                    parent.right = predecessor;
                }
                //Predecessor not leaf
                if(predecessor.left != null){
                    predecessor.left.parent = predecessor.parent;
                    predecessor.left.updateHeights();
                }
                //Remove predecessor from its origin and connect its child (if it has one)
                if(predecessor.parent.right == predecessor) {
                    predecessor.parent.right = predecessor.left;
                }
                else{
                    predecessor.parent.left = predecessor.left;
                }
                //Replace nodeToRemove for predecessor
                predecessor.parent = parent;
                predecessor.right = nodeToRemove.right;
                nodeToRemove.right.parent = predecessor;
                predecessor.left = nodeToRemove.left;
                if(nodeToRemove.left != null) {
                    nodeToRemove.left.parent = predecessor;
                }
                //Update heights
                predecessor.updateHeights();
                //Balance
                predecessor.checkBalance();
            }
        }
    }

    private void copyContent(Node predecessor) {
        this.id = predecessor.id;
        this.weight = predecessor.weight;
    }

    private Node duplicate (Node node){
        Node duplicate = new Node();
        duplicate.id = node.id;
        duplicate.weight = node.weight;
        duplicate.parent = node.parent;
        duplicate.right = node.right;
        duplicate.left = node.left;

        return duplicate;
    }
    private void switchRoot(Node nodeToCpy){
        Node newRoot = duplicate(nodeToCpy);

        //Update not-root
        nodeToCpy.id = this.id;
        nodeToCpy.weight = this.weight;
        nodeToCpy.right = this.right;
        if(nodeToCpy.right != null){
            nodeToCpy.right.parent = nodeToCpy;
        }
        nodeToCpy.left = this.left;
        if(nodeToCpy.left != null){
            nodeToCpy.left.parent = nodeToCpy;
        }
        nodeToCpy.parent = this;
        nodeToCpy.updateHeight();

        //Update root
        this.id = newRoot.id;
        this.weight = newRoot.weight;
        if(newRoot.left != this) {
            this.left = newRoot.left;
            newRoot.left.parent = this;
        }else{
            this.left = nodeToCpy;
        }
        if(newRoot.right != this) {
            this.right = newRoot.right;
            newRoot.right.parent = this;
        }
        else{
            this.right = nodeToCpy;
        }
        this.parent = null;
        updateHeight();
    }

    private Node getPredecessor() {
        //Get first child to the left
        Node child = this.left;

        //Get furthest to the right in the subtree
        while(child.right != null){
            child = child.right;
        }
        return child;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public String getKingdom() {
        return kingdom;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node getParent(){
        return parent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public void getNodesInRange(CustomList<Node> witches, float min_weight, float max_weight) {
        if(this.weight >= min_weight && this.weight <= max_weight){
            witches.addLast(this);
        }
        if(this.weight > min_weight && this.left != null) {
           this.left.getNodesInRange(witches, min_weight, max_weight);
        }
        if(this.weight <= max_weight && this.right != null){
            this.right.getNodesInRange(witches, min_weight, max_weight);
        }
    }

    public void findEqualWeight(float weightToFind, StringBuilder log) {
        if (weight == weightToFind) {
            log.append("\t* %s (%d, %s): %skg\n".formatted(name, id, kingdom, Float.toString(weight)));
            if (left != null) {
                left.findEqualWeight(weightToFind, log);
            }
            if (right != null) {
                right.findEqualWeight(weightToFind, log);
            }
            return;
        }
        if (weight > weightToFind && left != null) {
            left.findEqualWeight(weightToFind, log);
        }
        else if (weight < weightToFind && right != null){
            right.findEqualWeight(weightToFind, log);
        }
    }

    public int getBalanceFactor(){
        return getHeight(right) - getHeight(left);
    }
}
