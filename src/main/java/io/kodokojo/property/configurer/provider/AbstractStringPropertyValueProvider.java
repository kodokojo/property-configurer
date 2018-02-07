/**
 * Kodo Kojo - Software factory done right
 * Copyright © 2018 Kodo Kojo (infos@kodokojo.io)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 */
package io.kodokojo.property.configurer.provider;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Utility class which allow to return a value from a String resolved property.
 */
public abstract class AbstractStringPropertyValueProvider implements PropertyValueProvider {

    protected abstract String provideValue(String key);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T providePropertyValue(Class<T> classType, String key) {
        if (classType == null) {
            throw new IllegalArgumentException("classType must be defined.");
        }
        if (isBlank(key)) {
            throw new IllegalArgumentException("key must be defined.");
        }
        String value = provideValue(key);
        if (value != null) {
            if (classType.equals(String.class)) {
                return (T) value;
            } else if (classType.equals(Integer.class) || int.class.isAssignableFrom(classType)) {
                return (T) Integer.valueOf(value);
            } else if (classType.equals(Long.class) || long.class.isAssignableFrom(classType)) {
                return (T) Long.valueOf(value);
            } else if (classType.equals(BigDecimal.class)) {
                return (T) new BigDecimal(value);
            } else if (classType.equals(Boolean.class) || boolean.class.isAssignableFrom(classType)) {
                return (T) Boolean.valueOf(value);
            } else {
                throw new IllegalArgumentException("Unable to convert Property '" + key + "' with value '" + value + "' to type '" + classType.getCanonicalName() + "'.");
            }
        }
        return null;
    }

}
