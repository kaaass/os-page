package net.kaaass.ospage;

import net.kaaass.ospage.algo.*;
import net.kaaass.ospage.simu.Address;
import net.kaaass.ospage.simu.Config;
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
        this.form.init();
        this.form.setVisible(true);
    }

    public void setAlgorithm(IAlgorithm algorithm) {
        this.currentAlgorithm = algorithm;
        this.simulation.setAlgorithm(algorithm);
        this.form.update();
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

    public Simulation getSimulation() {
        return simulation;
    }

    public void addRandomRequests() {
        var count = Config.getDefault().getRandomAddRequestCount();
        for (int i = 0; i < count; i++) {
            this.simulation.addRequest(Simulation.Request.random());
        }
    }

    public void addRequest(Simulation.AccessType type, int address) {
        this.simulation.addRequest(new Simulation.Request(type, Address.of(address)));
    }
}
