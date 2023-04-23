package ntannotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
/**
 * Will output this field or get method to network tables using the given name or if none is provided the field/method's name.
 */
public @interface LogNT {
    /** Name in Network Tables */
    String value() default NTLogger.nullVal;
}
