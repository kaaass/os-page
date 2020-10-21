package net.kaaass.ospage.simu;

import java.util.ArrayList;

public class SimulationLogger {
    public void pageInMemory(Simulation.Request request, PageEntry dstPage, Address dstAddr) {
        System.out.printf("页面已经在内存中 %s -> %s\n", request.address, dstAddr);
    }

    public void pageSwapIn(Simulation.Request request, int frameId, PageEntry dstPage, Address dstAddr) {
        System.out.printf("页面换入内存，页框（%d -> %d） %s -> %s\n", dstPage.getLogicId(), frameId, request.address, dstAddr);
    }

    public void pageRetire(PageEntry retirePage, PageEntry dstPage) {
        System.out.printf("淘汰页 %d 的页框 %d 给页 %d\n", retirePage.getLogicId(), retirePage.getFrameId(), dstPage == null ? -1 : dstPage.getLogicId());
    }

    public void workingSetSizeChange(WorkingSet set, int diff) {
        System.out.printf("工作集大小变换为 %d\n", set.size());
    }

    public void workingSetShrink(WorkingSet workingSet, ArrayList<Integer> retireFrames) {
        System.out.printf("工作集缩小，回收页框 %s\n", retireFrames);
    }

    public void workingSetExpand(WorkingSet workingSet, ArrayList<Integer> allocFrames) {
        System.out.printf("工作集放大，分配页框 %s\n", allocFrames);
    }
}
