import java.util.ArrayList;
import java.util.Arrays;

public class ClosestPoints2DImproved {
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
            return ClosestPoints2DImproved.bruteForce(pointsSortedByX);
        }

        int minInSides = (int) Math.ceil(n / 8.0);
        int mustContain = (int) Math.ceil(2*12*Math.pow(n,1/2));
        double largestSoFar = 0;
        Cutplane2D cut = new Cutplane2D(axis.X, pointsSortedByX[n/2].x);


        for (int i = minInSides; i<n-minInSides-mustContain; i++ ){
            double size = -pointsSortedByX[i].x+pointsSortedByX[i+mustContain].x;
            if(size > largestSoFar){
                largestSoFar = size;
                cut = new Cutplane2D(axis.X, pointsSortedByX[i].x + size/2);
            }
        }
        for (int i = minInSides; i<n-minInSides-mustContain; i++ ){
            double size = -pointsSortedByY[i].y+pointsSortedByY[i+mustContain].y;
            if(size > largestSoFar){
                largestSoFar = size;
                cut = new Cutplane2D(axis.Y, pointsSortedByY[i].y + size/2);
            }
        }
        ArrayList<Point2D> pointsLeftOfCenterX = new ArrayList<>(), pointsRightOfCenterX = new ArrayList<>();
        ArrayList<Point2D> pointsLeftOfCenterY = new ArrayList<>(), pointsRightOfCenterY = new ArrayList<>();

        switch (cut.axis){
            case X: 
                for(int i = 0; i < n; i++){
                    if(cut.cutpos > pointsSortedByX[i].x)
                         pointsLeftOfCenterX.add(pointsSortedByX[i]);
                    else pointsRightOfCenterX.add(pointsSortedByX[i]);
                    if(cut.cutpos > pointsSortedByY[i].x)
                        pointsLeftOfCenterY.add(pointsSortedByY[i]);
                    else pointsRightOfCenterY.add(pointsSortedByY[i]);
                } break;
            case Y:
                for(int i = 0; i < n; i++){
                    if(cut.cutpos > pointsSortedByX[i].y)
                        pointsLeftOfCenterX.add(pointsSortedByX[i]);
                    else pointsRightOfCenterX.add(pointsSortedByX[i]);
                    if(cut.cutpos > pointsSortedByY[i].y)
                        pointsLeftOfCenterY.add(pointsSortedByY[i]);
                    else pointsRightOfCenterY.add(pointsSortedByY[i]);
                } break;
            default: break; //Not possible
            }

        int leftSize = pointsLeftOfCenterX.size();
        int rightSize = pointsRightOfCenterX.size();
        Point2D[] leftX = new Point2D[leftSize], leftY = new Point2D[leftSize];
        pointsLeftOfCenterX.toArray(leftX); pointsLeftOfCenterY.toArray(leftY);
        Point2D[] rightX = new Point2D[rightSize], rightY = new Point2D[rightSize];
        pointsRightOfCenterX.toArray(rightX); pointsRightOfCenterY.toArray(rightY);

        Pair closestPairLeft = bentleyShamos(leftX, leftY);
        Pair closestPairRight = bentleyShamos(rightX, rightY);

        Pair closestPair = closestPairLeft;
        if (closestPairLeft.distance() > closestPairRight.distance()) {
            closestPair = closestPairRight;
        }

        int size = 0;
        Point2D[] slab = new Point2D[n];
        double distance = closestPair.distance();

        for (Point2D point : pointsSortedByY) {
            boolean inSlab = false;
            switch(cut.axis){
                case X: inSlab = abs(cut.cutpos - point.x)< distance; break;
                case Y: inSlab = abs(cut.cutpos - point.y)< distance; break;
                default: break;
            }
            if (inSlab) {
                slab[size++] = point;
            }
        }

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                Pair newPair = new Pair(slab[i], slab[j]);
                boolean inDistance = false;
                switch(cut.axis){
                    case X: inDistance = abs(newPair.a.y - newPair.b.y) >= distance; break;
                    case Y: inDistance = abs(newPair.a.x - newPair.b.x) >= distance; break;
                    default: break;
                }
                if (inDistance) break;
                
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
