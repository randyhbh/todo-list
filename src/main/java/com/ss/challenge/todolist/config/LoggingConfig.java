package com.ss.challenge.todolist.config;

import com.ss.challenge.todolist.api.http.ExceptionHandlerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;

@Configuration
public class LoggingConfig {

    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint) {
        MethodParameter methodParameter = injectionPoint.getMethodParameter();
        Field field = injectionPoint.getField();

        if (methodParameter != null) {
            var targetClass = methodParameter.getContainingClass();
            return LoggerFactory.getLogger(targetClass);
        }

        if (field != null) {
            var targetClass = field.getDeclaringClass();
            return LoggerFactory.getLogger(targetClass);
        }

        throw new RuntimeException("Could not produce all the required Loggers");
    }
}
