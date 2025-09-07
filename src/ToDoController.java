import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

//CONTROLS THE FUNCTIONALITY OF BUTTONS
public class ToDoController {

    // ====================================== FIELDS ==============================================
    private final ToDoListGUI gui;
    private final ToDoLogic logic;
    private List<Task> currentFilteredTasks;
    private javax.swing.Timer messageTimer;
    private static final String PLACEHOLDER_TEXT = "<no existing task>";

    // =================================== CONSTRUCTORS ===========================================
    public ToDoController(ToDoListGUI gui, ToDoLogic logic) {

        this.gui = gui;
        this.logic = logic;

        //Buttons actionListeners
        setupEmptyTableListener();
        setupPlaceholderRenderer();
        wireFilter();
        wireAdd();
        wireEdit();
        wireDelete();
        wireToggle();
        refreshCurrentFilter();  // load initial data
    }

    // ================================= COMPONENT CONTROLLER ==============================================

    //ADD BUTTON
    private void wireAdd() {

        gui.getAddButton().addActionListener(e -> {

            String newTask = gui.getTaskInput().getText().trim();
                if (!newTask.isEmpty()) {
                    logic.addTask(newTask);
                    gui.getTaskInput().setText("");
                    showTemporaryMessage(gui.getMessageLabel(),
                        "==== Task added successfully. ====", new Color(62, 147, 34));

                    refreshCurrentFilter();

                } else{
                    showTemporaryMessage(gui.getMessageLabel(),
                        "==== Please input the new task in the textbox. ====", new Color(197, 19, 19));
                }
        });
    }

    //EDIT BUTTON
    private void wireEdit() {

        gui.getEditButton().addActionListener(e -> {

            int selectedRow = gui.getTaskTable().getSelectedRow();
                if (selectedRow == -1) {
                    showTemporaryMessage(gui.getMessageLabel(),
                        "==== Please select a task to edit. ====", new Color(197, 19, 19));

                    return;
                }

            Task task = currentFilteredTasks.get(selectedRow);
            String newDescription = JOptionPane.showInputDialog(gui, "Edit Task: " + task.getDescription());

                if (newDescription != null && !newDescription.trim().isEmpty()) {
                    logic.editTask(task, newDescription.trim());
                    showTemporaryMessage(gui.getMessageLabel(),
                        "==== Task edited successfully. ====", new Color(62, 147, 34));

                    refreshCurrentFilter();

                } else if (newDescription != null) {
                    showTemporaryMessage(gui.getMessageLabel(),
                        "==== Task description cannot be empty. ====", new Color(197, 19, 19));
                }
        });
    }

    // DELETE BUTTON
    private void wireDelete() {
        gui.getDeleteButton().addActionListener(e -> {
            int[] selectedRows = gui.getTaskTable().getSelectedRows();

            if (selectedRows.length == 0) {
                showTemporaryMessage(gui.getMessageLabel(),
                        "==== Please select at least one task to delete. ====",
                        new Color(197, 19, 19));
                return;
            }

            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(gui,
                    "Are you sure you want to delete the selected task(s)?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Delete in reverse order so indices don’t shift
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    Task task = currentFilteredTasks.get(selectedRows[i]);
                    logic.deleteTask(task);
                }

                showTemporaryMessage(gui.getMessageLabel(),
                        "==== Task(s) deleted successfully. ====",
                        new Color(62, 147, 34));

                refreshCurrentFilter();
            }
        });
    }

    //VIEW DROPDOWN
    private void wireFilter() {

        gui.getViewComboBox().addActionListener(e -> {

            String choice = (String) gui.getViewComboBox().getSelectedItem();
                if (choice != null) {
                    refreshTable(choice);
                }
        });
    }

    //TOGGLE STATUS
    private void wireToggle() {

        gui.getTaskTable().getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {  //ensures you’re only handling checkbox clicks (cell edits), not task additions or deletions.

                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 0 && row >= 0) {
                    Task task = currentFilteredTasks.get(row);
                    logic.toggleTaskStatus(task);

                    refreshCurrentFilter();
                }
            }
        });
    }

    private void refreshTable(String filter) {

        switch (filter){
            case "VIEW COMPLETED TASKS" -> currentFilteredTasks  = logic.getCompletedTasks();
            case "VIEW PENDING TASKS" -> currentFilteredTasks  = logic.getPendingTasks();
            default -> currentFilteredTasks  = logic.getAllTasks();
        }

        DefaultTableModel model = gui.getTableModel();
        model.setRowCount(0); // clear existing rows

        for (Task task : currentFilteredTasks ) {
            model.addRow(new Object[]{task.isCompleted(), task.getDescription()});
        }
    }

    private void refreshCurrentFilter() {
        String filter = (String) gui.getViewComboBox().getSelectedItem();
        if (filter != null) { refreshTable(filter); }
    }

    private void setupEmptyTableListener() {
        gui.getTableModel().addTableModelListener(e -> updateEmptyTablePlaceholder());
        updateEmptyTablePlaceholder(); // run once at startup
    }

    private void updateEmptyTablePlaceholder() {
        DefaultTableModel model = gui.getTableModel();

        if (logic.getAllTasks().isEmpty()) {
            if (model.getRowCount() == 0) {
                model.addRow(new Object[]{false, PLACEHOLDER_TEXT});
                gui.getTaskTable().setEnabled(false);
                gui.getTaskTable().clearSelection();   // ✅ make sure placeholder is NOT selected
            }
        } else {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (PLACEHOLDER_TEXT.equals(model.getValueAt(i, 1))) {
                    model.removeRow(i);
                    break;
                }
            }
            gui.getTaskTable().setEnabled(true);
        }
    }

    private void setupPlaceholderRenderer() {
        gui.getTaskTable().setDefaultRenderer(Object.class, (table, value, isSelected, hasFocus, row, column) -> {
            Component c = new DefaultTableCellRenderer().getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (PLACEHOLDER_TEXT.equals(table.getValueAt(row, 1))) {
                c.setForeground(Color.GRAY);
                c.setFont(c.getFont().deriveFont(Font.ITALIC));
            } else {
                c.setForeground(Color.DARK_GRAY);
                c.setFont(c.getFont().deriveFont(Font.PLAIN));
            }

            return c;
        });
    }

    private void showTemporaryMessage(JLabel label, String message, Color color) {
        // Stop any existing timer first
        if (messageTimer != null && messageTimer.isRunning()) {
            messageTimer.stop();
        }

        label.setForeground(color);
        label.setFont(new Font("Arial", Font.ITALIC, 11));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText(message);

        // FIX: Make sure label always has space, even if text is empty
        if (label.getPreferredSize().height == 0) {
            label.setPreferredSize(new Dimension(label.getWidth(), 20)); // reserve ~20px
        }

        // Start new timer
        messageTimer = new javax.swing.Timer(5000, e -> label.setText(""));
        messageTimer.setRepeats(false); // run once only
        messageTimer.start();
    }
}