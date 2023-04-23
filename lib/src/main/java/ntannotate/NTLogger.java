package ntannotate;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Used to log fields and methods(Can't have parameters 
 * and must return something) to network tables for viewing or editing(in the case of fields). <p>
 * <b> Static fields and methods will be automatically retrieved </b> but if 
 * you want instance fields or methods you must provide an instance to back the values.
 * Use {@link NTLogger#addInstance(Object obj)} to supply these to the Logger.
 * @author WispySparks
 * 
 */
public final class NTLogger {

    private static Class<LogNT> annotation = LogNT.class;
    private static Set<Field> staticFields;
    private static Set<Method> staticMethods;
    private static HashMap<Object, ReflectionData> instanceFieldsAndMethods = new HashMap<>();
    private static NetworkTable table = NetworkTableInstance.getDefault().getTable("NT Logging");
    private static boolean initialized = false;

    private NTLogger() {}

    /**
     * Initializes the logger using the specified class as the root for package purposes. 
     * @param clazz - Make this a class in the top level package of your project (ex: Main.class)
     */
    public static void initialize(Class<?> clazz) {
        Reflections reflections = new Reflections(
            new ConfigurationBuilder()
            .addUrls(ClasspathHelper.forClass(clazz))
            .addScanners(Scanners.FieldsAnnotated, Scanners.MethodsAnnotated)
        );
        staticFields = reflections.getFieldsAnnotatedWith(annotation)
            .stream()
            .filter(NTLogger::filterStaticField)
            .collect(Collectors.toSet());
        staticMethods = reflections.getMethodsAnnotatedWith(annotation)
            .stream()
            .filter(NTLogger::filterStaticMethod)
            .collect(Collectors.toSet());
        staticFields.forEach(NTLogger::setAccessible);
        staticMethods.forEach(NTLogger::setAccessible);
        initialized = true;
    }

    /**
     * Call this in robot periodic to log everything to network tables.
     */
    public static void log() {
        if (!initialized) throw new IllegalStateException("NTLogger was never initialized!");
        System.out.println(table.getPath());
        for (Field field : staticFields) {
            try {
                System.out.println(field.getType().getName() + " " + field.getName() + " " + field.get(null));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds an instance to the logger so that instance variables and methods can be retrieved and logged on said instance.
     * @param obj - Instance to get fields and methods from
     */
    public static void addInstance(Object obj) {
        if (!initialized) throw new IllegalStateException("NTLogger was never initialized!");
        Set<Field> fields = Set.of(obj.getClass().getDeclaredFields())
            .stream()
            .filter((f) -> f.isAnnotationPresent(annotation))
            .filter(NTLogger::filterInstanceField)
            .collect(Collectors.toSet());
        Set<Method> methods = Set.of(obj.getClass().getDeclaredMethods())
            .stream()
            .filter((m) -> m.isAnnotationPresent(annotation))
            .filter(NTLogger::filterInstanceMethod)
            .collect(Collectors.toSet());
        fields.forEach(NTLogger::setAccessible);
        methods.forEach(NTLogger::setAccessible);
        instanceFieldsAndMethods.put(obj, new ReflectionData(
            fields,
            methods
        ));
    }

    public static String dump() {
        if (!initialized) throw new IllegalStateException("NTLogger was never initialized!");
        return 
        "LOGGER\n" +
        "Static Fields:\n" +
        staticFields +
        "\nStatic Methods:\n" +
        staticMethods +
        "\nInstance Fields and Methods:\n" +
        instanceFieldsAndMethods;
    }
    
    private static boolean filterStaticField(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }

    private static boolean filterInstanceField(Field f) {
        return !Modifier.isStatic(f.getModifiers());
    }

    private static boolean filterStaticMethod(Method m) {
        return Modifier.isStatic(m.getModifiers()) && !m.getReturnType().equals(Void.class) && m.getParameterTypes().length == 0;
    }

    private static boolean filterInstanceMethod(Method m) {
        return !Modifier.isStatic(m.getModifiers()) && !m.getReturnType().equals(Void.class) && m.getParameterTypes().length == 0;
    }

    private static void setAccessible(AccessibleObject ao) {
        ao.setAccessible(true);
    }

    private record ReflectionData(Set<Field> fields, Set<Method> methods) {}

}
