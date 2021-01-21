import java.util.ArrayList;
import java.util.Arrays;

public class ClosestPoints3DSemiImproved {
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
            return ClosestPoints3DSemiImproved.bruteForce(pointsSortedByX);
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
        Pair pairs[] = S(slabY, slabZ, closestPair.distance(), axis.X);

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

    public static Pair[] SHelper(Point3D[] points, axis ax, double delta) {
        Point3D[] pointsSortedByX = points.clone();
        Point3D[] pointsSortedByY = points.clone();
        Point3D[] pointsSortedByZ = points.clone();
        Arrays.sort(pointsSortedByX, (p1, p2) -> Double.compare(p1.x, p2.x));
        Arrays.sort(pointsSortedByY, (p1, p2) -> Double.compare(p1.y, p2.y));
        Arrays.sort(pointsSortedByZ, (p1, p2) -> Double.compare(p1.z, p2.z));
        switch (ax){
            case X: return S(pointsSortedByY, pointsSortedByZ, delta, ax);
            case Y: return S(pointsSortedByX, pointsSortedByZ, delta, ax);
            case Z: return S(pointsSortedByX, pointsSortedByY, delta, ax);
            default: return null; //This case shouldn't happen, just returning null to satisfy compiler
        }
    }

    public static Pair[] S(Point3D[] pointsSortedByA, Point3D[] pointsSortedByB, double delta, axis ax) {
        int n = pointsSortedByA.length;
        if (n <= 1) {
            CompCounter.addComp(n-1);
            return new Pair[0];
        }

        int minInSides = (int) Math.ceil(n / 8.0);
        int atMostContain = (int) Math.floor(2*36*Math.pow(n,1-1/2));
        Cutplane3D cut = new Cutplane3D(axis.X,0);

        switch (ax){
            case X:
                for(int i = minInSides; i < n-minInSides+1; i++){
                    double sizey = pointsSortedByA[Math.min(i+atMostContain, n-minInSides-1)].y-pointsSortedByA[i].y;
                    double sizez = pointsSortedByB[Math.min(i+atMostContain, n-minInSides-1)].z-pointsSortedByB[i].z;
                    if(sizey > 2*delta || (atMostContain>n-n/4)){
                        cut = new Cutplane3D(axis.Y, (pointsSortedByA[i].y+sizey/2));
                        break;
                    }else if (sizez > 2*delta){
                        cut = new Cutplane3D(axis.Z, (pointsSortedByB[i].z+sizez/2));
                        break;
                    }
                } break;
            case Y:
                for(int i = minInSides; i < n-minInSides; i++){
                    double sizex = pointsSortedByA[Math.min(i+atMostContain, n-minInSides-1)].x-pointsSortedByA[i].x;
                    double sizez = pointsSortedByB[Math.min(i+atMostContain, n-minInSides-1)].z-pointsSortedByB[i].z;
                    if(sizex < 2*delta || atMostContain > n-n/4){
                        cut = new Cutplane3D(axis.X, (pointsSortedByA[i].x+sizex/2));
                        break;
                    }else if (sizez < 2*delta){
                        cut = new Cutplane3D(axis.Z, (pointsSortedByB[i].z+sizez/2));
                        break;
                    }
                } break;
            case Z:
                for(int i = minInSides; i < n-minInSides; i++){
                    double sizex = pointsSortedByA[Math.min(i+atMostContain, n-minInSides-1)].x-pointsSortedByA[i].x;
                    double sizey = pointsSortedByB[Math.min(i+atMostContain, n-minInSides-1)].y-pointsSortedByB[i].y;
                    if(sizey > 2*delta || atMostContain > n-n/4){
                        cut = new Cutplane3D(axis.Y, (pointsSortedByA[i].y+sizey/2));
                        break;
                    }else if (sizex > 2*delta || atMostContain > n-n/4){
                        cut = new Cutplane3D(axis.X, (pointsSortedByA[i].x+sizex/2));
                        break;
                    }
                } break;
        }

        ArrayList<Point3D> pointsLeftA = new ArrayList<>(), pointsRightA = new ArrayList<>(), pointsLeftB = new ArrayList<>(), pointsRightB = new ArrayList<>();
        switch (ax){
            case X:
                switch (cut.axis){
                    case X: //Impossible case, since this is cutplane from 3D
                    case Y:
                    for(int i = 0; i < n; i++){
                        if(pointsSortedByA[i].y<cut.cutpos)
                             pointsLeftA.add(pointsSortedByA[i]);
                        else pointsRightA.add(pointsSortedByA[i]);
                        if(pointsSortedByB[i].y<cut.cutpos)
                             pointsLeftB.add(pointsSortedByB[i]);
                        else pointsRightB.add(pointsSortedByB[i]);
                    }break;
                    case Z:
                    for(int i = 0; i < n; i++){
                        if(pointsSortedByA[i].z<cut.cutpos)
                             pointsLeftA.add(pointsSortedByA[i]);
                        else pointsRightA.add(pointsSortedByA[i]);
                        if(pointsSortedByB[i].z<cut.cutpos)
                             pointsLeftB.add(pointsSortedByB[i]);
                        else pointsRightB.add(pointsSortedByB[i]);
                    }break;
                }   break;
            case Y:
            switch (cut.axis){
                case X:
                    for(int i = 0; i < n; i++){
                        if(pointsSortedByA[i].x<cut.cutpos)
                            pointsLeftA.add(pointsSortedByA[i]);
                        else pointsRightA.add(pointsSortedByA[i]);
                        if(pointsSortedByB[i].x<cut.cutpos)
                            pointsLeftB.add(pointsSortedByB[i]);
                        else pointsRightB.add(pointsSortedByB[i]);
                    }break;
                case Y:break;

                case Z:
                    for(int i = 0; i < n; i++){
                        if(pointsSortedByA[i].z<cut.cutpos)
                            pointsLeftA.add(pointsSortedByA[i]);
                        else pointsRightA.add(pointsSortedByA[i]);
                        if(pointsSortedByB[i].z<cut.cutpos)
                            pointsLeftB.add(pointsSortedByB[i]);
                        else pointsRightB.add(pointsSortedByB[i]);
                    }break;
            }break;
            case Z:
            switch (cut.axis){
                case X:
                    for(int i = 0; i < n; i++){
                        if(pointsSortedByA[i].x<cut.cutpos)
                            pointsLeftA.add(pointsSortedByA[i]);
                        else pointsRightA.add(pointsSortedByA[i]);
                        if(pointsSortedByB[i].x<cut.cutpos)
                            pointsLeftB.add(pointsSortedByB[i]);
                        else pointsRightB.add(pointsSortedByB[i]);
                    }break;
                case Y:
                    for(int i = 0; i < n; i++){
                        if(pointsSortedByA[i].y<cut.cutpos)
                            pointsLeftA.add(pointsSortedByA[i]);
                        else pointsRightA.add(pointsSortedByA[i]);
                        if(pointsSortedByB[i].y<cut.cutpos)
                            pointsLeftB.add(pointsSortedByB[i]);
                        else pointsRightB.add(pointsSortedByB[i]);
                    }break;
                case Z: break;
            }break;
        }
        int leftSize = pointsLeftA.size(), rightSize = pointsRightA.size();
        Point3D[] leftA = new Point3D[leftSize], leftB = new Point3D[leftSize], rightA = new Point3D[rightSize], rightB = new Point3D[rightSize];
        pointsLeftA.toArray(leftA); pointsLeftB.toArray(leftB); pointsRightA.toArray(rightA); pointsRightB.toArray(rightB);


        Pair neighborLeft[] = S(leftA, leftB, delta, ax);
        Pair neighborRight[] = S(rightA, rightB, delta, ax);

        ArrayList<Point3D> slab = new ArrayList<>();
        double middleLine = cut.cutpos;

        for (Point3D point : pointsSortedByA) {
            double p = 0;
            switch (cut.axis){
                case X: p = point.x; break;
                case Y: p = point.y; break;
                case Z: p = point.z; break;
            }
            if (Math.abs(middleLine - p) < delta) {
                slab.add(point);
            }
        }

        ArrayList<Pair> result = new ArrayList<>();
        result.addAll(Arrays.asList(neighborLeft));
        result.addAll(Arrays.asList(neighborRight));

        for (int i = 0; i < slab.size() - 1; i++) {
            for (int j = i + 1; j < slab.size(); j++) {
                Pair newPair = new Pair(slab.get(i), slab.get(j));
                double a = 0;
                switch (cut.axis){
                    case X: a = newPair.a.x - newPair.b.x; break;
                    case Y: a = newPair.a.y - newPair.b.y; break;
                    case Z: a = newPair.a.z - newPair.b.z; break;
                }
                if (Math.abs(a) >= delta) break;
                CompCounter.addComp();
                if (newPair.distance() <= delta) {
                    result.add(newPair);
                }
            }
        }

        return result.toArray(new Pair[result.size()]);
    }
}
