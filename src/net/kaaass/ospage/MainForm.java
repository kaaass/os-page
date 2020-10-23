package net.kaaass.ospage;

import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.util.GuiUtils;

import javax.swing.*;
import java.util.Collections;
import java.util.Vector;

public class MainForm extends JFrame {

    /*
     * 界面控件
     */

    private JTable tableRequest;
    private JTable tablePage;
    private JComboBox comboBoxRW;
    private JTextField textFieldAddress;
    private JButton btnAddRequest;
    private JButton btnRandRequest;
    private JComboBox comboBoxAlgorithmSelect;
    private JButton btnNext;
    private JButton btnAuto;
    private JTable tableAddressMap;
    private JList listLog;
    private JButton btnReset;
    private JButton btnAbout;
    private JPanel mainPanel;

    /*
     * 表格数据
     */

    private Vector<Vector<String>> dataTableRequest;
    private Vector<Vector<String>> dataTablePage;
    private Vector<Vector<String>> dataTableAddrMap;

    /*
     * 逻辑对象
     */

    private final MainLogic mainLogic;

    public MainForm(MainLogic mainLogic) {
        super("2020操作系统课程设计：基于工作集模型的局部页面置换算法");
        this.mainLogic = mainLogic;
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        addListeners();
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
        comboBoxAlgorithmSelect.addActionListener(e -> {
            // TODO
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
            // TODO
        });
        // 关于按钮
        btnAbout.addActionListener(e -> {
            // TODO
        });
    }

    private void createUIComponents() {
        // 请求表
        var requestColumns = new Vector<>() {{
            add("序号");
            add("类型");
            add("请求逻辑地址");
        }};
        this.tableRequest = new JTable(dataTableRequest, requestColumns);
        GuiUtils.autoFitTableColumns(this.tableRequest);
        // 页表
        var pageColumns = new Vector<>();
        Collections.addAll(pageColumns, PageEntry.COMMON_COLUMNS);
        this.tablePage = new JTable(dataTablePage, pageColumns);
        // 地址映射表
        var addrMapColumns = new Vector<>() {{
            add("序号");
            add("源页号");
            add("页框号");
            add("地址映射（逻辑->物理）");
        }};
        this.tableAddressMap = new JTable(dataTableAddrMap, addrMapColumns);
        GuiUtils.autoFitTableColumns(this.tableAddressMap);
    }
}
