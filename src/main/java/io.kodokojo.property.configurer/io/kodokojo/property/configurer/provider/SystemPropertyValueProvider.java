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

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * {@link PropertyValueProvider} which lookup in the System properties variables.
 */
public class SystemPropertyValueProvider extends AbstractStringPropertyValueProvider {
    @Override
    protected String provideValue(String key) {
        if (isBlank(key)) {
            throw new IllegalArgumentException("key must be defined.");
        }
        return System.getProperty(key);
    }
}
