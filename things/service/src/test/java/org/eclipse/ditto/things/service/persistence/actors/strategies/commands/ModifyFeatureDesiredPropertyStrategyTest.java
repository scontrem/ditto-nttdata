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

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.ditto.things.model.TestConstants.Thing.THING_V2;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.eclipse.ditto.base.model.entity.metadata.Metadata;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeException;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.headers.metadata.MetadataHeaderKey;
import org.eclipse.ditto.internal.utils.persistentactors.commands.CommandStrategy;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.json.JsonValue;
import org.eclipse.ditto.things.model.TestConstants;
import org.eclipse.ditto.things.model.ThingId;
import org.eclipse.ditto.things.model.signals.commands.modify.ModifyFeatureDesiredProperty;
import org.eclipse.ditto.things.model.signals.events.FeatureDesiredPropertyCreated;
import org.eclipse.ditto.things.model.signals.events.FeatureDesiredPropertyModified;
import org.eclipse.ditto.things.service.persistence.actors.ETagTestUtils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for {@link ModifyFeatureDesiredPropertyStrategy}.
 */
public final class ModifyFeatureDesiredPropertyStrategyTest extends AbstractCommandStrategyTest {

    private static String featureId;
    private static JsonPointer propertyPointer;
    private static JsonValue newPropertyValue;

    private ModifyFeatureDesiredPropertyStrategy underTest;

    @BeforeClass
    public static void initTestFixture() {
        featureId = TestConstants.Feature.FLUX_CAPACITOR_ID;
        propertyPointer = JsonFactory.newPointer("target_year_3");
        newPropertyValue = JsonFactory.newValue("Foo!");
    }

    @Before
    public void setUp() {
        underTest = new ModifyFeatureDesiredPropertyStrategy();
    }

    @Test
    public void assertImmutability() {
        assertInstancesOf(ModifyFeatureDesiredPropertyStrategy.class, areImmutable());
    }

    @Test
    public void modifyFeatureDesiredPropertyOnThingWithoutFeatures() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final ModifyFeatureDesiredProperty command =
                ModifyFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, newPropertyValue,
                        DittoHeaders.empty());
        final DittoRuntimeException expectedException =
                ExceptionFactory.featureNotFound(context.getState(), command.getFeatureId(),
                        command.getDittoHeaders());

        assertErrorResult(underTest, THING_V2.removeFeatures(), command, expectedException);
    }

    @Test
    public void modifyFeatureDesiredPropertyOnThingWithoutThatFeature() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final ModifyFeatureDesiredProperty command =
                ModifyFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, newPropertyValue,
                        DittoHeaders.empty());
        final DittoRuntimeException expectedException =
                ExceptionFactory.featureNotFound(context.getState(), command.getFeatureId(),
                        command.getDittoHeaders());

        assertErrorResult(underTest, THING_V2.removeFeature(featureId), command, expectedException);
    }

    @Test
    public void modifyFeatureDesiredPropertyOfFeatureWithoutProperties() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final ModifyFeatureDesiredProperty command =
                ModifyFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, newPropertyValue,
                        DittoHeaders.empty());

        assertModificationResult(underTest, THING_V2.removeFeatureDesiredProperties(featureId), command,
                FeatureDesiredPropertyCreated.class,
                ETagTestUtils.modifyFeatureDesiredPropertyResponse(context.getState(), command.getFeatureId(),
                        command.getDesiredPropertyPointer(), command.getDesiredPropertyValue(), command.getDittoHeaders(), true));
    }

    @Test
    public void modifyExistingFeatureDesiredProperty() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final ModifyFeatureDesiredProperty command =
                ModifyFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, newPropertyValue,
                        DittoHeaders.empty());

        assertModificationResult(underTest, THING_V2, command,
                FeatureDesiredPropertyModified.class,
                ETagTestUtils.modifyFeatureDesiredPropertyResponse(context.getState(), command.getFeatureId(),
                        command.getDesiredPropertyPointer(), command.getDesiredPropertyValue(), command.getDittoHeaders(), false));
    }

    @Test
    public void setMetadata() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final ModifyFeatureDesiredProperty command =
                ModifyFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, newPropertyValue,
                        DittoHeaders.newBuilder()
                                .putMetadata(MetadataHeaderKey.of(JsonPointer.of("meta")), JsonObject.newBuilder()
                                        .set("description", "bumlux")
                                        .build())
                                .build());

        final FeatureDesiredPropertyModified event =
                assertModificationResult(underTest, THING_V2, command,
                        FeatureDesiredPropertyModified.class,
                        ETagTestUtils.modifyFeatureDesiredPropertyResponse(context.getState(), command.getFeatureId(),
                                command.getDesiredPropertyPointer(), command.getDesiredPropertyValue(), command.getDittoHeaders(),
                                false));

        assertThat((CharSequence) event.getResourcePath()).isEqualTo(command.getResourcePath());
        assertThat(event.getMetadata()).contains(Metadata.newMetadata(JsonObject.newBuilder()
                .set("meta", JsonObject.newBuilder()
                        .set("description", "bumlux")
                        .build())
                .build()));
    }

    @Test
    public void restrictMetadataUnderExistingFields() {
        final CommandStrategy.Context<ThingId> context = getDefaultContext();
        final JsonObject propertyObject = JsonObject.newBuilder()
                .set("a", 1)
                .set(JsonPointer.of("b/c"), 3)
                .build();
        final ModifyFeatureDesiredProperty command =
                ModifyFeatureDesiredProperty.of(context.getState(), featureId, propertyPointer, propertyObject,
                        DittoHeaders.newBuilder()
                                .putMetadata(MetadataHeaderKey.of(JsonPointer.of("m0")), JsonValue.of(0))
                                .putMetadata(MetadataHeaderKey.of(JsonPointer.of("a/m1")), JsonValue.of(1))
                                .putMetadata(MetadataHeaderKey.of(JsonPointer.of("b/m2")), JsonValue.of(2))
                                // metadata b/c is ignored because it exists as value
                                .putMetadata(MetadataHeaderKey.of(JsonPointer.of("b/c")), JsonValue.of(3))
                                // metadata e/f is ignored because e does not exist as value
                                .putMetadata(MetadataHeaderKey.of(JsonPointer.of("e/f")), JsonValue.of(4))
                                .build());

        final FeatureDesiredPropertyModified event =
                assertModificationResult(underTest, THING_V2, command,
                        FeatureDesiredPropertyModified.class,
                        ETagTestUtils.modifyFeatureDesiredPropertyResponse(context.getState(), command.getFeatureId(),
                                command.getDesiredPropertyPointer(), command.getDesiredPropertyValue(), command.getDittoHeaders(),
                                false));

        assertThat((CharSequence) event.getResourcePath()).isEqualTo(command.getResourcePath());
        assertThat(event.getMetadata()).contains(Metadata.newMetadata(JsonObject.of("{" +
                "  \"m0\":0," +
                "  \"a\":{\"m1\":1}," +
                "  \"b\":{\"m2\":2}" +
                "}")));
    }

}
