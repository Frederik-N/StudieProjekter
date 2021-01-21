public class Point3D {
    public final double x, y, z;
    public boolean isLeft = false, inSlab = false;


    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static double distance(Point3D a, Point3D b) {
        double x = a.x - b.x;
        double y = a.y - b.y;
        double z = a.z - b.z;
        return Math.sqrt(x*x + y*y + z*z);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
