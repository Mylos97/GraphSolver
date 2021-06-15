package sample;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.List;

public class ApplicationController {

    ApplicationView applicationView;
    Graph graph;
    Algorithms algorithms;

    public int NUMBEROFNODES = 20;
    public int NUMBEROFEDGES = 3;
    public int[] STARTNODEEDGEARRAY = new int[]{NUMBEROFNODES, NUMBEROFEDGES};

    public ApplicationController(Graph g) {
        this.graph = g;
        this.algorithms = new Algorithms(graph);
    }


    public void setApplicationView(ApplicationView view) {
        this.applicationView = view;

        //Generate the graph with input from the textfield
        EventHandler<ActionEvent> generate = e -> {
            createNodes(view, getInputNumber(view.inputNodesAndEdges));
            algorithms.resetAlgorithms();
        };
        view.generateGraph.setOnAction(generate);

        //Show the distance of the edges
        EventHandler<ActionEvent> showDistance = e -> flipShowDistance(view);
        view.showDistance.setOnAction(showDistance);

        //Use the correct algorithm
        EventHandler<ActionEvent> useAlgorithm = e -> {
            selectAlgo(view);
        };
        view.findSPath.setOnAction(useAlgorithm);

        // Canvas events
        view.canvas.setOnMouseClicked(e -> mouseEventOnCanvas(e, view));

        // Init creating of graph
        createNodes(view, STARTNODEEDGEARRAY);
    }

    private void createNodes(ApplicationView view, int[] numberOfNodesEdges) {
        algorithms.clearShortestPathEdges();

        //Check if correct input else generate standard graph
        if (numberOfNodesEdges[0] != -1) {
            graph.generateGraph(numberOfNodesEdges[0], numberOfNodesEdges[1]);
        } else {
            graph.generateGraph(10, 1);
        }

        view.drawNodes();
    }

    private void flipShowDistance(ApplicationView view) {
        view.SHOWDISTANCE = !(view.SHOWDISTANCE);
        view.flipColor();

        if (algorithms.getDistance() != 0) {
            view.drawShortestPath();
        } else {
            view.drawNodes();
        }
    }

    private int[] getInputNumber(TextField t) {
        String inputString = t.getText();
        List<String> inputList = Arrays.asList(inputString.split(","));
        int[] outputInts = new int[2];

        int placementInt = 0;
        double tempDouble = 0;
        boolean foundString = false;


        if (inputList.size() == 2) {

            for (String s : inputList) {

                //Regex to check if string is a number
                if (s.matches("-?\\d+")) {
                    tempDouble = Integer.valueOf(s);
                    tempDouble = checkIfValidInt(tempDouble);

                    //If the number parsed the test input it to the array
                    if (tempDouble != -1) {
                        outputInts[placementInt] = (int) tempDouble;
                        placementInt++;
                    }


                } else {
                    foundString = true;
                    break;
                }

            }

            if (!foundString && correctArray(outputInts)) {
                NUMBEROFNODES = outputInts[0];
                NUMBEROFEDGES = outputInts[1];
                return outputInts;
            }

        }

        t.setText("Please input in the format of no_nodes, no_edges -> 10,1");
        return new int[]{-1, -1};
    }

    private double checkIfValidInt(double number) {
        // Check if number is int and greater than 0 and less than 501
        if (number >= 1 && number <= 500) {

            if (number == (int) number) {
                return (int) number;
            }

        }

        return -1;
    }

    private void selectAlgo(ApplicationView view) {
        //Lets check if the user selected the correct amount of nodes
        int selectedNodes = view.countSelected();

        if (view.algorithms.getValue().equals("Dijsktras") && selectedNodes == 2) {
            algorithms.Dijkstras();
            view.drawShortestPath();
            view.outputTextField.setText("Distance : " + algorithms.getDistance() + "\t" + " Computation time in milliseconds : " + String.valueOf(algorithms.getDuration()));
        } else if (view.algorithms.getValue().equals("Astar") && selectedNodes == 2) {
            algorithms.Astar();
            view.drawShortestPath();
            view.outputTextField.setText("Distance : " + algorithms.getDistance() + "\t" + " Computation time in milliseconds : " + String.valueOf(algorithms.getDuration()));
        } else if (view.algorithms.getValue().equals("Prims") && selectedNodes == 0) {
            algorithms.Prims();
            view.drawShortestPath();
            view.outputTextField.setText("Distance : " + algorithms.getDistance() + "\t" + " Computation time in milliseconds : " + String.valueOf(algorithms.getDuration()));
        } else {
            graph.generateGraph(NUMBEROFNODES, NUMBEROFEDGES);
            view.outputTextField.setText("Please select the correct number of nodes for the algorithm!");
            view.drawNodes();
        }

    }

    private boolean correctArray(int[] ints) {
        return ints[0] > ints[1];
    }

    private void mouseEventOnCanvas(MouseEvent me, ApplicationView view) {
        Node temp;
        int selected = 0;

        // Get number of selected nodes
        for (Node no : graph.getNodes()) {

            if (no.getSelected()) {
                selected++;
            }
        }


        for (Node n : graph.getNodes()) {
            if (clickOnNode(me, n)) {

                selected++;

                // To make sure only to nodes is selected
                if (selected > 2 && n.getSelected()) {
                    n.mouseClick();
                }

            }
        }


        view.drawNodes();
    }

    private void mouseMovementOnCanvas(MouseEvent me, ApplicationView view) {
        for (Node n : graph.getNodes()) {
            if (hoverOnNode(me, n)) {
                n.setHoverOver(true);
            } else {
                n.setHoverOver(false);
            }
        }
    }

    private boolean hoverOnNode(MouseEvent me, Node n) {
        double dx = me.getSceneX() - n.getPos()[0] - 22;
        double dy = me.getSceneY() - n.getPos()[1] - 120;

        // Lets help the user a bit with - 4
        double dist = Math.sqrt(dx * dx + dy * dy) - 4;

        if (dist < applicationView.NODESIZE) {
            return true;
        }

        return false;
    }

    private boolean clickOnNode(MouseEvent me, Node n) {
        // Subtract the placement of the canvas
        double dx = me.getSceneX() - n.getPos()[0] - 22;
        double dy = me.getSceneY() - n.getPos()[1] - 120;

        // Lets help the user a bit with - 4
        double dist = Math.sqrt(dx * dx + dy * dy) - 4;

        if (dist < applicationView.NODESIZE) {
            n.mouseClick();
            return true;
        }

        return false;
    }

}
