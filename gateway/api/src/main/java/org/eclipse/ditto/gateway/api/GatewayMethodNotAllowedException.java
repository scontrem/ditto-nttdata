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
package org.eclipse.ditto.gateway.api;

import java.net.URI;
import java.text.MessageFormat;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.base.model.common.HttpStatus;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeException;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeExceptionBuilder;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.json.JsonParsableException;
import org.eclipse.ditto.json.JsonObject;

/**
 * This exception indicates a requested HTTP method was not allowed on a API Gateway resource.
 */
@Immutable
@JsonParsableException(errorCode = GatewayMethodNotAllowedException.ERROR_CODE)
public final class GatewayMethodNotAllowedException extends DittoRuntimeException implements GatewayException {

    /**
     * Error code of this exception.
     */
    public static final String ERROR_CODE = ERROR_CODE_PREFIX + "method.notallowed";

    private static final String MESSAGE_TEMPLATE = "The provided HTTP method ''{0}'' is not allowed on this resource.";

    private static final String DEFAULT_DESCRIPTION = "Check if you used the correct resource and method combination.";

    private static final long serialVersionUID = -4940757644888672775L;

    private GatewayMethodNotAllowedException(final DittoHeaders dittoHeaders,
            @Nullable final String message,
            @Nullable final String description,
            @Nullable final Throwable cause,
            @Nullable final URI href) {
        super(ERROR_CODE, HttpStatus.METHOD_NOT_ALLOWED, dittoHeaders, message, description, cause, href);
    }

    /**
     * A mutable builder for a {@code GatewayMethodNotAllowedException}.
     *
     * @param httpMethod the HTTP method which was used but not allowed.
     * @return the builder.
     */
    public static Builder newBuilder(final String httpMethod) {
        return new Builder(httpMethod);
    }

    /**
     * Constructs a new {@code GatewayMethodNotAllowedException} object with given message.
     *
     * @param message detail message. This message can be later retrieved by the {@link #getMessage()} method.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new GatewayMethodNotAllowedException.
     * @throws NullPointerException if {@code dittoHeaders} is {@code null}.
     */
    public static GatewayMethodNotAllowedException fromMessage(@Nullable final String message,
            final DittoHeaders dittoHeaders) {
        return DittoRuntimeException.fromMessage(message, dittoHeaders, new Builder());
    }

    /**
     * Constructs a new {@code GatewayMethodNotAllowedException} object with the exception message extracted from the given
     * JSON object.
     *
     * @param jsonObject the JSON to read the {@link org.eclipse.ditto.base.model.exceptions.DittoRuntimeException.JsonFields#MESSAGE} field from.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new GatewayMethodNotAllowedException.
     * @throws NullPointerException if any argument is {@code null}.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if this JsonObject did not contain an error message.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * format.
     */
    public static GatewayMethodNotAllowedException fromJson(final JsonObject jsonObject,
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
     * A mutable builder with a fluent API for a {@link GatewayMethodNotAllowedException}.
     */
    @NotThreadSafe
    public static final class Builder extends DittoRuntimeExceptionBuilder<GatewayMethodNotAllowedException> {

        private Builder() {
            description(DEFAULT_DESCRIPTION);
        }

        private Builder(final String httpMethod) {
            this();
            message(MessageFormat.format(MESSAGE_TEMPLATE, httpMethod));
        }

        @Override
        protected GatewayMethodNotAllowedException doBuild(final DittoHeaders dittoHeaders,
                @Nullable final String message,
                @Nullable final String description,
                @Nullable final Throwable cause,
                @Nullable final URI href) {
            return new GatewayMethodNotAllowedException(dittoHeaders, message, description, cause, href);
        }
    }
}
