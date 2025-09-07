import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.net.URL;

public class ToDoListGUI extends JFrame{

    // ========================================= FIELDS =========================================
    private JComboBox<String> viewComboBox;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JLabel messageLabel;
    private JTextField taskInput;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    // ========================================= CONSTRUCTOR =========================================
    public ToDoListGUI(){
        //JFrame
        super("To-Do List App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(450,550));

        //Image Icon
        ImageIcon appIcon = loadIcon("/appIcon.png");
        if (appIcon != null) {
            setIconImage(appIcon.getImage()); // app & title bar icon
        }

        //mainPanel
        JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(255, 255, 179));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

            mainPanel.add(createTopPanel(), BorderLayout.NORTH);
            mainPanel.add(createCenterPanel(),BorderLayout.CENTER);
            mainPanel.add(createBottomPanel(),BorderLayout.SOUTH);

        //JFrame
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    // ========================================= TOP PANEL =========================================
    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(new Color(255, 255, 179));

        //titleLabel
        JLabel titleLabel = new JLabel("TO-DO LIST",  loadIcon("/checkIcon.png"), JLabel.CENTER);
            titleLabel.setFont(new Font("Impact",Font.PLAIN,28));
            titleLabel.setForeground(new Color(89, 89, 89));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //viewComboBox
        viewComboBox = createJComboBox();

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            comboPanel.setBackground(new Color(255, 255, 179));
            comboPanel.add(viewComboBox);

        topPanel.add(titleLabel,BorderLayout.NORTH);
        topPanel.add(comboPanel,BorderLayout.CENTER);

        return topPanel;
    }

    // ===== HELPER - ICON =====
    private ImageIcon loadIcon(String path) {
        URL iconUrl = getClass().getResource(path);
        return (iconUrl != null) ? new ImageIcon(iconUrl) : null;
    }

    // ===== HELPER - viewComboBox =====
    private JComboBox<String> createJComboBox() {
        viewComboBox = new JComboBox<>(new String[]{
                "VIEW ALL TASKS","VIEW COMPLETED TASKS","VIEW PENDING TASKS"});

        DefaultListCellRenderer customRenderer = new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setForeground(new Color(38, 38, 38));
                    label.setFont(new Font("Arial",Font.BOLD,11));
                    label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

                return label;
            }
        };

        viewComboBox.setRenderer(customRenderer);
        viewComboBox.setPreferredSize(new Dimension(250, 40));
        viewComboBox.setFocusable(false);

        return viewComboBox;
    }

    // ========================================= CENTER PANEL =========================================
    private JPanel createCenterPanel(){

        JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBackground(new Color(255, 255, 179));
            centerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        //scrollPane
        JScrollPane scrollPane = new JScrollPane(createJTable());
        JTableHeader header = taskTable.getTableHeader();
            header.setFont(header.getFont().deriveFont(Font.BOLD));
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        centerPanel.add(scrollPane,BorderLayout.CENTER);

        return centerPanel;
    }

    // ===== HELPER - scrollPane =====
    private JTable createJTable(){

        tableModel = new DefaultTableModel(new Object[]{"STATUS", "TASK"}, 0){

            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? Boolean.class : String.class;}
            public boolean isCellEditable(int row, int column){ return column == 0;}
        };

        //taskTable
        taskTable = new JTable(tableModel);
            taskTable.setRowHeight(24);
            taskTable.setFillsViewportHeight(true);
            taskTable.getTableHeader().setReorderingAllowed(false);
            taskTable.setShowHorizontalLines(true);
            taskTable.setShowGrid(true);
            taskTable.setGridColor(Color.LIGHT_GRAY);
            taskTable.getColumnModel().getColumn(0).setMaxWidth(60);
            taskTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            taskTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        styleText(taskTable);

        return taskTable;
    }

    // ========================================= BOTTOM PANEL =========================================
    private JPanel createBottomPanel(){

        JPanel bottomPanel = new JPanel(new BorderLayout(0,0));
            bottomPanel.setBackground(new Color(217,217,217));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(1 ,5,7,5));

            messageLabel = new JLabel("", SwingConstants.CENTER);
            messageLabel.setFont(new Font("Arial", Font.ITALIC, 11));

            // Force a minimum height so layout doesn’t collapse
            messageLabel.setPreferredSize(new Dimension(0, 20)); // width = flexible, height = 20px

            //addSubPanels
            bottomPanel.add(messageLabel, BorderLayout.NORTH);
            bottomPanel.add(createAddTaskPanel(),BorderLayout.CENTER);
            bottomPanel.add(createEditDeletePanel(),BorderLayout.SOUTH);

        return bottomPanel;
    }

    // ===== HELPER - addSubPanels =====
    private JPanel createAddTaskPanel(){

        JPanel addTaskPanel = new JPanel(new BorderLayout(0,0));
            addTaskPanel.setBackground(new Color(217, 217, 217));

        taskInput = new JTextField();
            taskInput.setPreferredSize(new Dimension(0, 20));
            styleText(taskInput);

        addButton = new JButton("ADD");
            styleButton(addButton,70);

        addTaskPanel.add(taskInput, BorderLayout.CENTER);
        addTaskPanel.add(addButton, BorderLayout.EAST);

        return addTaskPanel;
    }

    // ===== HELPER - taskInput and taskTable =====
    private void styleText(JComponent component){
        component.setForeground(Color.DARK_GRAY);
        component.setFont(new Font("Arial",Font.PLAIN,13));
        component.setBorder(BorderFactory.createCompoundBorder(
                component.getBorder(),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));
    }

    // ===== HELPER - addSubPanels =====
    private JPanel createEditDeletePanel(){

        JPanel editDeletePanel = new JPanel(new GridLayout(1,2));
            editDeletePanel.setBackground(new Color(217, 217, 217));

        //EditButton
        editButton = new JButton("EDIT TASK");
        styleButton(editButton,100);

        //DeleteButton
        deleteButton = new JButton("DELETE TASK");
        styleButton(deleteButton, 100);

        editDeletePanel.add(editButton);
        editDeletePanel.add(deleteButton);

        return editDeletePanel;
    }

    // ===== HELPER - EditButton & DeleteButton style =====
    private void styleButton(JButton button, int width){
        button.setPreferredSize(new Dimension(width,35));
        button.setBackground(new Color(12, 12, 12));
        button.setForeground(new Color(239, 239, 239));
        button.setFont(new Font("SansSerif",Font.BOLD,13));
    }

    // ========================================= GETTERS =========================================
    public JComboBox<String> getViewComboBox(){ return viewComboBox; }
    public JTable getTaskTable(){ return taskTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTaskInput(){ return taskInput; }
    public JButton getAddButton(){ return addButton; }
    public JButton getEditButton(){ return editButton; }
    public JButton getDeleteButton(){ return deleteButton; }
    public JLabel getMessageLabel(){ return messageLabel; }
}
