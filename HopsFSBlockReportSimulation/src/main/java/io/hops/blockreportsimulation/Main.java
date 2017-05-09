package io.hops.blockreportsimulation;

import io.hops.blockreportsimulation.components.Parent;
import se.sics.kompics.Kompics;

public class Main {
    public static void main(String[] args) {

        Kompics.createAndStart(Parent.class);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException ex) {
            System.exit(1);
        }
        Kompics.shutdown();
        System.exit(0);
    }
}
