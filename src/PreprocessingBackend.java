import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A class that performs a preprocessing task in the background,
 * Displays a progress bar during computation, and shows the result after completion.
 * This class uses SwingWorker as it works robustly with JProgressBar.
 */
public class PreprocessingBackend implements PropertyChangeListener {
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
     * Progress bar that shows computation progress.
     */
    private final JProgressBar progressBar;

    /**
     * Function to display the final result panel.
     */
    private final Runnable setUpResultPanel;

    /**
     * Function to disable the progress bar panel.
     */
    private final Runnable disableProgressBarPanel;

    /**
     * Label to display the result text.
     */
    private final JLabel resultLabel;

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
            disableProgressBarPanel.run();

            setUpResultLabel();
            setUpResultPanel.run();
        }
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
     * Starts the preprocessing task in the background.
     * NB!!! all the fields should be defined before calling this method!!!
     */
    public void startPreprocessing() {
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Constructs the PreprocessingBackend, initializes the progress bar,
     * and starts the background computation task.
     *
     * @param progressBar             The progress bar to display task progress.
     * @param showProgressBarPanel    Runnable that configures the progress bar panel and makes it visible
     * @param disableProgressBarPanel Runnable to hide the progress bar panel after completion.
     * @param resultLabel             The label to display the final result.
     * @param showResultPanel         Runnable that configures the result panel and makes it visible
     */
    PreprocessingBackend(JProgressBar progressBar, Runnable showProgressBarPanel, Runnable disableProgressBarPanel, JLabel resultLabel, Runnable showResultPanel) {
        this.progressBar = progressBar;
        this.resultLabel = resultLabel;
        this.setUpResultPanel = showResultPanel;
        this.disableProgressBarPanel = disableProgressBarPanel;

        setUpProgressBar();
        showProgressBarPanel.run();
    }
}

