package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ApplicationView {

    private ApplicationController applicationController;
    private Group root = new Group();
    private GridPane startview;
    private Graph graph;
    public Canvas canvas = new Canvas(800, 600);
    public final int NODESIZE = 5;
    public boolean SHOWDISTANCE = false;

    Button generateGraph = new Button("Generate Graph");
    Button findSPath = new Button("Find S-Path");
    Button showDistance = new Button("Show distance");
    TextField inputNodesAndEdges = new TextField();
    TextField outputTextField = new TextField();

    ComboBox<String> algorithms = new ComboBox<>();


    public ApplicationView(ApplicationController appController, Graph g) {
        this.applicationController = appController;
        this.graph = g;
        createAndConfigure();
    }

    private void createAndConfigure() {
        startview = new GridPane();
        startview.setPadding(new Insets(20, 20, 20, 20));

        Text title = new Text("Shortest Path");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        showDistance.setStyle("-fx-background-color: #FF0000; ");

        inputNodesAndEdges.setPrefHeight(20);
        inputNodesAndEdges.setPrefWidth(400);
        inputNodesAndEdges.setText(String.valueOf(applicationController.NUMBEROFNODES) + "," + String.valueOf(applicationController.NUMBEROFEDGES));

        GraphicsContext gc = canvas.getGraphicsContext2D();

        outputTextField.setEditable(false);

        startview.setVgap(10);
        startview.setHgap(10);

        //startview.setGridLinesVisible(true);

        startview.add(canvas, 0, 2);


        startview.add(title, 0, 0);
        startview.add(generateGraph, 1, 0);
        startview.add(findSPath, 2, 0);
        startview.add(algorithms, 2, 1);
        startview.add(showDistance, 1, 1);
        startview.add(inputNodesAndEdges, 0, 1);
        startview.add(outputTextField, 0, 3);

        algorithms.setItems(getAlgorithms());
        algorithms.getSelectionModel().selectFirst();


        root.getChildren().add(startview);
    }

    public void drawShortestPath() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


        for (Edge e : applicationController.algorithms.getTriedShortestPathEdges()) {
            drawTriedLines(e, gc);
        }

        for (Edge e : applicationController.algorithms.getShortestPathEdges()) {
            if (SHOWDISTANCE) {
                drawDistance(e, gc);
            }
            drawLinesShortestPath(e, gc);
        }

        for (Node n : applicationController.algorithms.getShortestPathNodes()) {
            drawNodeShortestpath(n, gc);
        }

    }


    public void drawNodes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Edge e : graph.getEdgesToShow()) {
            if (SHOWDISTANCE) {
                drawDistance(e, gc);
            }
            drawLines(e, gc);
        }

        // Draw the nodes
        for (Node c : graph.getNodes()) {
            drawNode(c, gc);
        }

    }

    public void flipColor() {
        if (SHOWDISTANCE) {
            showDistance.setStyle("-fx-background-color: #258B22; ");
        } else {
            showDistance.setStyle("-fx-background-color: #FF0000; ");
        }
    }


    private void drawDistance(Edge e, GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf((int) e.getDistance()), ((e.getFrom().getPos()[0] + e.getTo().getPos()[0]) / 2), ((e.getFrom().getPos()[1] + e.getTo().getPos()[1]) / 2));
    }

    private void drawLines(Edge e, GraphicsContext gc) {
        gc.setStroke(Color.GREY);
        gc.setLineWidth(0.1);
        gc.strokeLine(e.getFrom().getPos()[0] + NODESIZE / 2, e.getFrom().getPos()[1] + NODESIZE / 2, e.getTo().getPos()[0] + NODESIZE / 2, e.getTo().getPos()[1] + NODESIZE / 2);
    }

    private void drawLinesShortestPath(Edge e, GraphicsContext gc) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(0.8);
        gc.strokeLine(e.getFrom().getPos()[0] + NODESIZE / 2, e.getFrom().getPos()[1] + NODESIZE / 2, e.getTo().getPos()[0] + NODESIZE / 2, e.getTo().getPos()[1] + NODESIZE / 2);
    }

    private void drawTriedLines(Edge e, GraphicsContext gc) {
        gc.setStroke(Color.GREY);
        gc.setLineWidth(0.1);
        gc.strokeLine(e.getFrom().getPos()[0] + NODESIZE / 2, e.getFrom().getPos()[1] + NODESIZE / 2, e.getTo().getPos()[0] + NODESIZE / 2, e.getTo().getPos()[1] + NODESIZE / 2);
    }


    private void drawNode(Node n, GraphicsContext gc) {
        gc.setFill(Color.GREY);

        // Check if node is selected
        if (n.getSelected()) {
            gc.setFill(Color.YELLOW);
        } else if (n.isHoverOver()) {
            gc.setFill(Color.PAPAYAWHIP);
        } else {
            gc.setFill(Color.GREY);
        }

        gc.fillOval(n.getPos()[0], n.getPos()[1], NODESIZE, NODESIZE);
    }

    private void drawNodeId(Node n, GraphicsContext gc) {
        gc.setFill(Color.GREY);
        gc.fillText(String.valueOf(n.getId()), n.getPos()[0], n.getPos()[1] - 6);
    }

    private void drawNodeShortestpath(Node n, GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);


        // Check if node is selected
        if (n.getSelected()) {
            gc.fillText(String.valueOf(n.getId()), n.getPos()[0], n.getPos()[1] - 6);
            gc.setFill(Color.GREEN);
        } else {
            gc.setFill(Color.GREY);
        }

        gc.fillOval(n.getPos()[0], n.getPos()[1], NODESIZE, NODESIZE);

    }

    private ObservableList<String> getAlgorithms() {
        TypeAlgorithms[] typeAlgorithms = TypeAlgorithms.values();
        ArrayList<String> algoNames = new ArrayList<>();
        String s = "";

        for (TypeAlgorithms t : typeAlgorithms) {
            s = t.toString();
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();

            algoNames.add(s);
        }

        ObservableList<String> output = FXCollections.observableArrayList(algoNames);

        return output;
    }

    public int countSelected() {
        int selected = 0;
        for (Node n : graph.getNodes()) {
            if (n.getSelected()) {
                selected++;
            }
        }
        return selected;
    }

    public Parent asParent() {
        return root;
    }
}
