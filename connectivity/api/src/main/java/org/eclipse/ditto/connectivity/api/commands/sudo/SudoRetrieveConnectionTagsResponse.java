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
package org.eclipse.ditto.connectivity.api.commands.sudo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.base.model.common.HttpStatus;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.json.FieldType;
import org.eclipse.ditto.base.model.json.JsonParsableCommandResponse;
import org.eclipse.ditto.base.model.json.JsonSchemaVersion;
import org.eclipse.ditto.base.model.signals.commands.AbstractCommandResponse;
import org.eclipse.ditto.base.model.signals.commands.CommandResponseHttpStatusValidator;
import org.eclipse.ditto.base.model.signals.commands.CommandResponseJsonDeserializer;
import org.eclipse.ditto.json.JsonArray;
import org.eclipse.ditto.json.JsonField;
import org.eclipse.ditto.json.JsonFieldDefinition;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonObjectBuilder;
import org.eclipse.ditto.json.JsonValue;

/**
 * Response to a {@link SudoRetrieveConnectionTags} command.
 *
 * @since 3.0.0
 */
@Immutable
@JsonParsableCommandResponse(type = SudoRetrieveConnectionTagsResponse.TYPE)
public final class SudoRetrieveConnectionTagsResponse
        extends AbstractCommandResponse<SudoRetrieveConnectionTagsResponse>
        implements ConnectivitySudoQueryCommandResponse<SudoRetrieveConnectionTagsResponse> {

    /**
     * Type of this response.
     */
    public static final String TYPE = TYPE_PREFIX + SudoRetrieveConnectionTags.NAME;

    static final JsonFieldDefinition<JsonArray> CONNECTION_TAGS =
            JsonFieldDefinition.ofJsonArray("connectionTags", FieldType.REGULAR, JsonSchemaVersion.V_2);

    private static final HttpStatus HTTP_STATUS = HttpStatus.OK;

    private static final CommandResponseJsonDeserializer<SudoRetrieveConnectionTagsResponse> JSON_DESERIALIZER =
            CommandResponseJsonDeserializer.newInstance(TYPE,
                    context -> {
                        final JsonObject jsonObject = context.getJsonObject();
                        return new SudoRetrieveConnectionTagsResponse(
                                fromArray(jsonObject.getValueOrThrow(CONNECTION_TAGS)),
                                context.getDeserializedHttpStatus(),
                                context.getDittoHeaders());
                    });

    private final Set<String> connectionTags;

    private SudoRetrieveConnectionTagsResponse(final Collection<String> connectionTags,
            final HttpStatus httpStatus,
            final DittoHeaders dittoHeaders) {

        super(TYPE,
                CommandResponseHttpStatusValidator.validateHttpStatus(httpStatus,
                        Collections.singleton(HTTP_STATUS),
                        SudoRetrieveConnectionTagsResponse.class),
                dittoHeaders);
        this.connectionTags = Collections.unmodifiableSet(new LinkedHashSet<>(connectionTags));
    }

    /**
     * Returns a new instance of {@code SudoRetrieveConnectionTagsResponse}.
     *
     * @param dittoHeaders the headers of the request.
     * @param connectionTags the connection tags.
     * @return a new SudoRetrieveConnectionTagsResponse response.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public static SudoRetrieveConnectionTagsResponse of(final Collection<String> connectionTags,
            final DittoHeaders dittoHeaders) {

        return new SudoRetrieveConnectionTagsResponse(connectionTags, HTTP_STATUS, dittoHeaders);
    }

    /**
     * Creates a new {@code SudoRetrieveConnectionTagsResponse} from a JSON string.
     *
     * @param jsonString the JSON string of which the response is to be retrieved.
     * @param dittoHeaders the headers of the response.
     * @return the response.
     * @throws NullPointerException if {@code jsonString} is {@code null}.
     * @throws IllegalArgumentException if {@code jsonString} is empty.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonString} was not in the expected
     * format.
     */
    public static SudoRetrieveConnectionTagsResponse fromJson(final String jsonString,
            final DittoHeaders dittoHeaders) {
        return fromJson(JsonObject.of(jsonString), dittoHeaders);
    }

    /**
     * Creates a new {@code SudoRetrieveConnectionTagsResponse} from a JSON object.
     *
     * @param jsonObject the JSON object of which the response is to be created.
     * @param dittoHeaders the headers of the response.
     * @return the response.
     * @throws NullPointerException if {@code jsonObject} is {@code null}.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * format.
     */
    public static SudoRetrieveConnectionTagsResponse fromJson(final JsonObject jsonObject,
            final DittoHeaders dittoHeaders) {

        return JSON_DESERIALIZER.deserialize(jsonObject, dittoHeaders);
    }

    @Override
    protected void appendPayload(final JsonObjectBuilder jsonObjectBuilder,
            final JsonSchemaVersion schemaVersion,
            final Predicate<JsonField> thePredicate) {

        final Predicate<JsonField> predicate = schemaVersion.and(thePredicate);
        jsonObjectBuilder.set(CONNECTION_TAGS, JsonArray.of(connectionTags), predicate);
    }

    public Set<String> getConnectionTags() {
        return connectionTags;
    }

    @Override
    public SudoRetrieveConnectionTagsResponse setEntity(final JsonValue entity) {
        return of(fromArray(entity.asArray()), getDittoHeaders());
    }

    private static Set<String> fromArray(final JsonArray array) {
        return array.stream()
                .filter(JsonValue::isString)
                .map(JsonValue::asString)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public JsonValue getEntity(final JsonSchemaVersion schemaVersion) {
        return JsonArray.of(connectionTags);
    }

    @Override
    public SudoRetrieveConnectionTagsResponse setDittoHeaders(final DittoHeaders dittoHeaders) {
        return of(connectionTags, dittoHeaders);
    }

    @Override
    protected boolean canEqual(@Nullable final Object other) {
        return other instanceof SudoRetrieveConnectionTagsResponse;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final SudoRetrieveConnectionTagsResponse that = (SudoRetrieveConnectionTagsResponse) o;
        return Objects.equals(connectionTags, that.connectionTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connectionTags);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" +
                super.toString() +
                ", connectionTags=" + connectionTags +
                "]";
    }

}
