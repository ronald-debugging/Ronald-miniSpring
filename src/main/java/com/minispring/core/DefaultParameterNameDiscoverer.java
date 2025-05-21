package com.minispring.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Default parameter name discoverer
 * Implements parameter name discovery based on Java 8 reflection API
 */
public class DefaultParameterNameDiscoverer implements ParameterNameDiscoverer {
    
    @Override
    public String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isNamePresent()) {
                parameterNames[i] = parameter.getName();
            } else {
                parameterNames[i] = "arg" + i;
            }
        }
        
        return parameterNames;
    }
    
    @Override
    public String[] getParameterNames(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();
        String[] parameterNames = new String[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isNamePresent()) {
                parameterNames[i] = parameter.getName();
            } else {
                parameterNames[i] = "arg" + i;
            }
        }
        
        return parameterNames;
    }
} 