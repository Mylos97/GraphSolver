package sample;

import java.util.ArrayList;

public class Node {
    private int pos[];
    private int id;
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private boolean selected = false;
    private boolean hoverOver = false;
    private Node previous = null;
    private Integer distance = Integer.MAX_VALUE;
    private Integer aStarDistance = Integer.MAX_VALUE;

    public Node(int[] position, int idNode) {
        pos = position;
        id = idNode;
    }

    public int getId() {
        return id;
    }

    public int[] getPos() {
        return pos;
    }

    public Integer getDistance() {
        return distance;
    }

    public Node copy() {
        Node copy = new Node(this.pos, this.id);
        copy.setDistance(this.distance);
        copy.setSelected(this.selected);

        for (Edge e : this.edges) {
            copy.addEdge(e);
        }

        return copy;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public void setaStarDistance(Integer dist) {
        this.aStarDistance = dist;
    }

    public Integer getaStarDistance() {
        return aStarDistance;
    }

    public void setPrevious(Node n) {
        this.previous = n;
    }

    public Node getPrevious() {
        return previous;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean b) {
        selected = b;
    }

    public void setHoverOver(Boolean b) {
        hoverOver = b;
    }

    public boolean isHoverOver() {
        return hoverOver;
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }

    public void addEdgebetween(Edge e, Node n) {
        edges.add(e);
        n.addEdge(new Edge(e.getTo(), this));
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    @Override
    public int hashCode(){
        final int prime = 3671;
        final int prime2 = 6481;
        int result = 1;
        result = prime*id + pos[0] * prime + pos[1] * prime2;

        return result;
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof Node) {
            if (((Node) object).getId() == this.id) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        System.out.println("Pos[" + pos[0] + ", " + pos[1] + "]" + " id: " + id);
        return "";
    }

    public boolean edgeValid(Edge e) {
        if (this.edges.size() == 0) {
            return false;
        }

        for (Edge ed : edges) {
            if (ed.equals(e)) {
                return true;
            }
        }

        return false;
    }

    public Integer distanceTo(Node n) {
        return (int) Math.sqrt(Math.pow(pos[0] - n.getPos()[0], 2) + Math.pow(pos[1] - n.getPos()[1], 2));
    }

    public void printNodeandEdges() {
        System.out.println("Node pos [" + pos[0] + "," + pos[1] + "]" + " distance: " + distance);
        for (Edge e : edges) {
            e.printEdge();
        }
    }

    public void printNode() {
        if (previous == null) {
            System.out.println("Start Node " + id + " Distance : " + distance);
        } else {
            System.out.println("Node " + id + " Distance " + distance + " Previous " + previous.getId());
        }
    }

    public void mouseClick() {
        selected = !selected;
    }
}
