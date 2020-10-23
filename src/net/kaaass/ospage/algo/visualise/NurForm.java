package net.kaaass.ospage.algo.visualise;

import net.kaaass.ospage.algo.NurAlgorithm;
import net.kaaass.ospage.simu.Config;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.simu.PageTable;
import net.kaaass.ospage.util.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.stream.Collectors;

public class NurForm extends BaseForm {
    private JTable tableFrame;
    private JPanel mainPanel;
    private JTextField textFieldInternal;
    private JTextField textFieldCount;
    private JButton btnChangeInternal;
    private NurAlgorithm algo;
    private Vector<Vector<String>> data;

    public NurForm(NurAlgorithm algo) {
        this.algo = algo;
        this.initialize("最近不用的先淘汰算法可视化", this.mainPanel);
        // 更改周期
        btnChangeInternal.addActionListener(e -> {
            try {
                int parsed = Integer.parseInt(textFieldInternal.getText());
                Config.getDefault().setNurResetInterval(parsed);
                JOptionPane.showMessageDialog(frame, "修改成功！");
            } catch (Exception ignore) {
                ignore.printStackTrace();
                JOptionPane.showMessageDialog(frame, "请输入整数！");
                this.textFieldInternal.setText(String.valueOf(Config.getDefault().getNurResetInterval()));
            }
        });
    }

    public void update(PageTable pageTable) {
        // 更新表格
        var frames = pageTable.stream()
                .filter(PageEntry::isInMemory)
                .collect(Collectors.toList());
        this.data.clear();
        GuiUtils.mapPageEntry(this.data, frames, algo.getColumnNames());
        this.tableFrame.updateUI();
        // 更新计数
        this.textFieldCount.setText(String.valueOf(algo.getAccessCount() + 1));
    }

    private void createUIComponents() {
        this.data = new Vector<>();
        this.tableFrame = createTable(400, 300, this.algo.getColumnNames(), this.data, true);
    }

    @Override
    public void show() {
        super.show();
        this.textFieldInternal.setText(String.valueOf(Config.getDefault().getNurResetInterval()));
    }

}
