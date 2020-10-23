package net.kaaass.ospage.algo.visualise;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * 饼图控件
 */
public class PieFigureComponent extends JComponent {

    /*
     * 绘制参数
     */

    private double outerRingSize = 0.8;
    private double innerRingSize = 0.4;
    private double fontSize = 0.05;
    private double outerTextPos = 0.95;
    private int current = 0;
    private int sectorCount;
    private Vector<String> innerText;
    private Vector<String> outerText;

    /*
     * 位置相关
     */

    private int innerW;
    private int innerH;
    private int innerX;
    private int innerY;
    private int innerCentricX;
    private int innerCentricY;

    public PieFigureComponent(int sectorCount, Vector<String> innerText, Vector<String> outerText) {
        this.sectorCount = sectorCount;
        this.innerText = innerText;
        this.outerText = outerText;
    }

    @Override
    public void paintComponent(Graphics g) {
        var g2d = (Graphics2D) g;
        int x = 0;
        int y = 0;
        int w = getSize().width - 1;
        int h = getSize().height - 1;
        // 设置反锯齿
        var rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHints(rh);
        // 绘制背景
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(x, y, w, h);
        // 绘制内容
        g2d.setPaint(Color.BLACK);
        // 计算绘制区域
        innerW = Math.min(w, h);
        innerH = innerW;
        innerX = w <= h ? 0 : (w - innerW) / 2;
        innerY = w >= h ? 0 : (h - innerH) / 2;
        innerCentricX = innerX + innerW / 2;
        innerCentricY = innerY + innerH / 2;
        // 绘制外圈
        drawCentricCircle(g2d, outerRingSize);
        // 绘制内圈
        drawCentricCircle(g2d, innerRingSize);
        // 绘制分隔线
        for (int i = 0; i < sectorCount; i++) {
            var inner = posOnCircle(innerRingSize, (double) (i) / sectorCount);
            var outer = posOnCircle(outerRingSize, (double) (i) / sectorCount);
            g2d.drawLine(
                    round(inner[0]), round(inner[1]),
                    round(outer[0]), round(outer[1])
            );
        }
        // 创建字体
        Font font = getFont();
        font = font.deriveFont((float) (innerH * fontSize));
        // 绘制文字
        var innerText = this.innerText.toArray(new String[0]);
        var outerText = this.outerText.toArray(new String[0]);
        for (int i = 0; i < sectorCount; i++) {
            // 内部
            var pos = posOnCircle((innerRingSize + outerRingSize) / 2,
                    (i + 0.5) / sectorCount);
            drawCentricString(g2d, pos[0], pos[1], innerText[i], font);
            // 外部
            pos = posOnCircle(outerTextPos, (i + 0.5) / sectorCount);
            drawCentricString(g2d, pos[0], pos[1], outerText[i], font);
        }
        // 绘制箭头
        var pos = posOnCircle(innerRingSize, (current + 0.5) / sectorCount);
        drawArrow(g2d, innerCentricX, innerCentricY, round(pos[0]), round(pos[1]));
    }

    private void drawCentricCircle(Graphics2D g2d, double size) {
        int outRingWH = round(size * innerH);
        g2d.drawOval(
                innerCentricX - outRingWH / 2,
                innerCentricY - outRingWH / 2,
                outRingWH,
                outRingWH);
    }

    private double[] posOnCircle(double size, double at) {
        var ret = new double[2];
        ret[0] = innerCentricX + size * innerH / 2 * Math.sin(at * Math.PI * 2);
        ret[1] = innerCentricY - size * innerH / 2 * Math.cos(at * Math.PI * 2);
        return ret;
    }

    private double[] rotateBy(double x, double y, double rx0, double ry0, double a) {
        double x0 = (x - rx0) * Math.cos(a) - (y - ry0) * Math.sin(a) + rx0;
        double y0 = (x - rx0) * Math.sin(a) + (y - ry0) * Math.cos(a) + ry0;
        return new double[]{x0, y0};
    }

    public void drawCentricString(Graphics2D g2d, double x, double y, String text, Font font) {
        FontMetrics metrics = g2d.getFontMetrics(font);
        var lines = text.lines().collect(Collectors.toList());
        int w = text.lines().mapToInt(metrics::stringWidth).max().orElse(0);
        int h = lines.size() * metrics.getHeight();
        x = x - w / 2.;
        y = y - h / 2. + metrics.getAscent();
        g2d.setFont(font);
        for (var line : lines) {
            g2d.drawString(line, round(x), round(y));
            y += metrics.getHeight();
        }
    }

    private void drawArrow(Graphics2D g2d, double x1, double y1, double x2, double y2) {
        // 线
        g2d.drawLine(round(x1), round(y1), round(x2), round(y2));
        // 箭头
        var arrowHead = new Polygon();
        double angle = Math.atan2(y2 - y1, x2 - x1) - Math.PI / 2;
        arrowHead.addPoint(round(x2), round(y2));
        var pos = rotateBy(-5, -10, 0, 0, angle);
        arrowHead.addPoint(round(x2 + pos[0]), round(y2 + pos[1]));
        pos = rotateBy(5, -10, 0, 0, angle);
        arrowHead.addPoint(round(x2 + pos[0]), round(y2 + pos[1]));
        g2d.fillPolygon(arrowHead);
    }

    private static int round(double val) {
        return (int) Math.round(val);
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSectorCount() {
        return sectorCount;
    }

    public void setSectorCount(int sectorCount) {
        this.sectorCount = sectorCount;
    }
}
