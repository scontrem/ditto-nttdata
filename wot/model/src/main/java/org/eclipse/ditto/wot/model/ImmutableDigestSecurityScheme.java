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
package org.eclipse.ditto.wot.model;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.json.JsonObject;

/**
 * Immutable implementation of {@link DigestSecurityScheme}.
 */
@Immutable
final class ImmutableDigestSecurityScheme extends AbstractSecurityScheme implements DigestSecurityScheme {

    ImmutableDigestSecurityScheme(final String securitySchemeName, final JsonObject wrappedObject) {
        super(securitySchemeName, wrappedObject);
    }

    @Override
    public Optional<Qop> getQop() {
        return wrappedObject.getValue(JsonFields.QOP)
                .flatMap(Qop::forName);
    }

    @Override
    public Optional<SecuritySchemeIn> getIn() {
        return wrappedObject.getValue(JsonFields.IN)
                .flatMap(SecuritySchemeIn::forName);
    }

    @Override
    public Optional<String> getName() {
        return wrappedObject.getValue(JsonFields.NAME);
    }

    @Override
    protected SecurityScheme createInstance(final JsonObject newWrapped) {
        return new ImmutableDigestSecurityScheme(getSecuritySchemeName(), newWrapped);
    }

    @Override
    protected boolean canEqual(@Nullable final Object other) {
        return other instanceof ImmutableDigestSecurityScheme;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + super.toString() + "]";
    }
}
