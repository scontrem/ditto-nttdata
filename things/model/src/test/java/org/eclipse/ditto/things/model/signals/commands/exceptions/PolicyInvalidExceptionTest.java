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
package org.eclipse.ditto.things.model.signals.commands.exceptions;

import static org.eclipse.ditto.things.model.signals.commands.assertions.ThingCommandAssertions.assertThat;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import java.net.URI;

import org.eclipse.ditto.base.model.assertions.DittoBaseAssertions;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeException;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.signals.GlobalErrorRegistry;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonField;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.things.model.signals.commands.TestConstants;
import org.junit.Test;

/**
 * Unit test for {@link PolicyInvalidException}.
 */
public class PolicyInvalidExceptionTest {

    private static final JsonObject KNOWN_JSON = JsonFactory.newObjectBuilder()
            .set(DittoRuntimeException.JsonFields.STATUS, PolicyInvalidException.STATUS.getCode())
            .set(DittoRuntimeException.JsonFields.ERROR_CODE, PolicyInvalidException.ERROR_CODE)
            .set(DittoRuntimeException.JsonFields.MESSAGE,
                    TestConstants.Thing.POLICY_INVALID_EXCEPTION.getMessage())
            .set(DittoRuntimeException.JsonFields.DESCRIPTION,
                    TestConstants.Thing.POLICY_INVALID_EXCEPTION.getDescription().orElse(null),
                    JsonField.isValueNonNull())
            .set(DittoRuntimeException.JsonFields.HREF,
                    TestConstants.Thing.POLICY_INVALID_EXCEPTION.getHref()
                            .map(URI::toString).orElse(null),
                    JsonField.isValueNonNull())
            .build();


    @Test
    public void assertImmutability() {
        assertInstancesOf(PolicyInvalidException.class, areImmutable());
    }

    @Test
    public void toJsonReturnsExpected() {
        final JsonObject jsonObject = TestConstants.Thing.POLICY_INVALID_EXCEPTION.toJson();

        assertThat(jsonObject).isEqualTo(KNOWN_JSON);
    }


    @Test
    public void createInstanceFromValidJson() {
        final PolicyInvalidException underTest =
                PolicyInvalidException.fromJson(KNOWN_JSON, DittoHeaders.empty());

        DittoBaseAssertions.assertThat(underTest).isEqualTo(TestConstants.Thing.POLICY_INVALID_EXCEPTION);
    }


    @Test
    public void checkGetters() {
        final String expectedMessage =
                "The Policy specified for the Thing with ID '" + TestConstants.Thing.THING_ID + "' is invalid.";
        final String expectedDescription = "It must contain at least one Subject with the following permission(s): " +
                "'[" + String.join(", ", TestConstants.Thing.REQUIRED_THING_PERMISSIONS) + "]'!";

        DittoBaseAssertions.assertThat(TestConstants.Thing.POLICY_INVALID_EXCEPTION)
                .hasMessage(expectedMessage)
                .hasDescription(expectedDescription)
                .hasErrorCode(PolicyInvalidException.ERROR_CODE)
                .hasStatus(PolicyInvalidException.STATUS);
    }

    @Test
    public void checkThingErrorCodeWorks() {
        final DittoRuntimeException actual =
                GlobalErrorRegistry.getInstance().parse(KNOWN_JSON, TestConstants.EMPTY_DITTO_HEADERS);

        assertThat(actual).isEqualTo(TestConstants.Thing.POLICY_INVALID_EXCEPTION);
    }

}
