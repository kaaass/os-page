package net.kaaass.ospage;

import net.kaaass.ospage.algo.*;
import net.kaaass.ospage.simu.IAlgorithm;
import net.kaaass.ospage.simu.Simulation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainLogic {

    private final MainForm form;

    private final Simulation simulation = new Simulation();

    private final List<IAlgorithm> availableAlgorithms;

    private IAlgorithm currentAlgorithm = null;

    public MainLogic() {
        this.availableAlgorithms = new ArrayList<>() {{
            add(new FifoAlgorithm());
            add(new LruAlgorithm());
            add(new NurAlgorithm());
            add(new ClockAlgorithm());
            add(new ImproveClockAlgorithm());
        }};
        this.form = new MainForm(this);
    }

    public void showMainForm() {
        this.form.setVisible(true);
    }

    public void setAlgorithm(IAlgorithm algorithm) {
        this.currentAlgorithm = algorithm;
        this.simulation.setAlgorithm(algorithm);
    }

    public void reset() {
        if (this.currentAlgorithm == null) {
            JOptionPane.showMessageDialog(this.form, "请先选择算法！");
            return;
        }
        this.simulation.reset();
    }

    public List<IAlgorithm> getAvailableAlgorithms() {
        return availableAlgorithms;
    }
}
