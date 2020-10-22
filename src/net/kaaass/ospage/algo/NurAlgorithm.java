package net.kaaass.ospage.algo;

import net.kaaass.ospage.algo.visualise.BaseForm;
import net.kaaass.ospage.algo.visualise.NurForm;
import net.kaaass.ospage.simu.*;

/**
 * 最近不用的先淘汰算法
 */
public class NurAlgorithm implements IAlgorithm {

    public static final String C_ACCESS = "访问标志";

    private int accessCount = -1;
    private NurForm form = new NurForm(this);

    @Override
    public void init(PageTable pageTable) {
        // 初始化页表
        this.resetFlag(pageTable);
        this.accessCount = -1;
        // 显示可视化窗口
        this.form.show();
    }

    @Override
    public PageEntry retire(PageTable pageTable) {
        // 找任意访问位为0
        var candidate = pageTable.stream()
                .filter(PageEntry::isInMemory)
                .filter(page -> page.getAttribute(C_ACCESS).equals(0))
                .findFirst();
        // 若找不到，则取任意一页
        var result = candidate.or(() -> pageTable.stream()
                .filter(PageEntry::isInMemory)
                .findFirst());
        return result.orElse(null);
    }

    @Override
    public void onAlloc(PageTable pageTable, PageEntry pageEntry) {
        pageEntry.setAttribute(C_ACCESS, 0);
    }

    @Override
    public void onAccess(PageTable pageTable, Simulation.Request request, PageEntry dstPage) {
        // 多次访问置0
        this.accessCount++;
        if (this.accessCount >= Config.getDefault().getNurResetInterval()) {
            this.accessCount = 0;
            this.resetFlag(pageTable);
        }
        // 单次访问置1
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

    public int getAccessCount() {
        return accessCount;
    }
}
