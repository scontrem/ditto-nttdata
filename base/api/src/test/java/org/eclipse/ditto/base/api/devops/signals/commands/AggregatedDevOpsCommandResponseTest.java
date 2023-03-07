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

package org.eclipse.ditto.base.api.devops.signals.commands;

import static org.eclipse.ditto.json.assertions.DittoJsonAssertions.assertThat;
import static org.mutabilitydetector.unittesting.AllowedReason.provided;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ditto.base.model.common.HttpStatus;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.signals.commands.CommandResponse;
import org.eclipse.ditto.base.model.signals.commands.GlobalCommandResponseRegistry;
import org.eclipse.ditto.json.JsonKey;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonValue;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public final class AggregatedDevOpsCommandResponseTest {

    private static final String RESPONSES_TYPE = DevOpsCommandResponse.TYPE_PREFIX + ":" + ExecutePiggybackCommand.NAME;
    private final GlobalCommandResponseRegistry underTest = GlobalCommandResponseRegistry.getInstance();

    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier.forClass(AggregatedDevOpsCommandResponse.class)
                .usingGetClass()
                .verify();
    }

    @Test
    public void assertImmutability() {
        assertInstancesOf(AggregatedDevOpsCommandResponse.class, areImmutable(),
                provided(JsonObject.class).isAlsoImmutable());
    }

    @Test
    public void testAggregatedPiggybackCommandResponseFromListOfCommandResponsesSerialization() {
        final ChangeLogLevelResponse changeLogLevelResponseForThings1 =
                ChangeLogLevelResponse.of("things", "1", true, DittoHeaders.empty());
        final ChangeLogLevelResponse changeLogLevelResponseForThings2 =
                ChangeLogLevelResponse.of("things", "2", true, DittoHeaders.empty());
        final ChangeLogLevelResponse changeLogLevelResponseForGateway1 =
                ChangeLogLevelResponse.of("gateway", "1", true, DittoHeaders.empty());

        final AggregatedDevOpsCommandResponse aggregatedDevOpsCommandResponse = AggregatedDevOpsCommandResponse.of(
                Arrays.asList(changeLogLevelResponseForThings1, changeLogLevelResponseForThings2,
                        changeLogLevelResponseForGateway1),
                RESPONSES_TYPE,
                HttpStatus.OK, DittoHeaders.empty(), true);

        final JsonObject responseToJson = aggregatedDevOpsCommandResponse.toJson();

        final CommandResponse<?> parsedCommandResponse = underTest.parse(responseToJson, DittoHeaders.empty());

        assertThat(parsedCommandResponse).isEqualTo(aggregatedDevOpsCommandResponse);
        assertThat(parsedCommandResponse.toJson()).isEqualTo(responseToJson);
    }

    @Test
    public void testSinglePiggybackCommandResponse() {
        final String EXPECTED_JSON = "{\"field1\":\"value1\",\"field2\":\"value2\"}";
        final ExecutePiggybackCommandResponse piggybackCommandResponse =
                ExecutePiggybackCommandResponse.of("connectivity", "1", HttpStatus.OK, JsonObject.of(EXPECTED_JSON),
                        DittoHeaders.empty());

        final AggregatedDevOpsCommandResponse aggregatedDevOpsCommandResponse = AggregatedDevOpsCommandResponse.of(
                List.of(piggybackCommandResponse),
                RESPONSES_TYPE,
                HttpStatus.OK, DittoHeaders.empty(), false);

        final JsonObject responseJson = aggregatedDevOpsCommandResponse.toJson();

        final CommandResponse<?> parsedCommandResponse = underTest.parse(responseJson, DittoHeaders.empty());
        final var jsonResponse = parsedCommandResponse.toJson();

        assertThat(jsonResponse.getKeys()).containsAll(
                List.of(JsonKey.of("status"), JsonKey.of("type"), JsonKey.of("responses")));
        assertThat(jsonResponse.getValue(JsonKey.of("status")).orElse(null)).isEqualTo(
                JsonValue.of(HttpStatus.OK.getCode()));
        assertThat(jsonResponse.getValue(JsonKey.of("responses")).get()).isEqualTo(JsonObject.of(EXPECTED_JSON));
        assertThat(parsedCommandResponse).isEqualTo(aggregatedDevOpsCommandResponse);
        assertThat(jsonResponse).isEqualTo(responseJson);
    }

}
