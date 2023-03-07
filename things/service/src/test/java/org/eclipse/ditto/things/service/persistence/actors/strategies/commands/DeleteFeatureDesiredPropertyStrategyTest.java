/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.things.service.persistence.actors.strategies.commands;

import static org.eclipse.ditto.things.model.TestConstants.Feature.FLUX_CAPACITOR;
import static org.eclipse.ditto.things.model.TestConstants.Feature.FLUX_CAPACITOR_ID;
import static org.eclipse.ditto.things.model.TestConstants.Thing.THING_V2;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.eclipse.ditto.base.model.exceptions.DittoRuntimeException;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.internal.utils.persistentactors.commands.CommandStrategy;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.things.model.Feature;
import org.eclipse.ditto.things.model.ThingId;
import org.eclipse.ditto.things.model.signals.commands.modify.DeleteFeatureDesiredProperty;
import org.eclipse.ditto.things.model.signals.commands.modify.DeleteFeatureDesiredPropertyResponse;
import org.eclipse.ditto.things.model.signals.events.FeatureDesiredPropertyDeleted;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for {@link DeleteFeatureDesiredPropertyStrategy}.
 */
public final class DeleteFeatureDesiredPropertyStrategyTest extends AbstractCommandStrategyTest {

    private static String featureId;
    private static JsonPointer propertyPointer;

    private DeleteFeatureDesiredPropertyStrategy underTest;

    @BeforeClass
    public static void initTestFixture() {
        featureId = FLUX_CAPACITOR_ID;
        propertyPointer = JsonFactory.newPointer("/target_year_2");
    }

    @Before
    public void setUp() {
        underTest = new DeleteFeatureDesiredPropertyStrategy();
    }

    @Test
    public void assertImmutability() {
        assertInstancesOf(DeleteFeatureDesiredPropertyStrategy.class, areImmutable());
    }

    @Test
    public void successfullyDeleteFeatureDesiredPropertyFromThing() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final DeleteFeatureDesiredProperty command =
                DeleteFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, DittoHeaders.empty());

        assertModificationResult(underTest, THING_V2, command,
                FeatureDesiredPropertyDeleted.class,
                DeleteFeatureDesiredPropertyResponse.of(context.getState(),
                        command.getFeatureId(), propertyPointer, command.getDittoHeaders()));
    }

    @Test
    public void deleteFeatureDesiredPropertyFromThingWithoutFeatures() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final DeleteFeatureDesiredProperty command =
                DeleteFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, DittoHeaders.empty());
        final DittoRuntimeException expectedException =
                ExceptionFactory.featureNotFound(context.getState(), command.getFeatureId(),
                        command.getDittoHeaders());

        assertErrorResult(underTest, THING_V2.removeFeatures(), command, expectedException);
    }

    @Test
    public void deleteFeatureDesiredPropertyFromThingWithoutThatFeature() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final DeleteFeatureDesiredProperty command =
                DeleteFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, DittoHeaders.empty());
        final DittoRuntimeException expectedException =
                ExceptionFactory.featureNotFound(context.getState(), command.getFeatureId(),
                        command.getDittoHeaders());

        assertErrorResult(underTest, THING_V2.removeFeature(featureId), command, expectedException);
    }

    @Test
    public void deleteFeatureDesiredPropertyFromFeatureWithoutDesiredProperties() {
        final Feature feature = FLUX_CAPACITOR.removeDesiredProperties();
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final DeleteFeatureDesiredProperty command =
                DeleteFeatureDesiredProperty.of(context.getState(), feature.getId(), propertyPointer, DittoHeaders.empty());
        final DittoRuntimeException expectedException =
                ExceptionFactory.featureDesiredPropertyNotFound(context.getState(), command.getFeatureId(),
                        propertyPointer, command.getDittoHeaders());

        assertErrorResult(underTest, THING_V2.setFeature(feature), command, expectedException);
    }

    @Test
    public void deleteFeatureDesiredPropertyFromFeatureWithoutThatProperty() {
        final Feature feature = FLUX_CAPACITOR.removeDesiredProperty(propertyPointer);
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final DeleteFeatureDesiredProperty command =
                DeleteFeatureDesiredProperty.of(context.getState(), feature.getId(), propertyPointer, DittoHeaders.empty());
        final DittoRuntimeException expectedException =
                ExceptionFactory.featureDesiredPropertyNotFound(context.getState(), command.getFeatureId(),
                        propertyPointer, command.getDittoHeaders());

        assertErrorResult(underTest, THING_V2.setFeature(feature), command, expectedException);
    }

}
