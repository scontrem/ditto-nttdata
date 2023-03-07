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
package org.eclipse.ditto.policies.model.enforcers.testbench.scenarios.scenario1;

import org.eclipse.ditto.policies.model.enforcers.testbench.scenarios.Scenario;
import org.eclipse.ditto.policies.model.enforcers.testbench.scenarios.ScenarioSetup;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;


@State(Scope.Benchmark)
public class Scenario1Simple3 implements Scenario1Simple {

    private final ScenarioSetup setup;

    public Scenario1Simple3() {
        setup = Scenario.newScenarioSetup( //
                false, //
                "Subject has READ+WRITE granted on '/'. Subject has WRITE revoked on '/'. Is NOT able to WRITE '/'", //
                getPolicy(), //
                Scenario.newAuthorizationContext(SUBJECT_WRITE_REVOKED), //
                "/", //
                "WRITE");
    }

    @Override
    public ScenarioSetup getSetup() {
        return setup;
    }

}
