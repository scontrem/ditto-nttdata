/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.policies.service.persistence.actors.strategies.commands;

import static org.eclipse.ditto.base.model.common.ConditionChecker.checkNotNull;

import java.util.Optional;

import javax.annotation.Nullable;

import org.eclipse.ditto.base.model.entity.metadata.Metadata;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.headers.entitytag.EntityTag;
import org.eclipse.ditto.internal.utils.persistentactors.results.Result;
import org.eclipse.ditto.internal.utils.persistentactors.results.ResultFactory;
import org.eclipse.ditto.json.JsonField;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.policies.api.PoliciesValidator;
import org.eclipse.ditto.policies.model.Label;
import org.eclipse.ditto.policies.model.Policy;
import org.eclipse.ditto.policies.model.PolicyEntry;
import org.eclipse.ditto.policies.model.PolicyId;
import org.eclipse.ditto.policies.model.Subject;
import org.eclipse.ditto.policies.model.signals.commands.PolicyCommandSizeValidator;
import org.eclipse.ditto.policies.model.signals.commands.modify.ModifySubject;
import org.eclipse.ditto.policies.model.signals.commands.modify.ModifySubjectResponse;
import org.eclipse.ditto.policies.model.signals.events.PolicyEvent;
import org.eclipse.ditto.policies.model.signals.events.SubjectCreated;
import org.eclipse.ditto.policies.model.signals.events.SubjectModified;
import org.eclipse.ditto.policies.service.common.config.PolicyConfig;

/**
 * This strategy handles the {@link org.eclipse.ditto.policies.model.signals.commands.modify.ModifySubject} command.
 */
final class ModifySubjectStrategy extends AbstractPolicyCommandStrategy<ModifySubject, PolicyEvent<?>> {

    ModifySubjectStrategy(final PolicyConfig policyConfig) {
        super(ModifySubject.class, policyConfig);
    }

    @Override
    protected Result<PolicyEvent<?>> doApply(final Context<PolicyId> context,
            @Nullable final Policy policy,
            final long nextRevision,
            final ModifySubject command,
            @Nullable final Metadata metadata) {

        final Policy nonNullPolicy = checkNotNull(policy, "policy");
        final PolicyId policyId = context.getState();
        final Label label = command.getLabel();
        final Subject subject = command.getSubject();
        final DittoHeaders commandHeaders = command.getDittoHeaders();

        final Optional<PolicyEntry> optionalEntry = nonNullPolicy.getEntryFor(label);
        if (optionalEntry.isPresent()) {

            final JsonPointer resourcesPointer = Policy.JsonFields.ENTRIES.getPointer()
                    .append(JsonPointer.of(label))
                    .append(PolicyEntry.JsonFields.SUBJECTS.getPointer())
                    .append(JsonPointer.of(subject.getId()));
            PolicyCommandSizeValidator.getInstance()
                    .ensureValidSize(nonNullPolicy, JsonField.newInstance(resourcesPointer, subject.toJson()),
                            () -> commandHeaders);

            final PolicyEntry policyEntry = optionalEntry.get();
            final Subject adjustedSubject = potentiallyAdjustSubject(subject);
            final ModifySubject adjustedCommand = ModifySubject.of(
                    command.getEntityId(), command.getLabel(), adjustedSubject, commandHeaders);

            final Policy newPolicy = nonNullPolicy.setSubjectFor(label, adjustedSubject);

            final Optional<Result<PolicyEvent<?>>> alreadyExpiredSubject =
                    checkForAlreadyExpiredSubject(newPolicy, commandHeaders, command);
            if (alreadyExpiredSubject.isPresent()) {
                return alreadyExpiredSubject.get();
            }

            final PoliciesValidator validator = PoliciesValidator.newInstance(newPolicy);

            if (validator.isValid()) {
                final PolicyEvent<?> event;
                final ModifySubjectResponse rawResponse;

                if (policyEntry.getSubjects().getSubject(adjustedSubject.getId()).isPresent()) {
                    rawResponse = ModifySubjectResponse.modified(policyId, label, adjustedSubject.getId(),
                            commandHeaders);
                    event = SubjectModified.of(policyId, label, adjustedSubject, nextRevision, getEventTimestamp(),
                            commandHeaders, metadata);
                } else {
                    rawResponse = ModifySubjectResponse.created(policyId, label, adjustedSubject, commandHeaders);
                    event = SubjectCreated.of(policyId, label, adjustedSubject, nextRevision, getEventTimestamp(),
                            commandHeaders, metadata);
                }
                return ResultFactory.newMutationResult(adjustedCommand, event,
                        appendETagHeaderIfProvided(adjustedCommand, rawResponse, nonNullPolicy));
            } else {
                return ResultFactory.newErrorResult(
                        policyEntryInvalid(policyId, label, validator.getReason().orElse(null), commandHeaders),
                        command);
            }
        } else {
            return ResultFactory.newErrorResult(policyEntryNotFound(policyId, label, command.getDittoHeaders()),
                    command);
        }
    }

    @Override
    public Optional<EntityTag> previousEntityTag(final ModifySubject command, @Nullable final Policy previousEntity) {
        return Optional.ofNullable(previousEntity)
                .flatMap(p -> p.getEntryFor(command.getLabel()))
                .flatMap(entry -> entry.getSubjects().getSubject(command.getSubject().getId()))
                .flatMap(EntityTag::fromEntity);
    }

    @Override
    public Optional<EntityTag> nextEntityTag(final ModifySubject command, @Nullable final Policy newEntity) {
        return Optional.of(command.getSubject()).flatMap(EntityTag::fromEntity);
    }
}
