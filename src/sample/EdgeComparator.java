package sample;

import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {

    @Override
    public int compare(Edge eFrom, Edge eTo) {
        if (eFrom.getDistance() > eTo.getDistance()) {
            return 1;
        } else {
            return -1;
        }
    }
}


