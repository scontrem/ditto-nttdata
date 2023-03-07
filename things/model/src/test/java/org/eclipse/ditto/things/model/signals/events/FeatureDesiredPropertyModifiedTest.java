/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.things.model.signals.events;

import static org.eclipse.ditto.json.assertions.DittoJsonAssertions.assertThat;
import static org.mutabilitydetector.unittesting.AllowedReason.provided;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.eclipse.ditto.base.model.json.FieldType;
import org.eclipse.ditto.base.model.signals.events.Event;
import org.eclipse.ditto.base.model.signals.events.EventsourcedEvent;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.json.JsonValue;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test for {@link FeatureDesiredPropertyModified}.
 */
public final class FeatureDesiredPropertyModifiedTest {

    private static final JsonPointer PROPERTY_JSON_POINTER = JsonFactory.newPointer("desiredProperties/target_year_1");

    private static final JsonValue NEW_PROPERTY_VALUE = JsonFactory.newValue(1953);

    private static final JsonObject KNOWN_JSON = JsonFactory.newObjectBuilder()
            .set(Event.JsonFields.TIMESTAMP, TestConstants.TIMESTAMP.toString())
            .set(Event.JsonFields.TYPE, FeatureDesiredPropertyModified.TYPE)
            .set(Event.JsonFields.METADATA, TestConstants.METADATA.toJson())
            .set(EventsourcedEvent.JsonFields.REVISION, TestConstants.Thing.REVISION_NUMBER)
            .set(ThingEvent.JsonFields.THING_ID, TestConstants.Thing.THING_ID.toString())
            .set(ThingEvent.JsonFields.FEATURE_ID, TestConstants.Feature.FLUX_CAPACITOR_ID)
            .set(FeatureDesiredPropertyModified.JSON_DESIRED_PROPERTY, PROPERTY_JSON_POINTER.toString())
            .set(FeatureDesiredPropertyModified.JSON_DESIRED_VALUE, NEW_PROPERTY_VALUE)
            .build();


    @Test
    public void assertImmutability() {
        assertInstancesOf(FeatureDesiredPropertyModified.class,
                areImmutable(),
                provided(JsonPointer.class, JsonValue.class).areAlsoImmutable());
    }


    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier.forClass(FeatureDesiredPropertyModified.class)
                .withRedefinedSuperclass()
                .verify();
    }

    @Test(expected = NullPointerException.class)
    public void tryToCreateInstanceWithNullThingId() {
        FeatureDesiredPropertyModified
                .of(null, TestConstants.Feature.FLUX_CAPACITOR_ID, PROPERTY_JSON_POINTER, NEW_PROPERTY_VALUE,
                        TestConstants.Thing.REVISION_NUMBER, null, TestConstants.EMPTY_DITTO_HEADERS, null);
    }


    @Test(expected = NullPointerException.class)
    public void tryToCreateInstanceWithNullFeatureId() {
        FeatureDesiredPropertyModified.of(TestConstants.Thing.THING_ID, null, PROPERTY_JSON_POINTER, NEW_PROPERTY_VALUE,
                TestConstants.Thing.REVISION_NUMBER, null, TestConstants.EMPTY_DITTO_HEADERS, null);
    }


    @Test(expected = NullPointerException.class)
    public void tryToCreateInstanceWithNullPropertyJsonPointer() {
        FeatureDesiredPropertyModified
                .of(TestConstants.Thing.THING_ID, TestConstants.Feature.FLUX_CAPACITOR_ID, null, NEW_PROPERTY_VALUE,
                        TestConstants.Thing.REVISION_NUMBER, null, TestConstants.EMPTY_DITTO_HEADERS, null);
    }


    @Test(expected = NullPointerException.class)
    public void tryToCreateInstanceWithNullPropertyValue() {
        FeatureDesiredPropertyModified
                .of(TestConstants.Thing.THING_ID, TestConstants.Feature.FLUX_CAPACITOR_ID, PROPERTY_JSON_POINTER, null,
                        TestConstants.Thing.REVISION_NUMBER, null, TestConstants.EMPTY_DITTO_HEADERS, null);
    }


    @Test
    public void toJsonReturnsExpected() {
        final FeatureDesiredPropertyModified underTest = FeatureDesiredPropertyModified
                .of(TestConstants.Thing.THING_ID, TestConstants.Feature.FLUX_CAPACITOR_ID, PROPERTY_JSON_POINTER,
                        NEW_PROPERTY_VALUE,
                        TestConstants.Thing.REVISION_NUMBER, TestConstants.TIMESTAMP,
                        TestConstants.EMPTY_DITTO_HEADERS, TestConstants.METADATA);
        final JsonObject actualJson = underTest.toJson(FieldType.regularOrSpecial());

        assertThat(actualJson).isEqualTo(KNOWN_JSON);
    }


    @Test
    public void createInstanceFromValidJson() {
        final FeatureDesiredPropertyModified underTest =
                FeatureDesiredPropertyModified.fromJson(KNOWN_JSON.toString(), TestConstants.EMPTY_DITTO_HEADERS);

        assertThat(underTest).isNotNull();
        assertThat((CharSequence) underTest.getEntityId()).isEqualTo(TestConstants.Thing.THING_ID);
        assertThat(underTest.getFeatureId()).isEqualTo(TestConstants.Feature.FLUX_CAPACITOR_ID);
        assertThat(underTest.getDesiredPropertyPointer()).isEqualTo(PROPERTY_JSON_POINTER);
        assertThat(underTest.getDesiredPropertyValue()).isEqualTo(NEW_PROPERTY_VALUE);
    }

}
