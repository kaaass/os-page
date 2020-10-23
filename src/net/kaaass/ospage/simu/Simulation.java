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

    /**
     * 已分配页框，防止重复
     */
    private final List<Integer> notAllocatedFrameIds = new ArrayList<>();

    private PageTable pageTable = null;

    private WorkingSet workingSet = null;

    private IAlgorithm algorithm = null;

    private SimulationLogger log = new SimulationLogger();

    public void setAlgorithm(IAlgorithm algorithm) {
        if (this.algorithm != null)
            this.algorithm.onClose();
        this.algorithm = algorithm;
        this.reset();
    }

    public void reset() {
        if (this.algorithm == null) return;
        this.requestQueue.clear();
        this.pageTable = new PageTable(Config.getDefault().getLogicPageCount());
        this.workingSet = new WorkingSet(Config.getDefault().getWindowSize(),
                Config.getDefault().getAlpha(),
                Config.getDefault().getInitFrameCount());
        // 算法初始化
        this.algorithm.init(this.pageTable);
        this.algorithm.onDraw(this);
        // 重置页框全集
        this.notAllocatedFrameIds.clear();
        for (int i = 0; i < (1 << 4); i++) {
            this.notAllocatedFrameIds.add(i);
        }
        Collections.shuffle(this.notAllocatedFrameIds);
        this.freeFrameIds.clear();
        // 分配初始页框
        this.frameSizeChange(Config.getDefault().getInitFrameCount());
        // 工作集重置
        this.workingSet.reset(Config.getDefault().getInitFrameCount());
    }

    public void runStep() {
        if (this.algorithm == null) return;
        if (this.requestQueue.isEmpty()) return;
        var request = this.requestQueue.poll();
        var dstPage = this.pageTable.mapLogicAddress(request.address);
        this.algorithm.onAccess(this.pageTable, request, dstPage);
        // 工作集判定
        int diff = this.workingSet.judge(dstPage.getLogicId());
        this.frameSizeChange(diff);
        // 内存映射
        var mapped = this.pageMapping(dstPage, request);
        if (request.type == AccessType.WRITE)
            mapped.setAttribute(PageEntry.C_MOD_FLAG, true);
        // 更新UI
        this.algorithm.onDraw(this);
    }

    private int allocNewFrame() {
        int id = this.notAllocatedFrameIds.get(0);
        this.freeFrameIds.add(id);
        this.notAllocatedFrameIds.remove(Integer.valueOf(id));
        return id;
    }

    private void freeFrame(int id) {
        this.freeFrameIds.remove(id);
        this.notAllocatedFrameIds.add(id);
    }

    /**
     * 通过读写请求进行内存页映射
     * @param dstPage 请求地址对应的页
     * @param request 请求
     * @return 映射后的页
     */
    private PageEntry pageMapping(PageEntry dstPage, Request request) {
        if (dstPage.isInMemory()) {
            // 在内存
            var dstAddr = Address.map(request.address, dstPage.getFrameId());
            log.pageInMemory(request, dstPage, dstAddr);
            return dstPage;
        }
        // 不在内存，换入
        if (!this.freeFrameIds.isEmpty()) {
            // 若当前还有空闲页框，直接分配
            var newFrame = this.freeFrameIds.poll();
            this.allocFrameToPage(dstPage, newFrame);
            var dstAddr = Address.map(request.address, newFrame);
            log.pageSwapIn(request, newFrame, dstPage, dstAddr);
            return dstPage;
        }
        // 使用算法淘汰一个页框
        var retirePage = this.algorithm.retire(this.pageTable);
        var retireFrameId = retirePage.getFrameId();
        log.pageRetire(retirePage, dstPage);
        retirePage.setFrameId(-1); // 换出内存
        this.allocFrameToPage(dstPage, retireFrameId);
        var dstAddr = Address.map(request.address, retireFrameId);
        log.pageSwapIn(request, retireFrameId, dstPage, dstAddr);
        return dstPage;
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
                var id = this.allocNewFrame();
                allocFrames.add(id);
            }
            log.workingSetExpand(workingSet, allocFrames);
        } else if (diff < 0) {
            // 淘汰 -diff 个
            var retireCount = -diff;
            var retireFrames = new ArrayList<Integer>();
            // 有未分配页框，直接释放未分配页框
            if (!this.freeFrameIds.isEmpty()) {
                while (!this.freeFrameIds.isEmpty() && retireCount > 0) {
                    var id = this.freeFrameIds.poll();
                    retireFrames.add(id);
                    this.freeFrame(id);
                    retireCount--;
                }
            }
            // 还有剩余待淘汰页框，用算法淘汰
            while (retireCount > 0) {
                var retirePage = this.algorithm.retire(this.pageTable);
                retireFrames.add(retirePage.getFrameId());
                this.freeFrame(retirePage.getFrameId());
                log.pageRetire(retirePage, null);
                retirePage.setFrameId(-1); // 换出内存
                retireCount--;
            }
            log.workingSetShrink(workingSet, retireFrames);
        }
    }

    /**
     * 页面分配新页框，即换入内存
     */
    private void allocFrameToPage(PageEntry dstPage, int frameId) {
        dstPage.setFrameId(frameId);
        dstPage.setAttribute(PageEntry.C_MOD_FLAG, false); // 同时要写出页内容
        this.algorithm.onAlloc(this.pageTable, dstPage);
    }

    public void addRequest(Request request) {
        this.requestQueue.add(request);
    }

    public PageTable getPageTable() {
        return pageTable;
    }

    public WorkingSet getWorkingSet() {
        return workingSet;
    }

    public Queue<Request> getRequestQueue() {
        return requestQueue;
    }

    public enum AccessType {
        READ("读"), WRITE("写");

        private final String name;

        AccessType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class Request {
        public AccessType type;
        public Address address;

        public Request(AccessType type, Address address) {
            this.type = type;
            this.address = address;
        }

        public static Request random() {
            var rand = new Random();
            AccessType type;
            if (rand.nextInt(6) > 0)
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
