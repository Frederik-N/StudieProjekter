public class Point2D {
    public final double x, y;
    public boolean isLeft = false;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double distance(Point2D a, Point2D b) {
        double x = a.x - b.x;
        double y = a.y - b.y;
        return Math.sqrt(x*x + y*y);
    }

    public String toString(){
        return "(" + x + "," + y + ")"; 
    }
}
