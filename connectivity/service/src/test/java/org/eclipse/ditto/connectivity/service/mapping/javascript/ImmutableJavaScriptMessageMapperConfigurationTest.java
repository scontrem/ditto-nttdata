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
package org.eclipse.ditto.connectivity.service.mapping.javascript;

import static org.mutabilitydetector.unittesting.AllowedReason.provided;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.eclipse.ditto.connectivity.service.mapping.MessageMapperConfiguration;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test for {@link ImmutableJavaScriptMessageMapperConfiguration}.
 */
public final class ImmutableJavaScriptMessageMapperConfigurationTest {

    @Test
    public void assertImmutability() {
        assertInstancesOf(ImmutableJavaScriptMessageMapperConfiguration.class,
                areImmutable(),
                provided(MessageMapperConfiguration.class).isAlsoImmutable());
    }

    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier.forClass(ImmutableJavaScriptMessageMapperConfiguration.class)
                .usingGetClass()
                .verify();
    }

}
