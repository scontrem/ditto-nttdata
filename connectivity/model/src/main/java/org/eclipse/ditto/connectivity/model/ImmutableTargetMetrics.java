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
package org.eclipse.ditto.connectivity.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.base.model.json.JsonSchemaVersion;
import org.eclipse.ditto.json.JsonCollectors;
import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonField;
import org.eclipse.ditto.json.JsonObject;

/**
 * Immutable implementation of {@link TargetMetrics}.
 */
@Immutable
final class ImmutableTargetMetrics implements TargetMetrics {

    private final Map<String, AddressMetric> addressMetrics;

    private ImmutableTargetMetrics(final Map<String, AddressMetric> addressMetrics) {
        this.addressMetrics = Collections.unmodifiableMap(new HashMap<>(addressMetrics));
    }

    /**
     * Creates a new {@code ImmutableTargetMetrics} instance.
     *
     * @param addressMetrics the AddressMetrics for each target
     * @return a new instance of ImmutableTargetMetrics
     */
    public static ImmutableTargetMetrics of(final Map<String, AddressMetric> addressMetrics) {
        return new ImmutableTargetMetrics(addressMetrics);
    }

    @Override
    public Map<String, AddressMetric> getAddressMetrics() {
        return addressMetrics;
    }

    @Override
    public JsonObject toJson(final JsonSchemaVersion schemaVersion, final Predicate<JsonField> thePredicate) {
        final Predicate<JsonField> predicate = schemaVersion.and(thePredicate);

        return JsonObject.newBuilder()
                .set(JsonFields.ADDRESS_METRICS,
                        addressMetrics.isEmpty() ? JsonFactory.nullObject() : addressMetricsToJson(),
                        predicate)
                .build();
    }

    private JsonObject addressMetricsToJson() {
        return addressMetrics.entrySet()
                .stream()
                .map(e -> ImmutableAddressMetric.toJsonField(e.getKey(), e.getValue()))
                .collect(JsonCollectors.fieldsToObject());
    }

    /**
     * Creates a new {@code TargetMetrics} object from the specified JSON object.
     *
     * @param jsonObject a JSON object which provides the data for the TargetMetrics to be created.
     * @return a new TargetMetrics which is initialised with the extracted data from {@code jsonObject}.
     * @throws NullPointerException if {@code jsonObject} is {@code null}.
     * @throws org.eclipse.ditto.json.JsonParseException if {@code jsonObject} is not an appropriate JSON object.
     */
    public static TargetMetrics fromJson(final JsonObject jsonObject) {
        final Map<String, AddressMetric> readAddressMetrics = jsonObject.getValue(JsonFields.ADDRESS_METRICS)
                .map(obj -> obj.stream()
                        .collect(Collectors.toMap(
                                f -> f.getKey().toString(),
                                f -> ConnectivityModelFactory.addressMetricFromJson(f.getValue().asObject()))))
                .orElse(Collections.emptyMap());
        return ImmutableTargetMetrics.of(readAddressMetrics);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImmutableTargetMetrics)) {
            return false;
        }
        final ImmutableTargetMetrics that = (ImmutableTargetMetrics) o;
        return Objects.equals(addressMetrics, that.addressMetrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressMetrics);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" +
                "addressMetrics=" + addressMetrics +
                "]";
    }

}
