package net.kaaass.ospage.simu;

/**
 * 置换算法
 */
public interface IAlgorithm {

    /**
     * 初始化算法
     */
    void init(PageTable pageTable);

    /**
     * 淘汰一页
     * @return 淘汰的页框属于的页
     */
    PageEntry retire(PageTable pageTable);

    /**
     * 分配一页，在换入页面后调用
     */
    void onAlloc(PageTable pageTable, PageEntry pageEntry);

    /**
     * 访问
     */
    void onAccess(PageTable pageTable, Simulation.Request request);

    /**
     * 获得特有列名
     */
    String[] getColumnNames();
}
