package Models;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Result> {

    @Override
    public int compare(Result a, Result b) {
        return a.Distance < b.Distance ? -1 : a.Distance == b.Distance ? 0 : 1;
    }
}
