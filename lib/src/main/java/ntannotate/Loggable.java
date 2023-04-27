package ntannotate;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableValue;

public interface Loggable {
    
    Map<String, NetworkTableValue> log();

}
