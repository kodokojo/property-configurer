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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class PropertyResolverTest {

    public interface ConfigSimple extends PropertyConfig {
        @Key("props.a")
        String a();
    }

    public interface ValidateConfigSimple extends PropertyConfig {
        @Key("props.a")
        String a();

        @Override
        default void valid() {
            throw new IllegalStateException("fake exception");
        }
    }

    public interface DefaultNumber extends PropertyConfig {

        @Key(value = "props.number", defaultValue = "")
        Long phoneNumber();

    }

    @Test
    public void default_validated_config() {
        ConfigSimple configSimple = createConfig(ConfigSimple.class, createPropertyValueProvider(), true);
        assertThat(configSimple).isNotNull();
        assertThat(configSimple.a()).isEqualTo("a");
    }

    @Test
    public void override_validated_config() {
        try {
            createConfig(ValidateConfigSimple.class, createPropertyValueProvider(), true);
            fail("Expected an exception");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("fake exception");
        }
    }

    @Test
    public void emptyDefaultValueThrowException() {

        DefaultNumber defaultNumber = createConfig(DefaultNumber.class, createPropertyValueProvider(), false);
        boolean iseSent = false;
        try {
            defaultNumber.phoneNumber();
            fail("Expecting an exception.");
        } catch (NumberFormatException n) {
            fail("Not expected exception", n);
        } catch (IllegalStateException e) {
            iseSent = true;
            assertThat(e.getMessage()).contains("Invalid value for key 'props.number' : ''");
        }
        assertThat(iseSent).isTrue();
    }

    private static PropertyValueProvider createPropertyValueProvider() {
        return new PropertyValueProvider() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> T providePropertyValue(Class<T> classType, String key) {
                switch (key) {
                    case "props.a":
                        return (T) "a";
                    case "props.b":
                        return (T) "b";
                }
                return null;
            }
        };
    }

    private <T extends PropertyConfig> T createConfig(Class<T> configClass, PropertyValueProvider valueProvider, boolean validated) {
        PropertyResolver resolver = new PropertyResolver(valueProvider);
        T proxy = resolver.createProxy(configClass);
        if (validated) proxy.valid();
        return proxy;
    }
}