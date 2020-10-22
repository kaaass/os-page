package net.kaaass.ospage.algo.visualise;

import net.kaaass.ospage.algo.FifoAlgorithm;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.simu.PageTable;
import net.kaaass.ospage.util.GuiUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

public class FifoForm extends BaseForm {
    private JTable tableQueue;
    private JPanel mainPanel;
    private JTable tableInfo;

    private Vector<Vector<String>> queueData;
    private Vector<Vector<String>> infoData;
    private FifoAlgorithm algo;

    public FifoForm(FifoAlgorithm algo) {
        this.algo = algo;
        this.initialize("先进先出算法可视化", this.mainPanel);
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

    private void createUIComponents() {
        // 信息表
        this.infoData = new Vector<>();
        this.infoData.add(new Vector<>() {{
            add("队列头指针");
            add("");
        }});
        this.infoData.add(new Vector<>() {{
            add("队列尾指针");
            add("");
        }});
        this.tableInfo = createTable(400, 50, new String[]{
                "项目", "值"
        }, this.infoData, false);
        // 队列表
        this.queueData = new Vector<>();
        this.tableQueue = createTable(400, 200, this.algo.getColumnNames(), this.queueData, true);
    }
}
