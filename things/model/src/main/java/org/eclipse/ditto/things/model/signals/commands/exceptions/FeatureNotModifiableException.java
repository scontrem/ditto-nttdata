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

import java.net.URI;
import java.text.MessageFormat;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.base.model.common.HttpStatus;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeException;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeExceptionBuilder;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.json.JsonParsableException;
import org.eclipse.ditto.things.model.ThingException;
import org.eclipse.ditto.things.model.ThingId;

/**
 * Indicates that the feature cannot be modified.
 */
@Immutable
@JsonParsableException(errorCode = FeatureNotModifiableException.ERROR_CODE)
public class FeatureNotModifiableException extends DittoRuntimeException implements ThingException {

    /**
     * Error code of this exception.
     */
    public static final String ERROR_CODE = ERROR_CODE_PREFIX + "feature.notmodifiable";

    private static final String MESSAGE_TEMPLATE = "The Feature with ID ''{0}'' on the Thing with ID ''{1}'' could not "
            + "be modified as the requester had insufficient permissions to modify it ('WRITE' is required).";

    private static final String DEFAULT_DESCRIPTION =
            "Check if the ID of the Thing and the ID of your requested Feature"
                    + " was correct and you have sufficient permissions.";

    private static final long serialVersionUID = 7276337778530053496L;

    private FeatureNotModifiableException(final DittoHeaders dittoHeaders,
            @Nullable final String message,
            @Nullable final String description,
            @Nullable final Throwable cause,
            @Nullable final URI href) {
        super(ERROR_CODE, HttpStatus.FORBIDDEN, dittoHeaders, message, description, cause, href);
    }

    /**
     * A mutable builder for a {@code FeatureNotModifiableException}.
     *
     * @param thingId the ID of the thing.
     * @param featureId the ID of the feature which is either not present on the thing or the requester had insufficient
     * access permissions.
     * @return the builder.
     */
    public static Builder newBuilder(final ThingId thingId, final String featureId) {
        return new Builder(thingId, featureId);
    }

    /**
     * Constructs a new {@code FeatureNotModifiableException} object with given message.
     *
     * @param message detail message. This message can be later retrieved by the {@link #getMessage()} method.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new FeatureNotModifiableException.
     * @throws NullPointerException if {@code dittoHeaders} is {@code null}.
     */
    public static FeatureNotModifiableException fromMessage(@Nullable final String message,
            final DittoHeaders dittoHeaders) {
        return DittoRuntimeException.fromMessage(message, dittoHeaders, new Builder());
    }

    /**
     * Constructs a new {@link FeatureNotModifiableException} object with the exception message extracted from the
     * given JSON object.
     *
     * @param jsonObject the JSON to read the {@link org.eclipse.ditto.base.model.exceptions.DittoRuntimeException.JsonFields#MESSAGE} field from.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new FeatureNotModifiableException.
     * @throws NullPointerException if any argument is {@code null}.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if this JsonObject did not contain an error message.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * format.
     */
    public static FeatureNotModifiableException fromJson(final JsonObject jsonObject,
            final DittoHeaders dittoHeaders) {
        return DittoRuntimeException.fromJson(jsonObject, dittoHeaders, new Builder());
    }

    @Override
    public DittoRuntimeException setDittoHeaders(final DittoHeaders dittoHeaders) {
        return new Builder()
                .message(getMessage())
                .description(getDescription().orElse(null))
                .cause(getCause())
                .href(getHref().orElse(null))
                .dittoHeaders(dittoHeaders)
                .build();
    }

    /**
     * A mutable builder with a fluent API for a {@link FeatureNotModifiableException}.
     */
    public static final class Builder extends DittoRuntimeExceptionBuilder<FeatureNotModifiableException> {

        private Builder() {
            description(DEFAULT_DESCRIPTION);
        }

        private Builder(final ThingId thingId, final String featureId) {
            this();
            message(MessageFormat.format(MESSAGE_TEMPLATE, featureId, String.valueOf(thingId)));
        }

        @Override
        protected FeatureNotModifiableException doBuild(final DittoHeaders dittoHeaders,
                @Nullable final String message,
                @Nullable final String description,
                @Nullable final Throwable cause,
                @Nullable final URI href) {
            return new FeatureNotModifiableException(dittoHeaders, message, description, cause, href);
        }
    }
}
