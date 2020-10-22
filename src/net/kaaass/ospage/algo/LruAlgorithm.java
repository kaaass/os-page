package net.kaaass.ospage.algo;

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

    @Override
    public void init(PageTable pageTable) {
        stack.clear();
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

    }

    @Override
    public void onClose() {

    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }
}
