import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PreprocessingPanel extends JPanel implements PropertyChangeListener {
    private int result = 0;
    private Task task;
    final private JPanel progressBarPanel = new JPanel();
    final private JProgressBar progressBar = new JProgressBar();
    final private JPanel resultPanel = new JPanel();
    final private JLabel resultLabel = new JLabel("Loaded!");

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
            progressBarPanel.setVisible(false);
            resultPanel.setVisible(true);
            add(resultPanel);
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

    private void centerComponent(JPanel panel, Component component, int[] verticalWeights, int[] horizontalWeights) {
        addRow(
                panel,
                0,
                new Component[]{Box.createVerticalGlue()},
                new GridBagConstraints[]{getGBC(1, verticalWeights[0], GridBagConstraints.NONE)}
        );

        GridBagConstraints[] middleRowConstraints = new GridBagConstraints[3];
        for (int i = 0; i < 3; i++) {
            middleRowConstraints[i] = getGBC(horizontalWeights[i], 1, GridBagConstraints.NONE);
        }
        middleRowConstraints[1].fill = GridBagConstraints.BOTH;
        addRow(
                panel,
                1,
                new Component[]{Box.createHorizontalGlue(), component, Box.createHorizontalGlue()},
                middleRowConstraints
        );

        addRow(
                panel,
                2,
                new Component[]{Box.createVerticalGlue()},
                new GridBagConstraints[]{getGBC(1, verticalWeights[2], GridBagConstraints.NONE)}
        );
    }


    private void setUpProgressBar() {
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
    }

    private void setUpResultLabel() {
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setVerticalAlignment(JLabel.CENTER);
        resultLabel.setFont(new Font("Sans Serif", Font.PLAIN, 60));
    }


    private void setUpPanel(JPanel panel, Component component, int[] verticalWeights, int[] horizontalWeights) {
        panel.setLayout(new GridBagLayout());
        centerComponent(panel, component, verticalWeights, horizontalWeights);
    }

    PreprocessingPanel() {
        startPreprocessing();
        this.setLayout(new BorderLayout());

        setUpProgressBar();
        setUpPanel(progressBarPanel, progressBar, new int[]{3, 1, 3}, new int[]{1, 4, 1});
        setUpResultLabel();
        setUpPanel(resultPanel, resultLabel, new int[]{3, 1, 3}, new int[]{1, 4, 1});
        progressBarPanel.setVisible(true);

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

