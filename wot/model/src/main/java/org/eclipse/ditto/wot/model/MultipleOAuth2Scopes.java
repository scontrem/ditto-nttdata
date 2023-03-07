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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.ditto.base.model.json.Jsonifiable;
import org.eclipse.ditto.json.JsonArray;
import org.eclipse.ditto.json.JsonValue;

/**
 * MultipleOAuth2Scopes is a container for multiple {@link SingleOAuth2Scopes}s.
 *
 * @since 2.4.0
 */
public interface MultipleOAuth2Scopes extends OAuth2Scopes, Iterable<SingleOAuth2Scopes>, Jsonifiable<JsonArray> {

    static MultipleOAuth2Scopes fromJson(final JsonArray jsonArray) {
        final List<SingleOAuth2Scopes> singleOAuth2Scopes = jsonArray.stream()
                .filter(JsonValue::isString)
                .map(JsonValue::asString)
                .map(SingleOAuth2Scopes::of)
                .collect(Collectors.toList());
        return of(singleOAuth2Scopes);
    }

    static MultipleOAuth2Scopes of(final Collection<SingleOAuth2Scopes> scopes) {
        return new ImmutableMultipleOAuth2Scopes(scopes);
    }

    default Stream<SingleOAuth2Scopes> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
