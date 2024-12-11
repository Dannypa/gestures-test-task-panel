import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MouseMemeResizePanel panel = new MouseMemeResizePanel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }
}