package at.minify.skymini.api.annotations;

import at.minify.skymini.core.data.Server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ServerWidget {

    Server server() default Server.SKYBLOCK;

}
