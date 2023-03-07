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
package org.eclipse.ditto.base.model.common;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.junit.Test;

/**
 * Unit test for {@link org.eclipse.ditto.base.model.common.ConditionChecker}.
 */
public final class ConditionCheckerTest {


    @Test
    public void assertImmutability() {
        assertInstancesOf(ConditionChecker.class, areImmutable());
    }


    @Test(expected = IllegalArgumentException.class)
    public void checkArgumentWithPredicate() {
        final String argument = "";
        ConditionChecker.checkArgument(argument, s -> !s.isEmpty());
    }

}
