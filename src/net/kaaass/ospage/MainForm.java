package net.kaaass.ospage;

import net.kaaass.ospage.simu.IAlgorithm;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.util.GuiUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Vector;

public class MainForm extends JFrame {

    /*
     * 界面控件
     */

    private JTable tableRequest;
    private JTable tablePage;
    private JComboBox<String> comboBoxRW;
    private JTextField textFieldAddress;
    private JButton btnAddRequest;
    private JButton btnRandRequest;
    private JComboBox<String> comboBoxAlgorithmSelect;
    private JButton btnNext;
    private JButton btnAuto;
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
        addListeners();
        // 增加算法
        logic.getAvailableAlgorithms().forEach(algorithm ->
                this.comboBoxAlgorithmSelect.addItem(algorithm.name()));
    }

    /**
     * 添加相关组件的侦听器
     */
    private void addListeners() {
        // 添加请求按钮
        btnAddRequest.addActionListener(e -> {
            // TODO
        });
        // 随机添加请求按钮
        btnRandRequest.addActionListener(e -> {
            // TODO
        });
        // 算法选择框
        comboBoxAlgorithmSelect.addItemListener(e -> {
            int selected = this.comboBoxAlgorithmSelect.getSelectedIndex();
            this.logic.setAlgorithm(this.logic.getAvailableAlgorithms().get(selected));
        });
        // 下一步按钮
        btnNext.addActionListener(e -> {
            // TODO
        });
        // 自动播放按钮
        btnAuto.addActionListener(e -> {
            // TODO
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

    }

    private void createUIComponents() {
        // 请求表
        var requestColumns = new Vector<>() {{
            add("序号");
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
            add("序号");
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
}
