package sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Graph {
    private ArrayList<Node> nodes = new ArrayList<>();
    private HashSet<Edge> edgesToShow = new HashSet<>();
    private int[] window_size;
    public final int NODEOFFSET = 32;
    private Random r = new Random();

    public Graph(int[] size) {
        window_size = size;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public HashSet<Edge> getEdgesToShow() {
        return edgesToShow;
    }

    public void clearNodes() {
        nodes.clear();
    }

    public void generateGraph(int n, int edges) {
        nodes.clear();
        edgesToShow.clear();
        Edge tempEdge;
        Node tempNode;
        int tempInt;
        int tries = 0;

        for (int i = 0; i < n; i++) {
            nodes.add(new Node(new int[]{NODEOFFSET + r.nextInt(window_size[0] - NODEOFFSET), NODEOFFSET + r.nextInt(window_size[1] - NODEOFFSET)}, i));
        }

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < edges; j++) {
                tempInt = r.nextInt(nodes.size());

                while (tempInt == i || nodes.get(i).distanceTo(nodes.get(tempInt)) > 500) {
                    tempInt = r.nextInt(nodes.size());
                    tries++;

                    // Lets try 10 times to make short path
                    if (tempInt != i && tries > 10) {
                        tries = 0;
                        break;
                    }
                }

                nodes.get(i).addEdge(new Edge(nodes.get(i), nodes.get(tempInt)));
                nodes.get(tempInt).addEdge(new Edge(nodes.get(tempInt), nodes.get(i)));
            }

        }

        fillEdgesToShow();
    }


    public void printNodes() {
        ArrayList<Edge> currentEdges = new ArrayList<>();
        for (Node n : nodes) {
            n.toString();

            currentEdges.addAll(n.getEdges());
            for (Edge e : currentEdges) {
                e.printEdge();
            }
            currentEdges.clear();

        }
    }

    private void fillEdgesToShow() {
        for(Node n : nodes){
            for(Edge e :  n.getEdges()){
                if(!edgesToShow.contains(e)){
                    edgesToShow.add(e);
                }
            }
        }

        System.out.println(edgesToShow.size());
    }

}
