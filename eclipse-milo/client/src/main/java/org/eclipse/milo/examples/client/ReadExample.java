/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.ImmutableList;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadExample implements ClientExample {

    public static void main(String[] args) throws Exception {
        ReadExample example = new ReadExample();

        new ClientExampleRunner(example, true).run();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        // synchronous connect
        client.connect().get();        

//        NodeId nodeId = new NodeId(3,"\"AFSM010_WriteRFID\"");
//        DataValue value = client.readValue(0.0, TimestampsToReturn.Both, nodeId).get();
//        System.out.println((Integer)value.getValue().getValue());
        
        // synchronous read request via VariableNode
//        VariableNode node = client.getAddressSpace().createVariableNode(Identifiers.Server_ServerStatus_StartTime);
//        DataValue value = node.readValue().get();
//        logger.info("StartTime={}", value.getValue().getValue());
        // asynchronous read request
        readServerStateAndTime(client).thenAccept(values -> {
            DataValue v0 = values.get(0);
            DataValue v1 = values.get(1);

            logger.info("AFSM010={}", v0.getValue().getValue());
            logger.info("AFSM010_WriteRFID={}", v1.getValue().getValue());
            logger.info("AFSM010_ReadRFID={}", v1.getValue().getValue());

            future.complete(client);
        });
    }

    private CompletableFuture<List<DataValue>> readServerStateAndTime(OpcUaClient client) {
    	NodeId nodeId_Tag1 = new NodeId(2, "FE6.AFSM.AFSM010");
        NodeId nodeId_Tag2 = new NodeId(2, "FE6.AFSM.AFSM010_WriteRFID");
        NodeId nodeId_Tag3 = new NodeId(2, "FE6.AFSM.AFSM010_ReadRFID");
        List<NodeId> nodeIds = ImmutableList.of(nodeId_Tag1,nodeId_Tag2,nodeId_Tag3);
        return client.readValues(0.0, TimestampsToReturn.Both, nodeIds);
    }

}
