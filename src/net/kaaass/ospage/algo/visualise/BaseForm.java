package net.kaaass.ospage.algo.visualise;

import net.kaaass.ospage.simu.PageEntry;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Vector;

public class BaseForm {

    protected JFrame frame = null;

    public void initialize(String title, JPanel panel) {
        this.frame = new JFrame(title);
        this.frame.setContentPane(panel);
        this.frame.setLocationRelativeTo(null);
        this.frame.pack();
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public void close() {
        this.frame.setVisible(false);
    }

    protected JTable createTable(int width, int height, String[] columns, Vector<Vector<String>> data, boolean common) {
        var colVec = new Vector<>();
        if (common) {
            Collections.addAll(colVec, PageEntry.COMMON_COLUMNS);
        }
        Collections.addAll(colVec, columns);
        var table = new JTable(data, colVec);
        table.setPreferredScrollableViewportSize(new Dimension(width, height));
        return table;
    }
}
