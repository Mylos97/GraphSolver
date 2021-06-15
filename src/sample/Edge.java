package sample;

import java.util.Random;

public class Edge {

    private Node from;
    private Node to;
    private double distance;
    private boolean selected = false;
    private Random r = new Random();

    // Add a bonus random distance for the edge
    public Edge(Node nFrom, Node nTo) {
        from = nFrom;
        to = nTo;
        distance = Math.sqrt(Math.pow(from.getPos()[0] + to.getPos()[0], 2) + Math.pow(from.getPos()[1] + to.getPos()[1], 2));
    }

    public double getDistance() {
        return distance;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void printEdge() {
        System.out.println("    From : " + from.getId() + " to : " + to.getId() + " distance : " + distance);
    }

    @Override
    public int hashCode(){
        final int prime = 97;
        int result = 1;
        result = prime * (from.getPos()[0] + to.getPos()[0]);

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Edge) {
            if (((Edge) object).getFrom().getId() == to.getId() && ((Edge) object).getTo().getId() == from.getId()) {
                return true;
            }
        }

        return false;
    }

}
