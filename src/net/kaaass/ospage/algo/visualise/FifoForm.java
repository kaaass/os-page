package net.kaaass.ospage.algo.visualise;

import net.kaaass.ospage.algo.FifoAlgorithm;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.simu.PageTable;
import net.kaaass.ospage.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class FifoForm {
    private JTable tableQueue;
    private JPanel mainPanel;
    private JLabel labelInfo;
    private JTable tableInfo;

    private JFrame frame;
    private Vector<String> queueColumns;
    private Vector<Vector<String>> queueData;
    private Vector<Vector<String>> infoData;
    private FifoAlgorithm algo;

    public FifoForm(FifoAlgorithm algo) {
        this.algo = algo;
        this.frame = new JFrame("先进先出算法可视化");
        this.frame.setContentPane(mainPanel);
        this.frame.setLocationRelativeTo(null);
        this.frame.pack();
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public void update(PageTable pageTable) {
        // 信息
        this.infoData.get(0).set(1, String.valueOf(this.algo.getHeadPageId()));
        this.infoData.get(1).set(1, String.valueOf(this.algo.getTailPageId()));
        // 队列
        this.queueData.clear();
        var list = new ArrayList<PageEntry>();
        var cur = algo.getHeadPageId();
        while (cur >= 0) {
            var entry = pageTable.get(cur);
            list.add(entry);
            cur = (Integer) entry.getAttribute(FifoAlgorithm.C_NEXT);
        }
        GuiUtils.mapPageEntry(this.queueData, list, algo.getColumnNames());
        //
        this.tableInfo.updateUI();
        this.tableQueue.updateUI();
    }

    public void close() {
        this.frame.setVisible(false);
    }

    private void createUIComponents() {
        // 信息表
        var columns = new Vector<>() {{
           add("项目");
           add("值");
        }};
        this.infoData = new Vector<>();
        this.infoData.add(new Vector<>(){{
            add("队列头指针");
            add("");
        }});
        this.infoData.add(new Vector<>(){{
            add("队列尾指针");
            add("");
        }});
        this.tableInfo = new JTable(infoData, columns);
        this.tableInfo.setPreferredScrollableViewportSize(new Dimension(400, 50));
        // 队列表
        this.queueColumns = new Vector<>();
        this.queueData = new Vector<>();
        Collections.addAll(queueColumns, PageEntry.COMMON_COLUMNS);
        Collections.addAll(queueColumns, this.algo.getColumnNames());
        this.tableQueue = new JTable(queueData, queueColumns);
        this.tableQueue.setPreferredScrollableViewportSize(new Dimension(400, 200));
    }
}
