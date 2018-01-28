/**
 * Kodo Kojo - Software factory done right
 * Copyright © 2018 Kodo Kojo (infos@kodokojo.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 */
package io.kodokojo.property.configurer.config.properties;


import io.kodokojo.property.configurer.config.properties.provider.PropertyValueProvider;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;


public class PropertyResolver {

    public static final String VALID_METHOD_NAME = "valid";

    private final PropertyValueProvider propertyValueProvider;

    private final InternalInvoker internalInvoker;

    public PropertyResolver(PropertyValueProvider propertyValueProvider) {
        if (propertyValueProvider == null) {
            throw new IllegalArgumentException("propertyValueProvider must be defined.");
        }
        this.propertyValueProvider = propertyValueProvider;
        this.internalInvoker = new InternalInvoker();
    }

    @SuppressWarnings("unchecked")
    public <T extends PropertyConfig> T createProxy(Class<T> propertyConfig) {
        return (T) Proxy.newProxyInstance(PropertyResolver.class.getClassLoader(), new Class[]{propertyConfig}, internalInvoker);
    }

    private class InternalInvoker implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> returnType = method.getReturnType();
            if (VALID_METHOD_NAME.equals(method.getName())) {
                //  Java 9 rules :)
                //  Fix found here :
                //  https://github.com/brharrington/spectator/commit/b43f0013ebbfdb5c142210b54afbfb2e31cc07b5

                //  Invoke the default method implementation with java 9 reflect
                final Class<?> declaringClass = method.getDeclaringClass();
                MethodType rt = MethodType.methodType(returnType);
                return MethodHandles.lookup()
                        .findSpecial(declaringClass, method.getName(), rt, declaringClass)
                        .bindTo(proxy)
                        .invokeWithArguments(args);
            } else {
                Key keyAnnotation = method.getAnnotation(Key.class);
                if (keyAnnotation == null) {
                    return method.invoke(proxy, args);
                }
                Object value = propertyValueProvider.providePropertyValue(returnType, keyAnnotation.value());
                if (value == null) {
                    try {
                        value = getDefaultValue(returnType, keyAnnotation.defaultValue());
                    } catch (NumberFormatException e) {
                        if (Number.class.isAssignableFrom(returnType)) {
                            throw new IllegalStateException("Invalid value for key '" + keyAnnotation.value() + "' : '" + keyAnnotation.defaultValue() + "'", e);
                        } else {
                            throw e;
                        }
                    }
                }
                if (value == null) {
                    throw new IllegalStateException("Unable to provide property value for key '" + keyAnnotation.value() + "'.");
                }
                return value;
            }
        }
    }

    private static Object getDefaultValue(Class<?> expectedType, Object value) {
        if (String.class.isAssignableFrom(expectedType)) {
            return value.toString();
        } else if (Integer.class.isAssignableFrom(expectedType) || int.class.isAssignableFrom(expectedType)) {
            return Integer.parseInt(value.toString());
        } else if (Long.class.isAssignableFrom(expectedType) || long.class.isAssignableFrom(expectedType)) {
            return Long.parseLong(value.toString());
        } else if (BigDecimal.class.isAssignableFrom(expectedType)) {
            return new BigDecimal(value.toString());
        } else if (Double.class.isAssignableFrom(expectedType) || double.class.isAssignableFrom(expectedType)) {
            return Double.valueOf(value.toString());
        } else if (Boolean.class.isAssignableFrom(expectedType) || boolean.class.isAssignableFrom(expectedType)) {
            return Boolean.parseBoolean(value.toString());
        }
        return value;
    }
}

