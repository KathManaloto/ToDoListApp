import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        setLookAndFeel();

        // Always launch Swing apps on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ToDoListGUI gui = new ToDoListGUI();
            ToDoLogic logic = new ToDoLogic();
            new ToDoController(gui, logic);
            gui.setVisible(true);   // ✅ show window here
        });
    }

    // ========================================= HELPER =========================================
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Nimbus not available, using default.");
        }
    }
}
