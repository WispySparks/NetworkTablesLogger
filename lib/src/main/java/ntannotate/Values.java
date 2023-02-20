package ntannotate;

public class Values {

    @NetworkTables(Name = "Length")
    private double length;

    Values(int length) {
        this.length = length;
    }
    
    public double getLength() {
        return length;
    }

}
