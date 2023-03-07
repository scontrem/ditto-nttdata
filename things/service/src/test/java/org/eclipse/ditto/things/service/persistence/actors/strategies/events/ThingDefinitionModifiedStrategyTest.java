/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.things.service.persistence.actors.strategies.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.things.model.Thing;
import org.eclipse.ditto.things.model.signals.events.ThingDefinitionModified;
import org.junit.Test;

/**
 * Unit test for {@link ThingDefinitionModifiedStrategy}.
 */
public final class ThingDefinitionModifiedStrategyTest extends AbstractStrategyTest {

    @Test
    public void assertImmutability() {
        assertInstancesOf(ThingDefinitionModifiedStrategy.class, areImmutable());
    }

    @Test
    public void appliesEventCorrectly() {
        final ThingDefinitionModifiedStrategy strategy = new ThingDefinitionModifiedStrategy();
        final ThingDefinitionModified event = ThingDefinitionModified.of(THING_ID, THING_DEFINITION, REVISION,
                TIMESTAMP, DittoHeaders.empty(), null);

        final Thing thingWithEventApplied = strategy.handle(event, THING, NEXT_REVISION);

        final Thing expected = THING.toBuilder()
                .setDefinition(THING_DEFINITION)
                .setRevision(NEXT_REVISION)
                .setModified(TIMESTAMP)
                .build();
        assertThat(thingWithEventApplied).isEqualTo(expected);
    }

}
