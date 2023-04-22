package ntannotate;

public class Test {

    @LogNT
    public static String jim = "jim";

    @LogNT
    private static String thing = "bob";

    @LogNT("Length")
    private double length;

    Test(int length) {
        this.length = length;
    }
    
    public double getLength() {
        return length;
    }

}
