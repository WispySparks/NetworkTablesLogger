package ntannotate;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Logger {

    public static final String tableName = "Logging";
    // private Set<Field>
    
    public static void setupEntries() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable(tableName);
        
    }

}
