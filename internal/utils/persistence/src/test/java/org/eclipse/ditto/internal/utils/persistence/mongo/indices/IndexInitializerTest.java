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
package org.eclipse.ditto.internal.utils.persistence.mongo.indices;

import static org.mutabilitydetector.unittesting.AllowedReason.provided;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.junit.Test;

import akka.stream.Materializer;

/**
 * Unit test for {@link IndexInitializer}.
 */
public class IndexInitializerTest {

    @Test
    public void assertImmutability() {
        assertInstancesOf(IndexInitializer.class, areImmutable(),
                provided(Materializer.class, IndexOperations.class).areAlsoImmutable());
    }
}
