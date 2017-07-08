package me.costa.gustavo.saad4jee.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

import me.costa.gustavo.saad4jee.enums.Comandos;


@InterceptorBinding 
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.TYPE, ElementType.METHOD})

public @interface RobotDetect{
	    @Nonbinding Comandos[] comandos() default {Comandos.ImprimirConsole};
}
