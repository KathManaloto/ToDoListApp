import javax.swing.*;
import java.awt.*;

public class ToDoListGUI extends JFrame{

    public ToDoListGUI(){
        super("To-Do App");

        setupFrame();
    }

    private void setupFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,450);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350,350));
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListGUI::new);
    }
}
