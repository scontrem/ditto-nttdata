/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.base.model.signals;

import java.net.URI;
import java.text.MessageFormat;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.base.model.common.HttpStatus;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeException;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeExceptionBuilder;
import org.eclipse.ditto.base.model.exceptions.GeneralException;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.json.JsonParsableException;
import org.eclipse.ditto.base.model.json.JsonSchemaVersion;
import org.eclipse.ditto.json.JsonObject;

/**
 * Thrown if a {@link Signal} is not supported by the version called.
 */
@Immutable
@JsonParsableException(errorCode = UnsupportedSchemaVersionException.ERROR_CODE)
public final class UnsupportedSchemaVersionException extends DittoRuntimeException implements GeneralException {

    /**
     * Error code of this exception.
     */
    public static final String ERROR_CODE = ERROR_CODE_PREFIX + "unsupported.api.version";

    private static final String MESSAGE_TEMPLATE = "The requested resource is not supported by version ''{0}''.";

    private static final String DEFAULT_DESCRIPTION = "Check if you specified the correct version of the API.";

    private static final long serialVersionUID = 1180217017112980012L;

    private UnsupportedSchemaVersionException(final DittoHeaders dittoHeaders,
            @Nullable final String message,
            @Nullable final String description,
            @Nullable final Throwable cause,
            @Nullable final URI href) {
        super(ERROR_CODE, HttpStatus.NOT_FOUND, dittoHeaders, message, description, cause, href);
    }

    /**
     * A mutable builder for a {@code UnsupportedSchemaVersionException}.
     *
     * @param version the version number.
     * @return the builder.
     */
    public static Builder newBuilder(final int version) {
        return new Builder(version);
    }

    /**
     * A mutable builder for a {@code UnsupportedSchemaVersionException}.
     *
     * @param version the version number.
     * @return the builder.
     */
    public static Builder newBuilder(final JsonSchemaVersion version) {
        return new Builder(version.toInt());
    }

    /**
     * Constructs a new {@code UnsupportedSchemaVersionException} object with given message.
     *
     * @param message detail message. This message can be later retrieved by the {@link #getMessage()} method.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new UnsupportedSchemaVersionException.
     * @throws NullPointerException if {@code dittoHeaders} is {@code null}.
     */
    public static UnsupportedSchemaVersionException fromMessage(@Nullable final String message,
            final DittoHeaders dittoHeaders) {
        return DittoRuntimeException.fromMessage(message, dittoHeaders, new Builder());
    }

    /**
     * Constructs a new {@code UnsupportedSchemaVersionException} object with the exception message extracted from the
     * given JSON object.
     *
     * @param jsonObject the JSON to read the {@link org.eclipse.ditto.base.model.exceptions.DittoRuntimeException.JsonFields#MESSAGE} field from.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new UnsupportedSchemaVersionException.
     * @throws NullPointerException if any argument is {@code null}.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if this JsonObject did not contain an error message.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * format.
     */
    public static UnsupportedSchemaVersionException fromJson(final JsonObject jsonObject,
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
     * A mutable builder with a fluent API for a {@link UnsupportedSchemaVersionException}.
     */
    @NotThreadSafe
    public static final class Builder extends DittoRuntimeExceptionBuilder<UnsupportedSchemaVersionException> {

        private Builder() {
            description(DEFAULT_DESCRIPTION);
        }

        private Builder(final int version) {
            this();
            message(MessageFormat.format(MESSAGE_TEMPLATE, version));
        }

        @Override
        protected UnsupportedSchemaVersionException doBuild(final DittoHeaders dittoHeaders,
                @Nullable final String message,
                @Nullable final String description,
                @Nullable final Throwable cause,
                @Nullable final URI href) {
            return new UnsupportedSchemaVersionException(dittoHeaders, message, description, cause, href);
        }

    }

}
