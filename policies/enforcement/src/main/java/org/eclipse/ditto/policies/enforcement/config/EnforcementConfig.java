/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.policies.enforcement.config;

import java.util.List;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.base.model.signals.Signal;
import org.eclipse.ditto.base.model.signals.commands.Command;
import org.eclipse.ditto.internal.utils.cacheloaders.config.AskWithRetryConfig;
import org.eclipse.ditto.internal.utils.config.KnownConfigValue;

/**
 * Provides configuration settings for enforcement behaviour.
 */
@Immutable
public interface EnforcementConfig {

    /**
     * Returns the configuration for the used "ask with retry" pattern in to e.g. load things+policies.
     *
     * @return the "ask with retry" pattern config for retrieval of things and policies.
     */
    AskWithRetryConfig getAskWithRetryConfig();

    /**
     * Returns whether live responses from channels other than their subscribers should be dispatched.
     *
     * @return whether global live response dispatching is enabled.
     */
    boolean isDispatchLiveResponsesGlobally();

    /**
     * Returns a list of namespaces for which a special usage logging should be enabled in enforcement.
     *
     * @return list of namespaces which should be inspected.
     */
    Set<String> getSpecialLoggingInspectedNamespaces();

    /**
     * Check if global dispatch of a signal should be supported.
     *
     * @param signal the signal.
     * @return whether global dispatch support is needed.
     */
    default boolean shouldDispatchGlobally(final Signal<?> signal) {
        return isDispatchLiveResponsesGlobally() &&
                signal instanceof Command<?> &&
                signal.getDittoHeaders().isResponseRequired();
    }

    /**
     * An enumeration of the known config path expressions and their associated default values for
     * {@code EnforcementConfig}.
     */
    enum EnforcementConfigValue implements KnownConfigValue {

        /**
         * Whether to enable dispatching live responses from channels other than the subscribers.
         */
        GLOBAL_LIVE_RESPONSE_DISPATCHING("global-live-response-dispatching", true),

        /**
         * List of namespaces for which a special usage logging should be enabled in enforcement.
         */
        SPECIAL_LOGGING_INSPECTED_NAMESPACES("special-logging-inspected-namespaces", List.of());

        private final String path;
        private final Object defaultValue;

        EnforcementConfigValue(final String thePath, final Object theDefaultValue) {
            path = thePath;
            defaultValue = theDefaultValue;
        }

        @Override
        public String getConfigPath() {
            return path;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }

    }

}
