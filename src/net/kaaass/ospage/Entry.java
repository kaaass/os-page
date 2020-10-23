package net.kaaass.ospage;

import javax.swing.*;

public class Entry {
    public static void main(String[] args) {
        // 字体反锯齿
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        // 设置Gtk风格GUI
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // 开始程序逻辑
        var logic = new MainLogic();
        logic.showMainForm();
    }
}
