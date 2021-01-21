import java.util.ArrayList;
import java.util.Arrays;

public class ClosestPoints3D {
    public static class Pair {
        public Point3D a, b;

        public Pair(Point3D a, Point3D b) {
            this.a = a;
            this.b = b;
        }

        public double distance() {
            return Point3D.distance(a, b);
        }

        public String toString() {
            return "[" + a.toString() + ", " + b.toString() + "] -> " + distance();
        }
    }

    public static Pair bruteForce(Point3D[] points) {
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

    public static Pair bentleyShamosHelper(Point3D[] pointsSortedByX, Point3D[] pointsSortedByY, Point3D[] pointsSortedByZ) {
        int n = pointsSortedByX.length;
        if (n <= 3) {
            CompCounter.addComp(n-1);
            return ClosestPoints3D.bruteForce(pointsSortedByX);
        }
        //split the point set for X
        int mid = n/2;

        Point3D[] pointsLeftOfCenterX = Arrays.copyOfRange(pointsSortedByX, 0, mid);
        Point3D[] pointsRightOfCenterX = Arrays.copyOfRange(pointsSortedByX, mid, n);

        //Split the point set for Y and Z and keep it sorted
        for(int i = 0; i < n; i++){
            pointsSortedByX[i].isLeft = i < mid;
        }

        int leftCountY=0, leftCountZ=0, rightCountY=0, rightCountZ=0;

        Point3D[] pointsLeftOfCenterY = new Point3D[mid],   pointsLeftOfCenterZ  = new Point3D[mid],
                 pointsRightOfCenterY = new Point3D[n-mid], pointsRightOfCenterZ = new Point3D[n-mid];

        for(int i = 0; i < n; i++){
            if (pointsSortedByY[i].isLeft)   pointsLeftOfCenterY[leftCountY++] = pointsSortedByY[i];
            else                           pointsRightOfCenterY[rightCountY++] = pointsSortedByY[i];
            if (pointsSortedByZ[i].isLeft)   pointsLeftOfCenterZ[leftCountZ++] = pointsSortedByZ[i];
            else                           pointsRightOfCenterZ[rightCountZ++] = pointsSortedByZ[i];
        }

        //Recursive call to archieve divide and conquer
        Pair left = bentleyShamosHelper(pointsLeftOfCenterX, pointsLeftOfCenterY, pointsLeftOfCenterZ);
        Pair right = bentleyShamosHelper(pointsRightOfCenterX, pointsRightOfCenterY, pointsRightOfCenterZ);

        //Find best solution so far
        Pair closestPair = left;
        if (left.distance() > right.distance()) {
            closestPair = right;
        }

        //Solve for the slab
        int size = 0;
        Point3D[] slab = new Point3D[n];
        double distance = closestPair.distance();
        double middleLine = pointsRightOfCenterX[0].x;
        //Find the slab points
        for (Point3D point : pointsSortedByY) {
            if (Math.abs(middleLine - point.x) < distance) {
                slab[size++] = point;
            }
        }
        //Get the sorted slab for y and z
        Point3D[] slabY = Arrays.copyOfRange(slab, 0, size);
        for(int i = 0; i < n; i++) pointsSortedByY[i].inSlab = false;
        for(int i = 0; i < size; i++) slabY[i].inSlab = true;
        Point3D[] slabZ = Arrays.stream(pointsSortedByZ).filter(p -> p.inSlab).toArray(Point3D[]::new);
        //Run the fixed radius neighbors algorithm
        Pair pairs[] = S(slabZ, slabY, closestPair.distance());

        Pair lowest = closestPair;
        for (Pair pair : pairs) {
            if (pair.distance() < lowest.distance()) {
                lowest = pair;
            }
        }

        return lowest;
    }

    public static Pair bentleyShamos(Point3D[] points) {
        Point3D[] pointsSortedByX = points.clone();
        Point3D[] pointsSortedByY = points.clone();
        Point3D[] pointsSortedByZ = points.clone();
        Arrays.sort(pointsSortedByX, (p1, p2) -> Double.compare(p1.x, p2.x));
        Arrays.sort(pointsSortedByY, (p1, p2) -> Double.compare(p1.y, p2.y));
        Arrays.sort(pointsSortedByZ, (p1, p2) -> Double.compare(p1.z, p2.z));
        return bentleyShamosHelper(pointsSortedByX, pointsSortedByY, pointsSortedByZ);
    }

    public static Pair[] S(Point3D[] pointsSortedByZ, Point3D[] pointsSortedByY, double delta) {
        int n = pointsSortedByZ.length;
        if (n <= 1) {
            CompCounter.addComp(n-1);
            return new Pair[0];
        }

        int mid = n/2;

        Point3D[] pointsLeftOfCenter = Arrays.copyOfRange(pointsSortedByZ, 0, mid);
        Point3D[] pointsRightOfCenter = Arrays.copyOfRange(pointsSortedByZ, mid, n);

        for(int i = 0; i < mid; i++){
            pointsSortedByZ[i].isLeft = true;
        }
        for(int i = mid; i < n; i++){
            pointsSortedByZ[i].isLeft = false;
        }
        int countLeft = 0, countRight = 0;

        Point3D[] pointsLeftOfCenterSortedByY = new Point3D[mid];
        Point3D[] pointsRightOfCenterSortedByY = new Point3D[n-mid];

        for(int i = 0; i < n; i++){
            if(pointsSortedByY[i].isLeft)
                pointsLeftOfCenterSortedByY[countLeft++] = pointsSortedByY[i];
            else
                pointsRightOfCenterSortedByY[countRight++] = pointsSortedByY[i];
        }

        Pair neighborLeft[] = S(pointsLeftOfCenter, pointsLeftOfCenterSortedByY, delta);
        Pair neighborRight[] = S(pointsRightOfCenter, pointsRightOfCenterSortedByY, delta);


        ArrayList<Point3D> slab = new ArrayList<>();
        double middleLine = pointsRightOfCenter[0].x;

        for (Point3D point : pointsSortedByY) {
            if (Math.abs(middleLine - point.x) < delta) {
                slab.add(point);
            }
        }

        ArrayList<Pair> result = new ArrayList<>();
        result.addAll(Arrays.asList(neighborLeft));
        result.addAll(Arrays.asList(neighborRight));

        for (int i = 0; i < slab.size() - 1; i++) {
            for (int j = i + 1; j < slab.size(); j++) {
                Pair newPair = new Pair(slab.get(i), slab.get(j));
                if (Math.abs(newPair.a.y - newPair.b.y) >= delta) break;
                CompCounter.addComp();
                if (newPair.distance() <= delta) {
                    result.add(newPair);
                }
            }
        }

        return result.toArray(new Pair[result.size()]);
    }
}
