package sample;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node node, Node t1) {
        if (node.getDistance() > t1.getDistance()) {
            return 1;
        } else {
            return -1;
        }
    }
}
