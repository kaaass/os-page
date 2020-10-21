package net.kaaass.ospage.simu;

import java.util.*;

/**
 * 模拟
 */
public class Simulation {

    /**
     * 访问请求队列
     */
    private final Queue<Request> requestQueue = new ArrayDeque<>();

    /**
     * 空闲页框
     */
    private final Queue<Integer> freeFrameIds = new ArrayDeque<>();

    private PageTable pageTable = null;

    private WorkingSet workingSet = null;

    private IAlgorithm algorithm = null;

    private SimulationLogger log = new SimulationLogger();

    public void setAlgorithm(IAlgorithm algorithm) {
        this.algorithm = algorithm;
        this.reset();
    }

    public void reset() {
        this.requestQueue.clear();
        this.pageTable = new PageTable(Config.getDefault().getLogicPageCount());
        this.workingSet = new WorkingSet(Config.getDefault().getWindowSize(),
                Config.getDefault().getAlpha(),
                Config.getDefault().getInitFrameCount());
        this.algorithm.init(this.pageTable);
        // 分配初始页框
        this.frameSizeChange(Config.getDefault().getInitFrameCount());
    }

    public void runStep() {
        var request = this.requestQueue.poll();
        this.algorithm.onAccess(this.pageTable, request);
        var dstPage = this.pageTable.mapLogicAddress(request.address);
        // 工作集判定
        int diff = this.workingSet.judge(dstPage.getLogicId());
        this.frameSizeChange(diff);
        // 内存映射
        if (dstPage.isInMemory()) {
            // 在内存
            var dstAddr = Address.map(request.address, dstPage.getFrameId());
            log.pageInMemory(request, dstPage, dstAddr);
            return;
        }
        // 不在内存，换入
        if (!this.freeFrameIds.isEmpty()) {
            // 若当前还有空闲页框，直接分配
            var newFrame = this.freeFrameIds.poll();
            this.allocFrameToPage(dstPage, newFrame);
            var dstAddr = Address.map(request.address, newFrame);
            log.pageSwapIn(request, newFrame, dstPage, dstAddr);
            return;
        }
        // 使用算法淘汰一个页框
        var retirePage = this.algorithm.retire(this.pageTable);
        var retireFrameId = retirePage.getFrameId();
        log.pageRetire(retirePage, dstPage);
        retirePage.setFrameId(-1); // 换出内存
        this.allocFrameToPage(dstPage, retireFrameId);
        var dstAddr = Address.map(request.address, retireFrameId);
        log.pageSwapIn(request, retireFrameId, dstPage, dstAddr);
    }

    private int generateFrameId() {
        var rand = new Random();
        return rand.nextInt((1 << 4) - 1);
    }

    private void frameSizeChange(int diff) {
        if (diff != 0) {
            log.workingSetSizeChange(workingSet, diff);
        }
        if (diff > 0) {
            // 分配 diff 个页框
            var allocCount = diff;
            var allocFrames = new ArrayList<Integer>();
            for (int i = 0; i < allocCount; i++) {
                var id = this.generateFrameId();
                allocFrames.add(id);
                this.freeFrameIds.add(id);
            }
            log.workingSetExpand(workingSet, allocFrames);
        } else if (diff < 0) {
            // 淘汰 -diff 个
            var retireCount = -diff;
            var retireFrames = new ArrayList<Integer>();
            // 有未分配页框，直接释放未分配页框
            if (!this.freeFrameIds.isEmpty()) {
                while (!this.freeFrameIds.isEmpty() && retireCount > 0) {
                    retireFrames.add(this.freeFrameIds.poll());
                    retireCount--;
                }
            }
            // 还有剩余待淘汰页框，用算法淘汰
            while (retireCount > 0) {
                var retirePage = this.algorithm.retire(this.pageTable);
                retireFrames.add(retirePage.getFrameId());
                log.pageRetire(retirePage, null);
                retirePage.setFrameId(-1); // 换出内存
                retireCount--;
            }
            log.workingSetShrink(workingSet, retireFrames);
        }
    }

    private void allocFrameToPage(PageEntry dstPage, int frameId) {
        dstPage.setFrameId(frameId);
        this.algorithm.onAlloc(this.pageTable, dstPage);
    }

    public void addRequest(Request request) {
        this.requestQueue.add(request);
    }

    public enum AccessType {
        READ, WRITE
    }

    public static class Request {
        AccessType type;
        Address address;

        public Request(AccessType type, Address address) {
            this.type = type;
            this.address = address;
        }

        public static Request random() {
            var rand = new Random();
            AccessType type;
            if (rand.nextInt(2) == 0)
                type = AccessType.READ;
            else
                type = AccessType.WRITE;
            return new Request(type, Address.random());
        }

        @Override
        public String toString() {
            return "Request{" +
                    "type=" + type +
                    ", address=" + address +
                    '}';
        }
    }

}
