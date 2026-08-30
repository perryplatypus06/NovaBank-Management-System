import javax.swing.SwingUtilities;

public class NovaBankApp {
    public static void main(String[] args) {
        // Enforce Swing execution on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}