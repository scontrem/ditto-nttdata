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
package org.eclipse.ditto.rql.query.criteria;

import javax.annotation.Nullable;

import org.eclipse.ditto.base.model.common.LikeHelper;
import org.eclipse.ditto.rql.query.criteria.visitors.PredicateVisitor;

/**
 * Like predicate.
 */
final class LikePredicateImpl extends AbstractSinglePredicate {

    public LikePredicateImpl(@Nullable final Object value) {
        super(value);
    }

    @Nullable
    private String convertToRegexSyntaxAndGetOption() {
        final Object value = getValue();
        if (value != null) {
            return LikeHelper.convertToRegexSyntax(value.toString());
        } else {
            return null;
        }
    }

    @Override
    public <T> T accept(final PredicateVisitor<T> visitor) {
        return visitor.visitLike(convertToRegexSyntaxAndGetOption());
    }
}
