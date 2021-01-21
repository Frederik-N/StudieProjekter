import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;

public class ClosestPoints3DImproved {
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
            return ClosestPoints3DImproved.bruteForce(pointsSortedByX);
        }

        int minInSides = (int) Math.ceil(n / 12.0);
        int mustContain = (int) Math.ceil(3*36*Math.pow(n,1-1/3));
        double largestSoFar = 0;
        Cutplane3D cut = new Cutplane3D(axis.X, pointsSortedByX[n/2].x);
        for (int i = minInSides; i<n-minInSides-mustContain; i++ ){
            double size = -pointsSortedByX[i].x+pointsSortedByX[i+mustContain].x;
            if(size > largestSoFar){
                largestSoFar = size;
                cut = new Cutplane3D(axis.X, pointsSortedByX[i].x + size/2);
            }
        }
        for (int i = minInSides; i<n-minInSides-mustContain; i++ ){
            double size = -pointsSortedByY[i].y+pointsSortedByY[i+mustContain].y;
            if(size > largestSoFar){
                largestSoFar = size;
                cut = new Cutplane3D(axis.Y, pointsSortedByY[i].y + size/2);
            }
        }
        for (int i = minInSides; i<n-minInSides-mustContain; i++ ){
            double size = -pointsSortedByZ[i].z+pointsSortedByZ[i+mustContain].z;
            if(size > largestSoFar){
                largestSoFar = size;
                cut = new Cutplane3D(axis.Z, pointsSortedByZ[i].z + size/2);
            }
        }
        ArrayList<Point3D> pointsLeftOfCenterX = new ArrayList<>(), pointsRightOfCenterX = new ArrayList<>();
        ArrayList<Point3D> pointsLeftOfCenterY = new ArrayList<>(), pointsRightOfCenterY = new ArrayList<>();
        ArrayList<Point3D> pointsLeftOfCenterZ = new ArrayList<>(), pointsRightOfCenterZ = new ArrayList<>();
        switch (cut.axis){
            case X: 
                for(int i = 0; i < n; i++){
                    if(cut.cutpos > pointsSortedByX[i].x)
                         pointsLeftOfCenterX.add(pointsSortedByX[i]);
                    else pointsRightOfCenterX.add(pointsSortedByX[i]);
                    if(cut.cutpos > pointsSortedByY[i].x)
                        pointsLeftOfCenterY.add(pointsSortedByY[i]);
                    else pointsRightOfCenterY.add(pointsSortedByY[i]);
                    if(cut.cutpos > pointsSortedByZ[i].x)
                        pointsLeftOfCenterZ.add(pointsSortedByZ[i]);
                    else pointsRightOfCenterZ.add(pointsSortedByZ[i]);
                } break;
                case Y:
                for(int i = 0; i < n; i++){
                    if(cut.cutpos > pointsSortedByX[i].y)
                        pointsLeftOfCenterX.add(pointsSortedByX[i]);
                    else pointsRightOfCenterX.add(pointsSortedByX[i]);
                    if(cut.cutpos > pointsSortedByY[i].y)
                        pointsLeftOfCenterY.add(pointsSortedByY[i]);
                    else pointsRightOfCenterY.add(pointsSortedByY[i]);
                    if(cut.cutpos > pointsSortedByZ[i].y)
                        pointsLeftOfCenterZ.add(pointsSortedByZ[i]);
                    else pointsRightOfCenterZ.add(pointsSortedByZ[i]);
                } break;
            case Z:
                for(int i = 0; i < n; i++){
                    if(cut.cutpos > pointsSortedByX[i].z)
                        pointsLeftOfCenterX.add(pointsSortedByX[i]);
                    else pointsRightOfCenterX.add(pointsSortedByX[i]);
                    if(cut.cutpos > pointsSortedByY[i].z)
                        pointsLeftOfCenterY.add(pointsSortedByY[i]);
                    else pointsRightOfCenterY.add(pointsSortedByY[i]);
                    if(cut.cutpos > pointsSortedByZ[i].z)
                        pointsLeftOfCenterZ.add(pointsSortedByZ[i]);
                    else pointsRightOfCenterZ.add(pointsSortedByZ[i]);
                } break;
        }

        int leftSize = pointsLeftOfCenterX.size();
        int rightSize = pointsRightOfCenterX.size();
        Point3D[] leftX = new Point3D[leftSize], leftY = new Point3D[leftSize], leftZ = new Point3D[leftSize];
        pointsLeftOfCenterX.toArray(leftX); pointsLeftOfCenterY.toArray(leftY); pointsLeftOfCenterZ.toArray(leftZ); 
        Point3D[] rightX = new Point3D[rightSize], rightY = new Point3D[rightSize], rightZ = new Point3D[rightSize];
        pointsRightOfCenterX.toArray(rightX); pointsRightOfCenterY.toArray(rightY); pointsRightOfCenterZ.toArray(rightZ);
        Pair left = bentleyShamosHelper(leftX, leftY, leftZ);
        Pair right = bentleyShamosHelper(rightX, rightY, rightZ);


        Pair closestPair = left;
        if (left.distance() > right.distance()) {
            closestPair = right;
        }

        int size = 0;
        Point3D[] slab = new Point3D[n];
        double distance = closestPair.distance();
        double middleLine = cut.cutpos;

        for (Point3D point : pointsSortedByX) {
            double p = 0;
            switch (cut.axis){
                case X: p = point.x; break;
                case Y: p = point.y; break;
                case Z: p = point.z; break; 
            }
            if (Math.abs(middleLine - p) < distance) {
                slab[size++] = point;
            }
        }

        Point3D[] trimmedSlab = Arrays.copyOfRange(slab, 0, size);


        Pair pairs[] = SHelper(trimmedSlab, cut.axis, closestPair.distance());

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
