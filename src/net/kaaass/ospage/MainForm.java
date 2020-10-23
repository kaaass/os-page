package net.kaaass.ospage;

import net.kaaass.ospage.simu.Address;
import net.kaaass.ospage.simu.Config;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.simu.Simulation;
import net.kaaass.ospage.util.GuiUtils;

import javax.management.ValueExp;
import javax.swing.*;
import java.util.Collections;
import java.util.Vector;
import java.util.stream.Collectors;

public class MainForm extends JFrame {

    /*
     * 界面控件
     */

    private JTable tableRequest;
    private JTable tablePage;
    private JComboBox<Simulation.AccessType> comboBoxRW;
    private JTextField textFieldAddress;
    private JButton btnAddRequest;
    private JButton btnRandRequest;
    private JComboBox<String> comboBoxAlgorithmSelect;
    private JButton btnNext;
    private JButton btnJump5;
    private JTable tableAddressMap;
    private JList<String> listLog;
    private JButton btnReset;
    private JButton btnAbout;
    private JPanel mainPanel;

    /*
     * GUI数据
     */

    private Vector<Vector<String>> dataTableRequest;
    private Vector<Vector<String>> dataTablePage;
    private Vector<Vector<String>> dataTableAddrMap;
    private Vector<String> dataLog;

    /*
     * 逻辑对象
     */

    private final MainLogic logic;

    public MainForm(MainLogic logic) {
        super("2020操作系统课程设计：基于工作集模型的局部页面置换算法");
        this.logic = logic;
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    public void init() {
        addListeners();
        // 增加下拉框内容
        this.logic.getAvailableAlgorithms().forEach(algorithm ->
                this.comboBoxAlgorithmSelect.addItem(algorithm.name()));
        this.comboBoxRW.addItem(Simulation.AccessType.READ);
        this.comboBoxRW.addItem(Simulation.AccessType.WRITE);
    }

    /**
     * 添加相关组件的侦听器
     */
    private void addListeners() {
        // 添加请求按钮
        btnAddRequest.addActionListener(e -> {
            // 解析参数
            var type = (Simulation.AccessType) this.comboBoxRW.getSelectedItem();
            int address = 0;
            try {
                address = Integer.parseInt(this.textFieldAddress.getText(), 16);
                if (address < 0 || address >= (1 << 16))
                    throw new Exception();
            } catch (Exception ignore) {
                JOptionPane.showMessageDialog(this,
                        String.format("请输入十六进制数（0-%x）", (1 << 16) - 1));
                return;
            }
            // 添加
            this.logic.addRequest(type, address);
            this.update();
        });
        // 随机添加请求按钮
        btnRandRequest.addActionListener(e -> {
            this.logic.addRandomRequests();
            this.update();
        });
        // 算法选择框
        comboBoxAlgorithmSelect.addItemListener(e -> {
            int selected = this.comboBoxAlgorithmSelect.getSelectedIndex();
            this.logic.setAlgorithm(this.logic.getAvailableAlgorithms().get(selected));
        });
        // 下一步按钮
        btnNext.addActionListener(e -> {
            if (this.logic.getSimulation().getRequestQueue().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请求队列空！");
                return;
            }
            this.logic.getSimulation().runStep();
            this.update();
        });
        // 跳过五步按钮
        btnJump5.addActionListener(e -> {
            if (this.logic.getSimulation().getRequestQueue().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请求队列空！");
                return;
            }
            for (int i = 0; i < 5; i++) {
                this.logic.getSimulation().runStep();
            }
            this.update();
        });
        // 重置按钮
        btnReset.addActionListener(e -> {
            this.logic.reset();
            this.update();
            // 清除日志
            this.dataTableAddrMap.clear();
            this.dataLog.clear();
            this.tableAddressMap.updateUI();
            this.listLog.updateUI();
        });
        // 关于按钮
        btnAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "本程序为吉林大学软件学院2018级操作系统课程设计（荣誉课）的结果物\n" +
                            "作者：@KAAAsS @KevinAxel");
        });
    }

    public void update() {
        // 请求列表
        this.dataTableRequest.clear();
        this.logic.getSimulation().getRequestQueue().stream()
                .map(request -> {
                    var row = new Vector<String>();
                    row.add(request.type.toString());
                    row.add(request.address.hexString());
                    return row;
                })
                .forEach(dataTableRequest::add);
        this.tableRequest.updateUI();
        // 页表
        var pageTable = this.logic.getSimulation().getPageTable();
        this.dataTablePage.clear();
        GuiUtils.mapPageEntry(this.dataTablePage, pageTable.stream().collect(Collectors.toList()), new String[0]);
        this.tablePage.updateUI();
    }

    private void createUIComponents() {
        // 请求表
        var requestColumns = new Vector<>() {{
            add("类型");
            add("请求逻辑地址");
        }};
        this.dataTableRequest = new Vector<>();
        this.tableRequest = new JTable(dataTableRequest, requestColumns);
        GuiUtils.autoFitTableColumns(this.tableRequest);
        // 页表
        var pageColumns = new Vector<>();
        Collections.addAll(pageColumns, PageEntry.COMMON_COLUMNS);
        this.dataTablePage = new Vector<>();
        this.tablePage = new JTable(dataTablePage, pageColumns);
        // 地址映射表
        var addrMapColumns = new Vector<>() {{
            add("源页号");
            add("页框号");
            add("地址映射（逻辑->物理）");
        }};
        this.dataTableAddrMap = new Vector<>();
        this.tableAddressMap = new JTable(dataTableAddrMap, addrMapColumns);
        GuiUtils.autoFitTableColumns(this.tableAddressMap);
        // 日志
        this.dataLog = new Vector<>();
        this.listLog = new JList<>(dataLog);
    }

    public void addLogText(String content) {
        this.dataLog.add(content);
        this.listLog.updateUI();
        // 滚动到最后
        this.listLog.ensureIndexIsVisible(this.dataLog.size() - 1);
    }

    public void addMapping(Address source, Address dest) {
        var row = new Vector<String>();
        row.add(String.valueOf(source.prefix()));
        row.add(String.valueOf(dest.prefix()));
        row.add(String.format("%s -> %s", source.hexString(), dest.hexString()));
        this.dataTableAddrMap.add(row);
        this.tableAddressMap.updateUI();
        // 滚动到最后
        this.tableAddressMap.scrollRectToVisible(this.tableAddressMap.getCellRect(
                this.tableAddressMap.getRowCount() - 1, 0, true));
    }
}
