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
package io.kodokojo.property.configurer.provider;

import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * {@link PropertyValueProvider} which lookup in a given {@link Properties}.
 */
public class PropertiesValueProvider implements PropertyValueProvider {

    private final Properties properties;

    public PropertiesValueProvider(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties must be defined.");
        }
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T providePropertyValue(Class<T> classType, String key) {
        if (classType == null) {
            throw new IllegalArgumentException("classType must be defined.");
        }
        if (isBlank(key)) {
            throw new IllegalArgumentException("key must be defined.");
        }
        Object valueObject = properties.get(key);
        if (valueObject != null && !classType.isAssignableFrom(valueObject.getClass())) {
            throw new IllegalArgumentException("Property key return value '" + valueObject + "' which type '" + valueObject.getClass().getCanonicalName() + " is not the expected '" + classType.getCanonicalName() + "'.");
        }
        return (T) valueObject;
    }
}
