package net.kaaass.ospage;

import net.kaaass.ospage.algo.NurAlgorithm;
import net.kaaass.ospage.simu.Simulation;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var sc = new Scanner(System.in);
        var algo = new NurAlgorithm();
        var simu = new Simulation();
        simu.setAlgorithm(algo);
        while (true) {
            var req = Simulation.Request.random();
            System.out.println("> 请求：" + req);
            simu.addRequest(req);
            simu.runStep();
            sc.nextLine();
        }
    }
}
