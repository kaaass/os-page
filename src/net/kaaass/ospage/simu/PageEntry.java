package net.kaaass.ospage.simu;

import java.util.HashMap;
import java.util.Map;

/**
 * 页表表项，可以是逻辑页或物理页框
 */
public class PageEntry {

    /**
     * 页框大小
     */
    public final static int FRAME_SIZE_BIT_LEN = 12;
    public final static int FRAME_SIZE = (1 << FRAME_SIZE_BIT_LEN);

    /*
     * 通用字段
     *
     * 忽略：外存块号、访问权限
     */

    public final static String C_LOGIC_ID = "逻辑页号";
    public final static String C_FRAME_ID = "页框号";
    public final static String C_MEM_FLAG = "内外标记";
    public final static String C_MOD_FLAG = "修改标志";

    public final static String[] COMMON_COLUMNS = new String[]{C_LOGIC_ID, C_FRAME_ID, C_MEM_FLAG, C_MOD_FLAG};

    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * 默认外存、未分配页框、未修改
     */
    public PageEntry(int logicId) {
        setAttribute(C_LOGIC_ID, logicId);
        setAttribute(C_FRAME_ID, -1);
        setAttribute(C_MEM_FLAG, false);
        setAttribute(C_MOD_FLAG, false);
    }

    public boolean isInMemory() {
        return (Boolean) getAttribute(C_MEM_FLAG);
    }

    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public int getLogicId() {
        return (Integer) getAttribute(C_LOGIC_ID);
    }

    public int getFrameId() {
        return (Integer) getAttribute(C_FRAME_ID);
    }

    public void setFrameId(int frameId) {
        setAttribute(C_FRAME_ID, frameId);
        if (frameId >= 0) {
            setAttribute(C_MEM_FLAG, true);
        } else {
            setAttribute(C_MEM_FLAG, false);
        }
    }
}
