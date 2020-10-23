package net.kaaass.ospage.simu;

import java.util.Set;
import java.util.TreeSet;

/**
 * 工作集模型
 */
public class WorkingSet {

    /**
     * 窗口大小
     */
    private int windowSize;

    /**
     * Alpha值
     */
    private float alpha;

    /**
     * 访问计数
     */
    private int count = 0;

    /**
     * 当前工作集
     */
    private final Set<Integer> workSet = new TreeSet<>();

    /**
     * 上次预测窗口大小
     */
    private int lastPredict = 0;

    public WorkingSet(int windowSize, float alpha, int initSize) {
        this.windowSize = windowSize;
        this.alpha = alpha;
        this.lastPredict = initSize;
    }

    /**
     * 判断是否需要更新工作集
     * @return 需要增减的页框数量
     */
    public int judge(int pageId) {
        this.count++;
        // 无需更新工作集
        if (this.count < windowSize) {
            this.workSet.add(pageId);
            return 0;
        }
        // 计算工作集大小
        var real = this.workSet.size();
        var newSize = Math.round(alpha * real + (1 - alpha) * lastPredict);
        var diff = newSize - this.lastPredict;
        this.lastPredict = newSize;
        this.count = 0;
        this.workSet.clear();
        return diff;
    }

    public int size() {
        return this.lastPredict;
    }

    public void reset(int init) {
        this.count = 0;
        this.lastPredict = init;
    }
}
