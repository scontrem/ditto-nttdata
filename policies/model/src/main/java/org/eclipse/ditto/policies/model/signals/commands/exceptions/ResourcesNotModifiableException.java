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
package org.eclipse.ditto.policies.model.signals.commands.exceptions;

import java.net.URI;
import java.text.MessageFormat;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.base.model.common.HttpStatus;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeException;
import org.eclipse.ditto.base.model.exceptions.DittoRuntimeExceptionBuilder;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.json.JsonParsableException;
import org.eclipse.ditto.policies.model.PolicyException;
import org.eclipse.ditto.policies.model.PolicyId;

/**
 * Thrown if the {@link org.eclipse.ditto.policies.model.Resources} of a {@link org.eclipse.ditto.policies.model.PolicyEntry}
 * could not be modified because the requester had insufficient permissions.
 */
@Immutable
@JsonParsableException(errorCode = ResourcesNotModifiableException.ERROR_CODE)
public final class ResourcesNotModifiableException extends DittoRuntimeException implements PolicyException {

    /**
     * Error code of this exception.
     */
    public static final String ERROR_CODE = ERROR_CODE_PREFIX + "resources.notmodifiable";

    private static final String MESSAGE_TEMPLATE = "The Resources of the PolicyEntry with Label ''{0}'' on the" +
            " Policy with ID ''{1}'' could not be modified as the requester had insufficient permissions.";

    private static final String DEFAULT_DESCRIPTION = "Check if the ID of the Policy and the PolicyEntry's Label" +
            " of your requested Resources was correct and you have sufficient permissions.";

    private static final long serialVersionUID = 798042490341996073L;

    private ResourcesNotModifiableException(final DittoHeaders dittoHeaders,
            @Nullable final String message,
            @Nullable final String description,
            @Nullable final Throwable cause,
            @Nullable final URI href) {
        super(ERROR_CODE, HttpStatus.FORBIDDEN, dittoHeaders, message, description, cause, href);
    }

    /**
     * A mutable builder for a {@code ResourcesNotModifiableException}.
     *
     * @param policyId the identifier of the Policy.
     * @param label the label of the PolicyEntry.
     * @return the builder.
     */
    public static Builder newBuilder(final PolicyId policyId, final CharSequence label) {
        return new Builder(policyId, label);
    }

    /**
     * Constructs a new {@code ResourcesNotModifiableException} object with given message.
     *
     * @param message detail message. This message can be later retrieved by the {@link #getMessage()} method.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new ResourcesNotModifiableException.
     * @throws NullPointerException if {@code dittoHeaders} is {@code null}.
     */
    public static ResourcesNotModifiableException fromMessage(@Nullable final String message,
            final DittoHeaders dittoHeaders) {
        return DittoRuntimeException.fromMessage(message, dittoHeaders, new Builder());
    }

    /**
     * Constructs a new {@code ResourcesNotModifiableException} object with the exception message extracted from the
     * given JSON object.
     *
     * @param jsonObject the JSON to read the {@link org.eclipse.ditto.base.model.exceptions.DittoRuntimeException.JsonFields#MESSAGE} field from.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new ResourcesNotModifiableException.
     * @throws NullPointerException if any argument is {@code null}.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if this JsonObject did not contain an error message.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * format.
     */
    public static ResourcesNotModifiableException fromJson(final JsonObject jsonObject,
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
     * A mutable builder with a fluent API for a {@link org.eclipse.ditto.policies.model.signals.commands.exceptions.ResourcesNotModifiableException}.
     */
    @NotThreadSafe
    public static final class Builder extends DittoRuntimeExceptionBuilder<ResourcesNotModifiableException> {

        private Builder() {
            description(DEFAULT_DESCRIPTION);
        }

        private Builder(final PolicyId policyId, final CharSequence label) {
            this();
            message(MessageFormat.format(MESSAGE_TEMPLATE, label, String.valueOf(policyId)));
        }

        @Override
        protected ResourcesNotModifiableException doBuild(final DittoHeaders dittoHeaders,
                @Nullable final String message,
                @Nullable final String description,
                @Nullable final Throwable cause,
                @Nullable final URI href) {
            return new ResourcesNotModifiableException(dittoHeaders, message, description, cause, href);
        }

    }

}
