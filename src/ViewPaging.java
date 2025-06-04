import core.BCP;
import core.Process;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
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
import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ViewPaging extends JFrame {

    private LinkedList<BCP> bcps;
    private static ViewPaging instance;

    private ViewPaging(LinkedList<BCP> bcps) {
        this.bcps = bcps;
        
        setTitle("Algoritmos de Paginación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ##### Table on the left #####
        String[] columnNames = {"proceso", "llegada", "rafagas", "paginas"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        // Load data from BCP list
        for (BCP bcp : bcps) {
            Object[] row = {
                bcp.getProcess().getName(),
                bcp.getArrival(),
                bcp.getBurst(),
                bcp.getProcess().getPages()
            };
            tableModel.addRow(row);
        }
        
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350, 0));
        mainPanel.add(scrollPane, BorderLayout.WEST);

        // ##### Right Panel with Algorithms #####
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Algoritmos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(titleLabel);

        // Add some space
        rightPanel.add(javax.swing.Box.createVerticalStrut(20));

        // CPU Scheduling Section
        JLabel cpuLabel = new JLabel("CPU Scheduling");
        cpuLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cpuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(cpuLabel);

        JLabel roundRobinLabel = new JLabel("• Round Robin: Q = 4");
        roundRobinLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        roundRobinLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(roundRobinLabel);

        // Add some space
        rightPanel.add(javax.swing.Box.createVerticalStrut(15));

        // Page Replacement Section
        JLabel pageLabel = new JLabel("Reemplazo de Página");
        pageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        pageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(pageLabel);

        JLabel fcfsLabel = new JLabel("• FCFS 2nd Chance");
        fcfsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        fcfsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(fcfsLabel);

        JLabel lruLabel = new JLabel("• LRU");
        lruLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        lruLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(lruLabel);

        // Add flexible space to push button to bottom
        rightPanel.add(javax.swing.Box.createVerticalGlue());

        // RUN Button
        JButton runButton = new JButton("RUN");
        runButton.setBackground(new java.awt.Color(76, 175, 80)); // Green color
        runButton.setForeground(java.awt.Color.WHITE);
        runButton.setFont(new Font("Arial", Font.BOLD, 12));
        runButton.setPreferredSize(new Dimension(100, 35));
        runButton.setMaximumSize(new Dimension(100, 35));
        runButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("RUN button clicked - Execute Round Robin with quantum 4");
                
                // Create algorithm list with only Round Robin
                List<EnumAlgo> normalAlgos = new ArrayList<>();
                normalAlgos.add(EnumAlgo.ROUND_ROBIN);
                
                // Set up multi-level array (empty for this case)
                EnumAlgo[] multiLevelAlgos = new EnumAlgo[3];
                for (int i = 0; i < 3; i++) {
                    multiLevelAlgos[i] = EnumAlgo.NONE;
                }
                
                // Set quantum to 4
                int quantum = 4;
                int[] quantumByLevel = {-1, -1, -1};
                
                // Call Main.programLogic with Round Robin configuration
                Main.programLogic(normalAlgos, multiLevelAlgos, quantum, quantumByLevel);
            }
        });
        
        rightPanel.add(runButton);

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        this.setVisible(true);
    }

    public static ViewPaging getInstance(LinkedList<BCP> bcps) {
        if (instance == null) {
            instance = new ViewPaging(bcps);
        }
        return instance;
    }

    public LinkedList<BCP> getBcps() {
        return bcps;
    }
    
    public enum EnumAlgo {
        FCFS, 
        SJF_EXPULSIVO, 
        SJF_NO_EXPULSIVO, 
        PRIORIDAD, 
        ROUND_ROBIN, 
        HRRN,
        NONE
    }
}