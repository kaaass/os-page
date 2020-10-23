package net.kaaass.ospage.algo.visualise;

import net.kaaass.ospage.algo.ClockAlgorithm;
import net.kaaass.ospage.algo.ImproveClockAlgorithm;
import net.kaaass.ospage.simu.Config;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.simu.PageTable;
import net.kaaass.ospage.util.GuiUtils;

import javax.swing.*;
import java.util.Vector;
import java.util.stream.Collectors;

public class ImproveClockForm extends BaseForm {
    private JTable tableClock;
    private JPanel mainPanel;
    private PieFigureComponent compClock;
    private JTextField textFieldSpeed;
    private JButton btnChange;
    private JTextField textFieldRound;
    private JTextField textFieldPtr;

    private Vector<Vector<String>> data;
    private ImproveClockAlgorithm algo;
    private Vector<String> innerText;
    private Vector<String> outerText;

    public ImproveClockForm(ImproveClockAlgorithm algo) {
        this.algo = algo;
        this.initialize("改进时钟算法可视化", this.mainPanel);
        // 绑定按键事件
        btnChange.addActionListener(e -> {
            try {
                int parsed = Integer.parseInt(textFieldSpeed.getText());
                if (parsed < 0)
                    throw new Exception();
                Config.getDefault().setClockPlaySpeed(parsed);
                JOptionPane.showMessageDialog(frame, "修改成功！");
            } catch (Exception ignore) {
                ignore.printStackTrace();
                JOptionPane.showMessageDialog(frame, "请输入整数（>=0）！");
                this.textFieldSpeed.setText(String.valueOf(Config.getDefault().getNurResetInterval()));
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
        this.tableClock.updateUI();
        // 更新图
        this.compClock.setSectorCount(frames.size());
        this.innerText.clear();
        this.outerText.clear();
        int ptr = 0;
        for (var frame : frames) {
            this.innerText.add(String.format("页%d/r=%d\n    m=%d",
                    frame.getLogicId(),
                    frame.getAttribute(ClockAlgorithm.C_ACCESS),
                    (boolean) frame.getAttribute(PageEntry.C_MOD_FLAG) ? 1 : 0));
            this.outerText.add(String.format("框%d", frame.getFrameId()));
            if (frame.getLogicId() == this.algo.getClockPtr())
                ptr = this.innerText.size() - 1;
        }
        this.compClock.setCurrent(ptr);
        this.compClock.repaint();
        // 更新信息域
        this.textFieldRound.setText(String.valueOf(this.algo.getRoundNo()));
        this.textFieldPtr.setText(String.valueOf(this.algo.getClockPtr()));
    }

    private void createUIComponents() {
        // 表格
        this.data = new Vector<>();
        this.tableClock = createTable(400, 300, this.algo.getColumnNames(), this.data, true);
        // 图
        this.innerText = new Vector<>();
        this.outerText = new Vector<>();
        this.compClock = new PieFigureComponent(0, this.innerText, this.outerText);
    }

    @Override
    public void show() {
        super.show();
        this.textFieldSpeed.setText(String.valueOf(Config.getDefault().getClockPlaySpeed()));
    }

}
