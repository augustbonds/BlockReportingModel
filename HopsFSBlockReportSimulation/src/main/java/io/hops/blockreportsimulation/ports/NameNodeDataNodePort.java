package io.hops.blockreportsimulation.ports;

import io.hops.blockreportsimulation.events.*;
import se.sics.kompics.PortType;

public class NameNodeDataNodePort extends PortType {
    {
        request(BadBlocks.class);
        request(BlocksReceivedAndDeleted.class);
        request(BlockReport.class);
    }

}
