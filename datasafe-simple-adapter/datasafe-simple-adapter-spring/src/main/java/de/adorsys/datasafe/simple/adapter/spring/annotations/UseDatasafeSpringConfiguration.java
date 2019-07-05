package de.adorsys.datasafe.simple.adapter.spring.annotations;

import de.adorsys.datasafe.simple.adapter.spring.DatasafeSpringConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({DatasafeSpringConfiguration.class})
public @interface UseDatasafeSpringConfiguration {
}


