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
package org.eclipse.ditto.things.service.signaltransformation.placeholdersubstitution;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.base.model.signals.Signal;
import org.eclipse.ditto.base.service.signaltransformer.placeholdersubstitution.SubstitutionStrategy;
import org.eclipse.ditto.base.service.signaltransformer.placeholdersubstitution.SubstitutionStrategyRegistry;

/**
 * Registry containing all of the thing specific instances of {@link SubstitutionStrategy}.
 */
@Immutable
final class ThingSubstitutionStrategyRegistry implements SubstitutionStrategyRegistry {

    private final List<SubstitutionStrategy<? extends Signal<?>>> strategies;

    private ThingSubstitutionStrategyRegistry() {
        strategies = List.copyOf(createStrategies());
    }

    public static ThingSubstitutionStrategyRegistry newInstance() {
        return new ThingSubstitutionStrategyRegistry();
    }

    /**
     * Get a matching strategy for handling the given {@code signal}.
     *
     * @param signal the instance of {@link Signal} to be handled.
     * @return an {@link Optional} containing the first strategy which matches; an empty {@link Optional} in case no
     * strategy matches.
     */
    @Override
    public Optional<SubstitutionStrategy<? extends Signal<?>>> getMatchingStrategy(final Signal<?> signal) {
        for (final SubstitutionStrategy<? extends Signal<?>> strategy : strategies) {
            if (strategy.matches(signal)) {
                return Optional.of(strategy);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<SubstitutionStrategy<? extends Signal<?>>> getStrategies() {
        return strategies;
    }

    private static List<SubstitutionStrategy<? extends Signal<?>>> createStrategies() {
        final List<SubstitutionStrategy<? extends Signal<?>>> strategies = new LinkedList<>();

        // replacement for policy-subject-id
        strategies.add(new CreateThingSubstitutionStrategy());

        return strategies;
    }
}
