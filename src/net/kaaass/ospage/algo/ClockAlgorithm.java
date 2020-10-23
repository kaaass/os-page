package net.kaaass.ospage.algo;

import net.kaaass.ospage.algo.visualise.ClockForm;
import net.kaaass.ospage.simu.*;

/**
 * 时钟算法
 */
public class ClockAlgorithm implements IAlgorithm {

    public static final String C_ACCESS = "访问标志";

    private int clockPtr = -1;
    private ClockForm form = new ClockForm(this);

    @Override
    public void init(PageTable pageTable) {
        this.clockPtr = -1;
        // 初始化页表
        this.resetFlag(pageTable);
        this.clockPtr = -1;
        // 显示可视化窗口
        this.form.show();
    }

    @Override
    public PageEntry retire(PageTable pageTable) {
        PageEntry iter;
        while (!(iter = pageTable.get(this.clockPtr))
                .getAttribute(C_ACCESS).equals(0)) {
            iter.setAttribute(C_ACCESS, 0);
            this.clockPtr = findNext(pageTable, this.clockPtr);
            // 更新窗口
            this.form.update(pageTable);
            try {
                Thread.sleep(Config.getDefault().getClockPlaySpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.clockPtr = findNext(pageTable, this.clockPtr);
        return iter;
    }

    private int findNext(PageTable pageTable, int cur) {
        do {
            cur = (cur + 1) % pageTable.size();
        } while (!pageTable.get(cur).isInMemory());
        return cur;
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

    @Override
    public String name() {
        return "时钟（CLOCK）";
    }

    public int getClockPtr() {
        return clockPtr;
    }
}
