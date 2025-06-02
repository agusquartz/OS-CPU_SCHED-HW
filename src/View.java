
import core.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class View extends JFrame {

    public enum EnumAlgo {
        FCFS, 
        SJF_EXPULSIVO, 
        SJF_NO_EXPULSIVO, 
        PRIORIDAD, 
        ROUND_ROBIN, 
        HRRN,
        NONE
    }

    private List<EnumAlgo> normalEnumAlgos;
    private EnumAlgo[] multiLevelEnumAlgos;
    private int quantum = -1;
    private int[] quantumByLevel;
    private JCheckBox fcfsCheckBox, sjfExpCheckBox, sjfNoExpCheckBox, priorityCheckBox, roundRobinCheckBox, hrrnCheckBox;
    private JTextField roundRobinQuantumField;
    private JComboBox<EnumAlgo>[] levelComboBoxes;
    private JTextField[] levelQuantumFields;
    private LinkedList<BCP> bcps;

    private View(LinkedList<BCP> bcps){
        setTitle("CPU Scheduling - OSHW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        normalEnumAlgos = new ArrayList<>();
        multiLevelEnumAlgos = new EnumAlgo[3];
        quantumByLevel = new int[3];

        // Initialize the multi-level array with NONE and quantumByLevel with -1 (invalid value)
        for (int i = 0; i < 3; i++){
            multiLevelEnumAlgos[i] = EnumAlgo.NONE;
            quantumByLevel[i] = -1;
        }

       
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 0, 10, 10)); 

        // ##### Table on the left #####
        String[] columnNames = {"id", "arrival", "burst", "priority"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        for (BCP b : bcps) {
            Object[] row = {
                b.getProcess().getName(),
                b.getArrival(),
                b.getBurst(),
                b.getPriority()
            };
            tableModel.addRow(row);
        }
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 0));
        mainPanel.add(scrollPane, BorderLayout.WEST);


        // ##### Checkbox Panel (normal selection) #####
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        fcfsCheckBox = new JCheckBox("FCFS");
        sjfExpCheckBox = new JCheckBox("SJF (Expulsivo)");
        sjfNoExpCheckBox = new JCheckBox("SJF (No Expulsivo)");
        priorityCheckBox = new JCheckBox("Prioridad");
        roundRobinCheckBox = new JCheckBox("Round Robin");
        hrrnCheckBox = new JCheckBox("HRRN");

        checkboxPanel.add(fcfsCheckBox);
        checkboxPanel.add(sjfExpCheckBox);
        checkboxPanel.add(sjfNoExpCheckBox);
        checkboxPanel.add(priorityCheckBox);
        checkboxPanel.add(roundRobinCheckBox);
        checkboxPanel.add(hrrnCheckBox);

        JPanel rrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel rrLabel = new JLabel("Quantum:");
        roundRobinQuantumField = new JTextField(5);
        rrPanel.add(rrLabel);
        rrPanel.add(roundRobinQuantumField);
        checkboxPanel.add(rrPanel);

        // ##### Multi-Level Queue Section #####
        JPanel multiLevelPanel = new JPanel();
        multiLevelPanel.setLayout(new BoxLayout(multiLevelPanel, BoxLayout.Y_AXIS));
        multiLevelPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel multiLevelTitle = new JLabel("Cola MultiNivel");
        multiLevelTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        multiLevelPanel.add(multiLevelTitle);

        levelComboBoxes = new JComboBox[3];
        levelQuantumFields = new JTextField[3];

        for (int i = 0; i < 3; i++){
            JPanel levelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            levelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel levelLabel = new JLabel("Nivel " + (i+1) + ":");

            levelComboBoxes[i] = new JComboBox<>(EnumAlgo.values());
            levelComboBoxes[i].setSelectedItem(EnumAlgo.NONE);

            levelQuantumFields[i] = new JTextField(5);
            levelQuantumFields[i].setVisible(false);

            final int index = i;
            levelComboBoxes[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    EnumAlgo selected = (EnumAlgo) levelComboBoxes[index].getSelectedItem();
                    if(selected == EnumAlgo.ROUND_ROBIN){
                        levelQuantumFields[index].setVisible(true);
                    } else{
                        levelQuantumFields[index].setVisible(false);
                    }
                    levelPanel.revalidate();
                    levelPanel.repaint();
                }
            });

            levelPanel.add(levelLabel);
            levelPanel.add(levelComboBoxes[i]);
            levelPanel.add(new JLabel("Quantum:"));
            levelPanel.add(levelQuantumFields[i]);
            multiLevelPanel.add(levelPanel);
        }

        // ##### Execute Button #####
        JButton executeButton = new JButton("Ejecutar");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                normalEnumAlgos.clear();

                System.out.println("Selected Algorithms (Normal):");
                if (fcfsCheckBox.isSelected()) {
                    normalEnumAlgos.add(EnumAlgo.FCFS);
                    System.out.println("FCFS");
                }
                if (sjfExpCheckBox.isSelected()) {
                    normalEnumAlgos.add(EnumAlgo.SJF_EXPULSIVO);
                    System.out.println("SJF (Expulsivo)");
                }
                if (sjfNoExpCheckBox.isSelected()) {
                    normalEnumAlgos.add(EnumAlgo.SJF_NO_EXPULSIVO);
                    System.out.println("SJF (No Expulsivo)");
                }
                if (priorityCheckBox.isSelected()) {
                    normalEnumAlgos.add(EnumAlgo.PRIORIDAD);
                    System.out.println("Prioridad");
                }
                if (roundRobinCheckBox.isSelected()) {
                    String quantumText = roundRobinQuantumField.getText().trim();
                    int quantumValue = parseQuantum(quantumText);
                    if (quantumText.isEmpty() || quantumValue < 1) {
                        System.out.println("Round Robin selected (Quantum not provided or invalid)");
                    } else {
                        normalEnumAlgos.add(EnumAlgo.ROUND_ROBIN);
                        quantum = quantumValue;
                        System.out.println("Round Robin (Quantum: " + quantumText + ")");
                    }
                }
                if (hrrnCheckBox.isSelected()) {
                    normalEnumAlgos.add(EnumAlgo.HRRN);
                    System.out.println("HRRN");
                }

                System.out.println("\nSelected Algorithms in Multi-Level Queue:");
                for (int i = 0; i < 3; i++){
                    EnumAlgo selected = (EnumAlgo) levelComboBoxes[i].getSelectedItem();
                    multiLevelEnumAlgos[i] = selected;

                    // If ROUND_ROBIN is selected, try to read its quantum
                    if(selected == EnumAlgo.ROUND_ROBIN) {
                        String qText = levelQuantumFields[i].getText().trim();
                        int qValue = parseQuantum(qText);
                        if(qText.isEmpty() || qValue < 1) {
                            quantumByLevel[i] = -1;
                            System.out.println("Nivel " + (i+1) + ": Round Robin with invalid or missing Quantum");
                        } else {
                            quantumByLevel[i] = qValue;
                            System.out.println("Nivel " + (i+1) + ": Round Robin (Quantum: " + qText + ")");
                        }
                    } else {
                        // If not RR, keep quantumByLevel as -1 for that level
                        quantumByLevel[i] = -1;
                        System.out.println("Nivel " + (i+1) + ": " + selected);
                    }
                }
                
                Main.programLogic(
                    View.this.getNormalEnumAlgos(),
                    View.this.getMultiLevelEnumAlgos(),
                    View.this.getQuantum(),
                    View.this.getQuantumByLevel());
            }
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(checkboxPanel);
        centerPanel.add(multiLevelPanel);
        centerPanel.add(executeButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        this.setVisible(true);
        // ##### END OF CONSTRUCTOR #####
    }

    // Helper method to parse the quantum and ensure it does not contain letters
    private int parseQuantum(String s) {
        if (s.length() < 10 && !containsLetter(s)) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                return -1;
            }
        }
        return -1;
    }

    // Helper method to check if the string contains any letters
    private boolean containsLetter(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public int getQuantum() {
        return quantum;
    }

    public int[] getQuantumByLevel() {
        return quantumByLevel;
    }

    public List<EnumAlgo> getNormalEnumAlgos() {
        return normalEnumAlgos;
    }

    /**
     * Returns the array of selected algorithms for the multi-level queue.
     * Each position corresponds to a level (index 0: Level 1, index 2: Level 3).
     */
    public EnumAlgo[] getMultiLevelEnumAlgos() {
        return multiLevelEnumAlgos;
    }

    public static View getInstance(LinkedList<BCP> bcps) {
        if (instance == null) {
            instance = new View(bcps);
        }
        return instance;
    }

    private static View instance;
}
