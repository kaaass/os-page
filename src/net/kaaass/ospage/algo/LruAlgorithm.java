package net.kaaass.ospage.algo;

import net.kaaass.ospage.algo.visualise.BaseForm;
import net.kaaass.ospage.algo.visualise.LruForm;
import net.kaaass.ospage.simu.IAlgorithm;
import net.kaaass.ospage.simu.PageEntry;
import net.kaaass.ospage.simu.PageTable;
import net.kaaass.ospage.simu.Simulation;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 使用过最久的先淘汰算法
 */
public class LruAlgorithm implements IAlgorithm {

    private Deque<PageEntry> stack = new ArrayDeque<>();
    private LruForm form = new LruForm(this);

    @Override
    public void init(PageTable pageTable) {
        stack.clear();
        this.form.show();
    }

    @Override
    public PageEntry retire(PageTable pageTable) {
        return stack.pollLast();
    }

    @Override
    public void onAlloc(PageTable pageTable, PageEntry pageEntry) {
        stack.push(pageEntry);
    }

    @Override
    public void onAccess(PageTable pageTable, Simulation.Request request, PageEntry dstPage) {
        if (stack.contains(dstPage)) {
            stack.remove(dstPage);
            stack.push(dstPage);
        }
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
        return new String[0];
    }

    @Override
    public String name() {
        return "使用最久先淘汰（LRU）";
    }

    public Deque<PageEntry> getStack() {
        return stack;
    }
}
