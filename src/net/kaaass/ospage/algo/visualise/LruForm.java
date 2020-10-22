package net.kaaass.ospage.algo.visualise;

import net.kaaass.ospage.algo.LruAlgorithm;
import net.kaaass.ospage.simu.PageTable;
import net.kaaass.ospage.util.GuiUtils;

import javax.swing.*;
import java.util.Vector;

public class LruForm extends BaseForm {
    private JTable tableStack;
    private JPanel mainPanel;
    private Vector<Vector<String>> data;
    private LruAlgorithm algo;

    public LruForm(LruAlgorithm algo) {
        this.algo = algo;
        this.initialize("使用过最久的先淘汰算法可视化", this.mainPanel);
    }

    public void update(PageTable pageTable) {
        this.data.clear();
        GuiUtils.mapPageEntry(this.data, this.algo.getStack(), algo.getColumnNames());
        this.tableStack.updateUI();
    }

    private void createUIComponents() {
        this.data = new Vector<>();
        this.tableStack = createTable(400, 300, this.algo.getColumnNames(), this.data, true);
    }
}
