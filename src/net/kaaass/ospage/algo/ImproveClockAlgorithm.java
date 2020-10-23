package net.kaaass.ospage.algo;

import net.kaaass.ospage.algo.visualise.ImproveClockForm;
import net.kaaass.ospage.simu.*;

/**
 * 改进的时钟算法
 */
public class ImproveClockAlgorithm implements IAlgorithm {

    public static final String C_ACCESS = "访问标志";

    private int clockPtr = -1;
    private int roundNo = 1;
    private ImproveClockForm form = new ImproveClockForm(this);

    @Override
    public void init(PageTable pageTable) {
        // 初始化页表
        this.resetFlag(pageTable);
        this.clockPtr = -1;
        // 显示可视化窗口
        this.form.show();
    }

    @Override
    public PageEntry retire(PageTable pageTable) {
        PageEntry result;
        this.roundNo = 1;
        result = round(pageTable, false);
        if (result == null) {
            this.roundNo = 2;
            updateAndWait(pageTable);
            result = round(pageTable, true);
        }
        if (result == null) {
            this.roundNo = 3;
            updateAndWait(pageTable);
            result = round(pageTable, false);
        }
        if (result == null) {
            this.roundNo = 4;
            updateAndWait(pageTable);
            result = round(pageTable, true);
        }
        return result;
    }

    private PageEntry round(PageTable pageTable, boolean m) {
        var end = this.clockPtr;
        PageEntry iter;
        while (!((iter = pageTable.get(this.clockPtr))
                .getAttribute(C_ACCESS).equals(0) &&
                iter.getAttribute(PageEntry.C_MOD_FLAG).equals(m))) {
            // 设置访问位
            if (m)
                iter.setAttribute(C_ACCESS, 0);
            // 未找到，寻找下一个
            this.clockPtr = findNext(pageTable, this.clockPtr);
            // 更新窗口
            updateAndWait(pageTable);
            // 判断一周，则退出
            if (this.clockPtr == end)
                return null;
        }
        // 找到了也移动到下一个
        this.clockPtr = findNext(pageTable, this.clockPtr);
        return iter;
    }

    private int findNext(PageTable pageTable, int cur) {
        do {
            cur = (cur + 1) % pageTable.size();
        } while (!pageTable.get(cur).isInMemory());
        return cur;
    }

    private void updateAndWait(PageTable pageTable) {
        this.form.update(pageTable);
        try {
            Thread.sleep(Config.getDefault().getClockPlaySpeed());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAlloc(PageTable pageTable, PageEntry pageEntry) {
        pageEntry.setAttribute(C_ACCESS, 1);
        if (this.clockPtr < 0)
            this.clockPtr = pageEntry.getLogicId();
    }

    @Override
    public void onAccess(PageTable pageTable, Simulation.Request request, PageEntry dstPage) {
        dstPage.setAttribute(C_ACCESS, 1);
    }

    private void resetFlag(PageTable pageTable) {
        pageTable.stream().forEach(page -> page.setAttribute(C_ACCESS, 0));
    }

    @Override
    public void onDraw(Simulation simulation) {
        this.form.update(simulation.getPageTable());
    }

    @Override
    public void onClose() {
        this.form.close();
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{C_ACCESS};
    }

    public int getClockPtr() {
        return clockPtr;
    }

    public int getRoundNo() {
        return roundNo;
    }
}
