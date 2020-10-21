package net.kaaass.ospage;

import net.kaaass.ospage.algo.FifoAlgorithm;
import net.kaaass.ospage.simu.Simulation;

public class Main {
    public static void main(String[] args) {
        var fifo = new FifoAlgorithm();
        var simu = new Simulation();
        simu.setAlgorithm(fifo);
        for (int i = 0; i < 10; i++) {
            var req = Simulation.Request.random();
            System.out.println("> 请求：" + req);
            simu.addRequest(req);
            simu.runStep();
        }
    }
}
