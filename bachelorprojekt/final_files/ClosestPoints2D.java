import java.util.Arrays;

public class ClosestPoints2D {
    public static class Pair {
        public Point2D a, b;

        public Pair(Point2D a, Point2D b) {
            this.a = a;
            this.b = b;
        }

        public double distance() {
            return Point2D.distance(a, b);
        }
        public String toString() {
            return "[" + a.toString() + ", " + b.toString() + "] -> " + distance();
        }
    }

    public static Pair bruteForce(Point2D[] points) {
        int n = points.length;
        if (n < 2) {
            return null;
        }

        Pair closestPair = new Pair(points[0], points[1]);
        if (n == 2) {
            return closestPair;
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                Pair newPair = new Pair(points[i], points[j]);
                if (newPair.distance() < closestPair.distance()) {
                    closestPair = newPair;
                }
            }
        }

        return closestPair;
    }

    public static Pair bentleyShamos(Point2D[] pointsSortedByX, Point2D[] pointsSortedByY) {
        int n = pointsSortedByX.length;
        if (n <= 3) {
            CompCounter.addComp(n-1);
            return ClosestPoints2D.bruteForce(pointsSortedByX);
        }

        int mid = n/2;

        Point2D[] pointsLeftOfCenter = Arrays.copyOfRange(pointsSortedByX, 0, mid);
        Point2D[] pointsRightOfCenter = Arrays.copyOfRange(pointsSortedByX, mid, n);

        for(int i = 0; i < mid; i++){
            pointsSortedByX[i].isLeft = true;
        }
        for(int i = mid; i < n; i++){
            pointsSortedByX[i].isLeft = false;
        }
        int countLeft = 0, countRight = 0;

        Point2D[] pointsLeftOfCenterSortedByY = new Point2D[mid];
        Point2D[] pointsRightOfCenterSortedByY = new Point2D[n-mid];

        for(int i = 0; i < n; i++){
            if(pointsSortedByY[i].isLeft)
                pointsLeftOfCenterSortedByY[countLeft++] = pointsSortedByY[i];
            else
                pointsRightOfCenterSortedByY[countRight++] = pointsSortedByY[i];
        }

        Pair closestPairLeft = bentleyShamos(pointsLeftOfCenter, pointsLeftOfCenterSortedByY);
        Pair closestPairRight = bentleyShamos(pointsRightOfCenter, pointsRightOfCenterSortedByY);

        Pair closestPair = closestPairLeft;
        if (closestPairLeft.distance() > closestPairRight.distance()) {
            closestPair = closestPairRight;
        }

        int size = 0;
        Point2D[] slab = new Point2D[n];
        double distance = closestPair.distance();
        double middleLine = pointsRightOfCenter[0].x;

        for (Point2D point : pointsSortedByY) {
            if (abs(middleLine - point.x) < distance) {
                slab[size++] = point;
            }
        }
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                Pair newPair = new Pair(slab[i], slab[j]);
                
                if (abs(newPair.a.y - newPair.b.y) >= distance){ break;}
                CompCounter.addComp();
                if (newPair.distance() < closestPair.distance()) {
                    distance = newPair.distance();
                    closestPair = newPair;
                }
            }
        }

        return closestPair;
    }

    public static Pair bentleyShamos(Point2D[] points) {
        Point2D[] pointsSortedByX = points.clone();
        Point2D[] pointsSortedByY = points.clone();
        sortByX(pointsSortedByX);
        sortByY(pointsSortedByY);
        return bentleyShamos(pointsSortedByX, pointsSortedByY);
    }

    // The functions below are abstracted away to hide usage of the standard Java library.

    private static double abs(double value) {
        return Math.abs(value);
    }

    private static void sortByX(Point2D[] points) {
        Arrays.sort(points, (p1, p2) -> Double.compare(p1.x, p2.x));
    }

    private static void sortByY(Point2D[] points) {
        Arrays.sort(points, (p1, p2) -> Double.compare(p1.y, p2.y));
    }
}
