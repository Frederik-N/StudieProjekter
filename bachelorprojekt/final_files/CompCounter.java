public class CompCounter {
    private long compCounter;
    private static CompCounter c;
    public CompCounter(){
        compCounter = 0;
    }

    public static CompCounter getCC(){
        if (c == null) c = new CompCounter();
        return c;
    }

    public static long getCount(){if (c == null) c = new CompCounter();return c.compCounter;}

    public static void addComp(){if (c == null) c = new CompCounter();c.compCounter ++;}

    public static void addComp(int i){if (c == null) c = new CompCounter();c.compCounter += i;}

    public static void reset(){c = new CompCounter();}

}