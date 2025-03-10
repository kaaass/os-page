package net.kaaass.ospage.algo;

import net.kaaass.ospage.algo.visualise.FifoForm;
import net.kaaass.ospage.simu.IAlgorithm;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.simu.PageTable;
import net.kaaass.ospage.simu.Simulation;

/**
 * 先进先出算法
 */
public class FifoAlgorithm implements IAlgorithm {

    public static final String C_NEXT = "队列指针";

    private final FifoForm form = new FifoForm(this);

    /**
     * 队首
     */
    private int headPageId = -1;
    private int tailPageId = -1;

    @Override
    public void init(PageTable pageTable) {
        this.headPageId = -1;
        this.tailPageId = -1;
        // 通常用于初始化页表所需要的字段
        for (int i = 0; i < pageTable.size(); i++) {
            pageTable.get(i).setAttribute(C_NEXT, -1);
        }
        // 显示可视化窗口
        this.form.show();
    }

    @Override
    public PageEntry retire(PageTable pageTable) {
        if (this.headPageId < 0)
            return null;
        var retire = pageTable.get(this.headPageId);
        this.headPageId = (Integer) retire.getAttribute(C_NEXT);
        retire.setAttribute(C_NEXT, -1);
        return retire;
    }

    @Override
    public void onAlloc(PageTable pageTable, PageEntry pageEntry) {
        var id = pageEntry.getLogicId();
        if (this.headPageId < 0) {
            this.headPageId = id;
        }
        if (this.tailPageId >= 0) {
            // 存在页面链，则尾部拉链
            pageTable.get(this.tailPageId).setAttribute(C_NEXT, id);
        }
        this.tailPageId = id;
        pageTable.get(id).setAttribute(C_NEXT, -1);
    }

    @Override
    public void onAccess(PageTable pageTable, Simulation.Request request, PageEntry dstPage) {
        // 忽略
    }

    @Override
    public void onDraw(Simulation simulation) {
        this.form.update(simulation.getPageTable());
    }

    @Override
    public void onClose() {
        // 关闭窗口
        this.form.close();
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{C_NEXT};
    }

    @Override
    public String name() {
        return "先进先出（FIFO）";
    }

    public int getHeadPageId() {
        return headPageId;
    }

    public int getTailPageId() {
        return tailPageId;
    }
}
