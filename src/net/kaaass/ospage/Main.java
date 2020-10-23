package net.kaaass.ospage;

import net.kaaass.ospage.algo.ClockAlgorithm;
import net.kaaass.ospage.algo.ImproveClockAlgorithm;
import net.kaaass.ospage.simu.Simulation;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        var sc = new Scanner(System.in);
        var algo = new ImproveClockAlgorithm();
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
