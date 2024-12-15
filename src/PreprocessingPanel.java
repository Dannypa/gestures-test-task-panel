import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PreprocessingPanel extends JPanel implements PropertyChangeListener {
    private final int MOD = 1_000_000_007;
    private int result;
    private Task task;
    private final JPanel progressBarPanel = new JPanel();
    private final JProgressBar progressBar = new JProgressBar();
    private final JPanel resultPanel = new JPanel();
    private final JLabel resultLabel = new JLabel();

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("progress")) {
            progressBar.setValue(task.getProgress());
        }
    }

    class Task extends SwingWorker<Void, Void> {
        private final int POWER = 1_000_000_000;
        private final int percent = POWER / 100;

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            System.out.println("started background");
            result = 1;
            setProgress(0);
            for (int progress = 0; progress <= POWER; progress++) {
                result = (result * 2) % MOD; // Never mind, java overflow is goofy like that
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
            progressBarPanel.setVisible(false);

            setUpResultLabel();
            setUpPanel(resultPanel, resultLabel, new int[]{3, 1, 3}, new int[]{1, 4, 1});
            resultPanel.setVisible(true);
            add(resultPanel);

        }
    }

    private void startPreprocessing() {
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    private GridBagConstraints getNoneFillGBC(int weightX, int weightY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        gbc.fill = GridBagConstraints.NONE;
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
                new GridBagConstraints[]{getNoneFillGBC(1, verticalWeights[0])}
        );

        GridBagConstraints[] middleRowConstraints = new GridBagConstraints[3];
        for (int i = 0; i < 3; i++) {
            middleRowConstraints[i] = getNoneFillGBC(horizontalWeights[i], 1);
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
                new GridBagConstraints[]{getNoneFillGBC(1, verticalWeights[2])}
        );
    }


    private void setUpProgressBar() {
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
    }

    private void setUpResultLabel() {
        resultLabel.setText("Loaded! " + result);
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

