package at.minify.skymini.api.annotations;

import at.minify.skymini.core.data.Priority;
import at.minify.skymini.core.data.Server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MiniRegistry {

    Priority priority() default Priority.DEFAULT;
    Server server() default Server.SKYBLOCK;

}
