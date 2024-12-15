import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A JPanel that performs a preprocessing task in the background,
 * Displays a progress bar during computation, and shows the result after completion.
 * This panel uses SwingWorker as it works robustly with JProgressBar.
 * The panel is resizable; the components are aligned using GridBagLayout.
 * The loading bar size changes depending on the size of the panel; the result size stays the same
 * as it seems that implementing the size change for it would require listening to resize events.
 */
public class PreprocessingPanel extends JPanel implements PropertyChangeListener {
    /**
     * Modulo value for computations to prevent overflow.
     */
    private final int MOD = 1_000_000_007;

    /**
     * The result of the preprocessing task.
     */
    private int result;

    /**
     * Background task that performs the computation.
     */
    private Task task;

    /**
     * Panel to display the progress bar.
     */
    private final JPanel progressBarPanel = new JPanel();

    /**
     * Progress bar that shows computation progress.
     */
    private final JProgressBar progressBar = new JProgressBar();

    /**
     * Panel to display the final result.
     */
    private final JPanel resultPanel = new JPanel();

    /**
     * Label to display the result text.
     */
    private final JLabel resultLabel = new JLabel();

    /**
     * Property change handler to react to updating progress of the background task.
     *
     * @param propertyChangeEvent The event containing the property change.
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("progress")) {
            progressBar.setValue(task.getProgress());
        }
    }

    /**
     * Background task that performs a computation (e.g., a power of 2 operation).
     * It periodically updates progress as a percentage.
     */
    class Task extends SwingWorker<Void, Void> {
        private final int POWER = 1_000_000_000;

        /**
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            System.out.println("started background");
            result = 1;
            setProgress(0);
            for (int progress = 0; progress <= POWER; progress++) {
                result = (result * 2) % MOD; // Never mind, java overflow is goofy like that
                int percent = POWER / 100;
                if ((progress % percent) == 0) {
                    setProgress(progress / percent);
                }
            }
            return null;
        }

        /**
         * Executed when the background computation finishes in event dispatching thread.
         * Updates the UI to display the result and hide the progress bar.
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

    /**
     * Starts the preprocessing task in the background.
     */
    private void startPreprocessing() {
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Creates a GridBagConstraints object for components based on weightx and weighty.
     *
     * @param weightX Horizontal weight of the component; see <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/GridBagConstraints.html#weightx">weightx</a>.
     * @param weightY Vertical weight of the component; see <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/GridBagConstraints.html#weighty">weighty</a>.
     * @return A configured GridBagConstraints object.
     */
    private GridBagConstraints getNoneFillGBC(int weightX, int weightY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        return gbc;
    }

    /**
     * Adds a component to a panel using the specified GridBagConstraints.
     *
     * @param panel The target panel.
     * @param c     The component to add.
     * @param gbc   The GridBagConstraints for the component.
     */
    private void addComponent(JPanel panel, Component c, GridBagConstraints gbc) {
        panel.add(c, gbc);
    }

    /**
     * Adds a row of components to a panel with specified constraints.
     *
     * @param panel       The target panel.
     * @param rowIndex    The row index to add components.
     * @param components  Array of components to add.
     * @param constraints Array of GridBagConstraints for each component.
     */
    private void addRow(JPanel panel, int rowIndex, Component[] components, GridBagConstraints[] constraints) {
        assert components.length == constraints.length;
        for (int i = 0; i < components.length; i++) {
            constraints[i].gridx = i;
            constraints[i].gridy = rowIndex;
            addComponent(panel, components[i], constraints[i]);
        }
    }

    /**
     * Centers a component within a panel using GridBagLayout.
     *
     * @param panel             The target panel.
     * @param component         The component to center.
     * @param verticalWeights   Weights for vertical layout.
     * @param horizontalWeights Weights for horizontal layout.
     */
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

    /**
     * Configures the progress bar UI.
     */
    private void setUpProgressBar() {
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
    }

    /**
     * Configures the result label UI to display the computation result.
     */
    private void setUpResultLabel() {
        resultLabel.setText("Loaded! " + result);
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setVerticalAlignment(JLabel.CENTER);
        resultLabel.setFont(new Font("Sans Serif", Font.PLAIN, 60));
    }


    /**
     * Sets up a panel and centers a given component in it using specified layout weights.
     *
     * @param panel             The target panel.
     * @param component         The component to center.
     * @param verticalWeights   Weights for vertical layout.
     * @param horizontalWeights Weights for horizontal layout.
     */
    private void setUpPanel(JPanel panel, Component component, int[] verticalWeights, int[] horizontalWeights) {
        panel.setLayout(new GridBagLayout());
        centerComponent(panel, component, verticalWeights, horizontalWeights);
    }

    /**
     * Constructs the PreprocessingPanel, initializes the progress bar, and starts the background task.
     */
    PreprocessingPanel() {
        startPreprocessing();
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        setUpProgressBar();
        setUpPanel(progressBarPanel, progressBar, new int[]{3, 1, 3}, new int[]{1, 4, 1});
        progressBarPanel.setVisible(true);

        this.add(progressBarPanel);
    }

//    // testing
//    public static void main(String[] args) {
//        PreprocessingPanel demo = new PreprocessingPanel();
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//        frame.add(demo);
//        frame.setSize(1000, 1000);
//        frame.setVisible(true);
//    }
}

