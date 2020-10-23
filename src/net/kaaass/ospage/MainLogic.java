package net.kaaass.ospage;

public class MainLogic {

    private MainForm mainForm = new MainForm(this);

    public void showMainForm() {
        this.mainForm.setVisible(true);
    }
}
