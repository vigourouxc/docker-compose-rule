/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 *
 * THIS SOFTWARE CONTAINS PROPRIETARY AND CONFIDENTIAL INFORMATION OWNED BY PALANTIR TECHNOLOGIES INC.
 * UNAUTHORIZED DISCLOSURE TO ANY THIRD PARTY IS STRICTLY PROHIBITED
 *
 * For good and valuable consideration, the receipt and adequacy of which is acknowledged by Palantir and recipient
 * of this file ("Recipient"), the parties agree as follows:
 *
 * This file is being provided subject to the non-disclosure terms by and between Palantir and the Recipient.
 *
 * Palantir solely shall own and hereby retains all rights, title and interest in and to this software (including,
 * without limitation, all patent, copyright, trademark, trade secret and other intellectual property rights) and
 * all copies, modifications and derivative works thereof.  Recipient shall and hereby does irrevocably transfer and
 * assign to Palantir all right, title and interest it may have in the foregoing to Palantir and Palantir hereby
 * accepts such transfer. In using this software, Recipient acknowledges that no ownership rights are being conveyed
 * to Recipient.  This software shall only be used in conjunction with properly licensed Palantir products or
 * services.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.palantir.docker.compose.matchers;

import com.palantir.docker.compose.connection.DockerPort;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.palantir.docker.compose.matchers.AvailablePortMatcher.areAvailable;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;

public class AvailablePortMatcherTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void no_unavailable_ports_is_good() throws Exception {
        List<DockerPort> unavailablePorts = emptyList();
        assertThat(unavailablePorts, areAvailable());
    }

    @Test
    public void some_unavailable_ports_results_in_useful_error_messages() throws Exception {
        List<DockerPort> unavailablePorts = newArrayList(new DockerPort("0.0.0.0", 1234, 1234),
                                                         new DockerPort("1.2.3.4", 2345, 3456));
        exception.expect(AssertionError.class);
        exception.expectMessage("For host with ip address: 0.0.0.0");
        exception.expectMessage("external port '1234' mapped to internal port '1234' was unavailable");
        exception.expectMessage("For host with ip address: 1.2.3.4");
        exception.expectMessage("external port '2345' mapped to internal port '3456' was unavailable");
        assertThat(unavailablePorts, areAvailable());
    }

}