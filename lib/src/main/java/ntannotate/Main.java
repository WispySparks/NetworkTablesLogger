package ntannotate;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws Exception {
        Values values = new Values(20);
        // grab a root class and then recurse over all fields in that class and all fields in those objects found and 
        // then you can find all fields with annotations and display those
        for (Field field : values.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NetworkTables.class)) {
                if (!field.canAccess(values)) field.setAccessible(true);
                NetworkTables annotation = field.getAnnotation(NetworkTables.class);
                Class<?> c = field.getType();
                Object val = field.get(values);
                System.out.println("Field '" + field.getName() + "' with type '" + c.getTypeName() + 
                "' has NetworkTables name of '" + annotation.Name() + "' and a value of '" + val + "'.");
            }
        }
    }

}
