package ntannotate;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Test test = new Test(20);
        NTLogger.initialize(Main.class);
        NTLogger.addInstance(test);
        NTLogger.log();
        System.out.println(NTLogger.dump());
        // grab a root class and then recurse over all fields in that class and all fields in those objects found and 
        // then you can find all fields with annotations and display those
        /*
        for (Field field : values.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(LogNT.class)) {
                if (!field.canAccess(values)) field.setAccessible(true);
                LogNT annotation = field.getAnnotation(LogNT.class);
                Class<?> c = field.getType();
                Object val = field.get(values);
                System.out.println("Field '" + field.getName() + "' with type '" + c.getTypeName() + 
                "' has NetworkTables name of '" + annotation.value() + "' and a value of '" + val + "'.");
            }
        }
        */
    }

}
