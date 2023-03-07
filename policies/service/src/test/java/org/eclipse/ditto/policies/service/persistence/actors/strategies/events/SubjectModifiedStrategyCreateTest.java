/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.policies.service.persistence.actors.strategies.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.ditto.policies.service.persistence.TestConstants.Policy.ADDITIONAL_SUPPORT_SUBJECT;
import static org.eclipse.ditto.policies.service.persistence.TestConstants.Policy.FEATURES_RESOURCE_KEY;
import static org.eclipse.ditto.policies.service.persistence.TestConstants.Policy.READ_WRITE_REVOKED;
import static org.eclipse.ditto.policies.service.persistence.TestConstants.Policy.SUPPORT_LABEL;
import static org.eclipse.ditto.policies.service.persistence.TestConstants.Policy.SUPPORT_SUBJECT_ID;

import java.time.Instant;

import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.policies.model.Label;
import org.eclipse.ditto.policies.model.PoliciesModelFactory;
import org.eclipse.ditto.policies.model.Policy;
import org.eclipse.ditto.policies.model.PolicyEntry;
import org.eclipse.ditto.policies.model.PolicyId;
import org.eclipse.ditto.policies.model.Subjects;
import org.eclipse.ditto.policies.model.signals.events.SubjectModified;

/**
 * Tests {@link org.eclipse.ditto.policies.service.persistence.actors.strategies.events.SubjectModifiedStrategy} with newly created Label.
 */
public class SubjectModifiedStrategyCreateTest extends AbstractPolicyEventStrategyTest<SubjectModified> {

    private static final Label NEW_LABEL = Label.of("new");
    private static final Subjects SUBJECTS = Subjects.newInstance(ADDITIONAL_SUPPORT_SUBJECT);

    @Override
    SubjectModifiedStrategy getStrategyUnderTest() {
        return new SubjectModifiedStrategy();
    }

    @Override
    SubjectModified getPolicyEvent(final Instant instant, final Policy policy) {
        final PolicyId policyId = policy.getEntityId().orElseThrow();
        return SubjectModified.of(policyId, NEW_LABEL, ADDITIONAL_SUPPORT_SUBJECT, 10L, instant, DittoHeaders.empty(),
                METADATA);
    }

    @Override
    protected void additionalAssertions(final Policy policyWithEventApplied) {
        // existing label is not touched
        assertThat(policyWithEventApplied.getEffectedPermissionsFor(SUPPORT_LABEL, SUPPORT_SUBJECT_ID,
                FEATURES_RESOURCE_KEY)).contains(READ_WRITE_REVOKED);

        // new label with subject and empty resources is created
        assertThat(policyWithEventApplied.getEntryFor(NEW_LABEL).map(PolicyEntry::getSubjects)).contains(SUBJECTS);
        assertThat(policyWithEventApplied.getEntryFor(NEW_LABEL).map(PolicyEntry::getResources)).contains(
                PoliciesModelFactory.emptyResources());
    }
}
