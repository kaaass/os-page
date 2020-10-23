package net.kaaass.ospage;

import javax.swing.*;

public class Entry {
    public static void main(String[] args) {
        // 设置Gtk风格GUI
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // 开始程序逻辑
        var logic = new MainLogic();
        logic.showMainForm();
//        var sc = new Scanner(System.in);
//        var algo = new ImproveClockAlgorithm();
//        var simu = new Simulation();
//        simu.setAlgorithm(algo);
//        while (true) {
//            var req = Simulation.Request.random();
//            System.out.println("> 请求：" + req);
//            simu.addRequest(req);
//            simu.runStep();
//            sc.nextLine();
//        }
    }
}
