package net.kaaass.ospage.simu;

/**
 * 页表
 */
public class PageTable {

    private PageEntry[] entries = null;

    public PageTable(int size) {
        this.entries = new PageEntry[size];
        // 初始化表项
        for (int i = 0; i < size; i++) {
            this.entries[i] = new PageEntry(i);
        }
    }

    public int size() {
        return this.entries.length;
    }

    /**
     * 映射逻辑地址到表项
     */
    public PageEntry mapLogicAddress(Address addr) {
        return this.get(addr.prefix());
    }

    public PageEntry get(int pageId) {
        return this.entries[pageId];
    }

}
