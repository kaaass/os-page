package net.kaaass.ospage.simu;

/**
 * 配置类
 */
public class Config {

    /**
     * 逻辑页数量
     */
    private int logicPageCount = 1 << 4;

    /**
     * 初始页框数量
     */
    private int initFrameCount = 3;

    /**
     * 工作集窗口大小
     */
    private int windowSize = 5;

    /**
     * 工作集递推因数
     */
    private float alpha = 0.5f;

    private final static Config DEFAULT = new Config();

    public static Config getDefault() {
        return DEFAULT;
    }

    public int getLogicPageCount() {
        return logicPageCount;
    }

    public void setLogicPageCount(int logicPageCount) {
        this.logicPageCount = logicPageCount;
    }

    public int getInitFrameCount() {
        return initFrameCount;
    }

    public void setInitFrameCount(int initFrameCount) {
        this.initFrameCount = initFrameCount;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
