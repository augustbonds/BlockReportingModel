package io.hops.blockreportsimulation.components;

import io.hops.blockreportsimulation.ports.NameNodeDataNodePort;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;

public class Parent extends ComponentDefinition {
    private Component nameNode = create(NameNodeComponent.class, Init.NONE);
    private Component dataNode = create(DataNodeComponent.class, Init.NONE);
    private Component timer = create(JavaTimer.class, Init.NONE);

    {
        connect(dataNode.getNegative(NameNodeDataNodePort.class), nameNode.getPositive(NameNodeDataNodePort.class), Channel.TWO_WAY);
        connect(dataNode.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
    }
}
