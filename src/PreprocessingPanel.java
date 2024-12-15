import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PreprocessingPanel extends JPanel implements PropertyChangeListener {
    private int result = 0;
    private Task task;
    final private JPanel progressBarPanel = new JPanel();
    final private JProgressBar progressBar = new JProgressBar();

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("progress")) {
            progressBar.setValue(task.getProgress());
        }
    }

    class Task extends SwingWorker<Void, Void> {
        private final int MAX_PROGRESS = 1_000_000_000;
        private final int percent = MAX_PROGRESS / 100;
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            result = 1;
            setProgress(0);
            for (int progress = 0; progress <= MAX_PROGRESS; progress++) {
                result *= 2; // I don't care about overflow
                if ((progress % percent) == 0) {
                    setProgress(progress / percent);
                }
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void startPreprocessing() {
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    private GridBagConstraints getGBC(int weightX, int weightY, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        gbc.fill = fill;
        return gbc;
    }

    private void addComponent(JPanel panel, Component c, GridBagConstraints gbc) {
        panel.add(c, gbc);
    }

    private void addRow(JPanel panel, int rowIndex, Component[] components, GridBagConstraints[] constraints) {
        assert components.length == constraints.length;
        for (int i = 0; i < components.length; i++) {
            constraints[i].gridx = i;
            constraints[i].gridy = rowIndex;
            addComponent(panel, components[i], constraints[i]);
        }
    }


    private void setUpProgressBar() {
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
    }

    private void setUpProgressBarPanel() {
        setUpProgressBar();

        progressBarPanel.setLayout(new GridBagLayout());

        addRow(
                progressBarPanel,
                0,
                new Component[]{Box.createVerticalGlue()},
                new GridBagConstraints[]{getGBC(1, 3, GridBagConstraints.NONE)}
        );

        addRow(
                progressBarPanel,
                1,
                new Component[]{Box.createHorizontalGlue(), progressBar, Box.createHorizontalGlue()},
                new GridBagConstraints[]{
                        getGBC(1, 1, GridBagConstraints.NONE),
                        getGBC(4, 1, GridBagConstraints.BOTH),
                        getGBC(1, 1, GridBagConstraints.NONE)
                }
        );

        addRow(
                progressBarPanel,
                2,
                new Component[]{Box.createVerticalGlue()},
                new GridBagConstraints[]{getGBC(1, 3, GridBagConstraints.NONE)}
        );
    }

    PreprocessingPanel() {
        startPreprocessing();
        this.setLayout(new BorderLayout());
        setUpProgressBarPanel();
        this.add(progressBarPanel);
    }

    // todo: delete
    public static void main(String[] args) {
        PreprocessingPanel demo = new PreprocessingPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(demo);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }
}

