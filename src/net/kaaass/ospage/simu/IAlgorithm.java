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
    void onAccess(PageTable pageTable, Simulation.Request request, PageEntry dstPage);

    /**
     * 绘制
     */
    void onDraw(Simulation simulation);

    /**
     * 切换其他算法调用
     */
    void onClose();

    /**
     * 获得特有列名
     */
    String[] getColumnNames();

    /**
     * 获得算法名称
     */
    String name();
}
