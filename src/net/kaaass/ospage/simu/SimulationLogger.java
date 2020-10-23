package net.kaaass.ospage.simu;

import net.kaaass.ospage.MainLogic;

import java.util.ArrayList;

public class SimulationLogger {

    private static MainLogic logic = null;

    public static void setLogic(MainLogic logic) {
        SimulationLogger.logic = logic;
    }

    public void pageInMemory(Simulation.Request request, PageEntry dstPage, Address dstAddr) {
        format("页面 %d 已经在内存中", dstPage.getLogicId());
        logMapping(request.address, dstAddr);
    }

    public void pageSwapIn(Simulation.Request request, int frameId, PageEntry dstPage, Address dstAddr) {
        format("页面换入内存，页框（%d -> %d）", dstPage.getLogicId(), frameId);
        logMapping(request.address, dstAddr);
    }

    public void pageRetire(PageEntry retirePage, PageEntry dstPage) {
        format("淘汰页 %d 的页框 %d 给页 %d", retirePage.getLogicId(), retirePage.getFrameId(), dstPage == null ? -1 : dstPage.getLogicId());
    }

    public void workingSetSizeChange(WorkingSet set, int diff) {
        format("工作集大小变换为 %d", set.size());
    }

    public void workingSetShrink(WorkingSet workingSet, ArrayList<Integer> retireFrames) {
        format("工作集缩小，回收页框 %s", retireFrames);
    }

    public void workingSetExpand(WorkingSet workingSet, ArrayList<Integer> allocFrames) {
        format("工作集放大，分配页框 %s", allocFrames);
    }

    public void logMapping(Address source, Address dest) {
        if (logic == null) {
            System.out.printf("页面映射：%s -> %s\n", source, dest);
            return;
        }
        logic.log(source, dest);
    }

    public void format(String format, Object... args) {
        var content = String.format(format, args);
        if (logic == null) {
            System.out.println(content);
            return;
        }
        logic.log(content);
    }
}
