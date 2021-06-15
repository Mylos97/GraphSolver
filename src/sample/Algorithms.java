package sample;

import java.util.*;

public class Algorithms {

    private Graph g;
    private NodeComparator nodeComparator = new NodeComparator();
    private EdgeComparator edgeComparator = new EdgeComparator();
    private Random r = new Random();
    private ArrayList<Node> shortestPathNodes = new ArrayList<>();
    private ArrayList<Edge> triedShortestPathEdges = new ArrayList<>();
    private ArrayList<Edge> shortestPathEdges = new ArrayList<>();
    private long duration = 0;
    private Integer distance = 0;

    public Algorithms(Graph graph) {
        this.g = graph;
    }

    public long getDuration() {
        return duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public ArrayList<Edge> getTriedShortestPathEdges() {
        return triedShortestPathEdges;
    }

    public ArrayList<Node> getShortestPathNodes() {
        return shortestPathNodes;
    }

    public ArrayList<Edge> getShortestPathEdges() {
        return shortestPathEdges;
    }

    public void clearShortestPathEdges() {
        triedShortestPathEdges.clear();
    }

    public void Dijkstras() {
        long startTime = System.nanoTime();
        resetAlgorithms();

        ArrayList<Node> copyArray = new ArrayList<>();
        copyArray.addAll(copyNodes());


        Node[] selectedNodes = new Node[2];
        int placement = 0;
        ArrayList<Node> settledNodes = new ArrayList<>();
        ArrayList<Node> unsettledNodes = new ArrayList<>();


        for (Node n : copyArray) {
            if (n.getSelected()) {
                selectedNodes[placement] = n;
                if (placement == 0) {
                    n.setDistance(0);
                }
                placement++;
            }
            unsettledNodes.add(n);
        }

        Collections.sort(unsettledNodes, nodeComparator);

        Node currentNode;
        Node updateNode;

        ArrayList<Edge> currentEdges = new ArrayList<>();

        Integer tempDistance = 0;

        if (selectedNodes[0] != null) {

            while (unsettledNodes.size() != 0) {
                currentNode = unsettledNodes.get(0);
                unsettledNodes.remove(0);
                settledNodes.add(currentNode);

                currentEdges.addAll(currentNode.getEdges());
                removeVisited(currentEdges, settledNodes);

                for (Edge e : currentEdges) {
                    updateNode = findNode(unsettledNodes, e.getTo());
                    tempDistance = (int) (currentNode.getDistance() + e.getDistance());

                    if (updateNode != null && tempDistance < updateNode.getDistance()) {
                        updateNode.setDistance(tempDistance);
                        updateNode.setPrevious(currentNode);
                    }

                }

                unsettledNodes.sort(nodeComparator);

                currentEdges.clear();

                if (currentNode.equals(selectedNodes[1])) {
                    break;
                }
            }

            shortestPathNodes.addAll(settledNodes);
            triedShortestPathEdges.addAll(createShortestEdges(settledNodes));
            getShortestPathRecursion(selectedNodes[1]);

        }

        long endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
    }

    public void Astar() {
        resetAlgorithms();
        long startTime = System.nanoTime();

        ArrayList<Node> copyArray = new ArrayList<>();
        ArrayList<Edge> tempEdges = new ArrayList<>();
        HashSet<Node> unsettledNodes = new HashSet<>();
        HashSet<Node> settledNodes = new HashSet<>();
        copyArray.addAll(copyNodes());

        Node[] selectedNodes = new Node[2];
        int placement = 0;
        Integer tempDistance = 0;
        Node currentNode = null;
        Node updateNode = null;


        for (Node n : copyArray) {
            if (n.getSelected()) {
                selectedNodes[placement] = n;
                if (placement == 0) {
                    n.setDistance(0);
                }
                placement++;
            }
            unsettledNodes.add(n);
        }

        while (!unsettledNodes.isEmpty()) {
            currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            tempEdges.addAll(currentNode.getEdges());
            removeVisited(tempEdges, settledNodes);

            for (Edge e : tempEdges) {
                updateNode = findNode(unsettledNodes, e.getTo());

                if (updateNode != null) {

                    tempDistance = (int) (currentNode.getDistance() + e.getDistance() + distanceToSelected(selectedNodes[1], updateNode));

                    if (tempDistance < updateNode.getaStarDistance()) {
                        updateNode.setaStarDistance(tempDistance);
                        updateNode.setDistance((int) (currentNode.getDistance() + e.getDistance()));
                        updateNode.setPrevious(currentNode);
                    }

                }


            }

            tempEdges.clear();
            settledNodes.add(currentNode);

            if (currentNode.equals(selectedNodes[1])) {
                break;
            }
        }

        shortestPathNodes.addAll(settledNodes);
        triedShortestPathEdges.addAll(createShortestEdges(settledNodes));
        getShortestPathRecursion(selectedNodes[1]);

        long endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
    }

    public void Prims() {
        long start = System.nanoTime();
        resetAlgorithms();

        ArrayList<Node> allNodes = new ArrayList<>();
        HashSet<Node> settledNodes = new HashSet<>();
        PriorityQueue<Edge> unsettledEdges = new PriorityQueue<>(100, edgeComparator);
        ArrayList<Edge> chosenEdges = new ArrayList<>();
        ArrayList<Edge> triedEdges = new ArrayList<>();


        allNodes.addAll(copyNodes());

        for (Node n : allNodes) {
            unsettledEdges.addAll(n.getEdges());
            triedEdges.addAll(n.getEdges());
        }

        int tempInt = 0;
        Node currentNode;
        Edge currentEdge;

        while (settledNodes.size() != allNodes.size()) {
            currentEdge = unsettledEdges.poll();
            if (!settledNodes.contains(currentEdge.getTo())) {
                distance += (int) currentEdge.getDistance();
                settledNodes.add(currentEdge.getTo());
                chosenEdges.add(currentEdge);
            }
        }

        triedShortestPathEdges.addAll(triedEdges);
        shortestPathNodes.addAll(settledNodes);
        shortestPathEdges.addAll(chosenEdges);

        long end = System.nanoTime();
        duration = (end - start) / 1000000;
    }

    private ArrayList<Node> copyNodes() {
        ArrayList<Node> output = new ArrayList<>();

        for (Node n : g.getNodes()) {
            output.add(n.copy());
        }

        return output;
    }

    private ArrayList<Edge> removeVisited(ArrayList<Edge> edges, ArrayList<Node> settledNodes) {
        ArrayList<Edge> output = new ArrayList<>();
        output.addAll(edges);

        for (Edge e : edges) {
            for (Node n : settledNodes) {
                if (n.getId() == e.getTo().getId()) {
                    output.remove(e);
                }
            }
        }

        return output;
    }


    private ArrayList<Edge> removeVisited(ArrayList<Edge> edges, Set<Node> settledNodes) {
        ArrayList<Edge> output = new ArrayList<>();
        output.addAll(edges);

        for (Edge e : edges) {
            for (Node n : settledNodes) {
                n.equals(e.getTo());
                output.remove(e);
            }
        }

        return output;
    }

    private ArrayList<Edge> createShortestEdges(ArrayList<Node> nodes) {
        ArrayList<Edge> output = new ArrayList<>();

        for (Node n : nodes) {
            if (n.getPrevious() != null) {
                output.add(new Edge(n, n.getPrevious()));
            }
        }

        return output;
    }

    private HashSet<Edge> createShortestEdges(Set<Node> nodes) {
        HashSet<Edge> output = new HashSet<>();

        for (Node n : nodes) {
            if (n.getPrevious() != null) {
                output.add(new Edge(n, n.getPrevious()));
            }
        }

        return output;
    }

    private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        Integer lowestDistance = Integer.MAX_VALUE;
        Integer tempDistance = 0;

        for (Node n : unsettledNodes) {
            tempDistance = n.getDistance();
            if (tempDistance < lowestDistance) {
                lowestDistance = tempDistance;
                lowestDistanceNode = n;
            }
        }

        return lowestDistanceNode;
    }

    private Node findNode(ArrayList<Node> nodes, Node ns) {

        for (Node n : nodes) {
            if (n.equals(ns)) {
                return n;
            }
        }

        return null;
    }

    private Node findNode(Set<Node> nodes, Node ns) {

        for (Node n : nodes) {
            if (n.equals(ns)) {
                return n;
            }
        }

        return null;
    }

    private Integer distanceToSelected(Node end, Node n) {
        double distance = Math.sqrt(Math.pow(end.getPos()[0] - n.getPos()[0], 2) + Math.pow(end.getPos()[1] - n.getPos()[1], 2));
        Integer output = (int) distance;
        return output;
    }

    private void getShortestPathRecursion(Node n) {
        if (n.getPrevious() != null) {
            shortestPathEdges.add(new Edge(n, n.getPrevious()));
            distance += n.getDistance();
            getShortestPathRecursion(n.getPrevious());
        }
    }

    public void resetAlgorithms() {
        distance = 0;
        duration = 0;
        triedShortestPathEdges.clear();
        shortestPathNodes.clear();
        shortestPathEdges.clear();
    }

}
