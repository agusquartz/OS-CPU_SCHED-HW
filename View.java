import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class View extends JFrame {

    public static View getInstance() {
        if (instance == null) {
            instance = new View();
        }
        return instance;
    }



    private View(){
        setTitle("CPU Scheduling - OSHW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        //JPanel checkboxPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JPanel checkboxPanel = new JPanel();

        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JCheckBox fcfsCheckBox = new JCheckBox("FCFS");
        final JCheckBox sjfExpCheckBox = new JCheckBox("SJF (Expulsivo)");
        final JCheckBox sjfNoExpCheckBox = new JCheckBox("SJF (No Expulsivo)");
        final JCheckBox priorityCheckBox = new JCheckBox("Prioridad");

        checkboxPanel.add(fcfsCheckBox);
        checkboxPanel.add(sjfExpCheckBox);
        checkboxPanel.add(sjfNoExpCheckBox);
        checkboxPanel.add(priorityCheckBox);

        JPanel roundRobinPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        final JCheckBox roundRobinCheckBox = new JCheckBox("Round Robin");
        roundRobinPanel.add(roundRobinCheckBox);

        JLabel quantumLabel = new JLabel("Quantum:");
        JTextField quantumField = new JTextField(5);
        roundRobinPanel.add(quantumLabel);
        roundRobinPanel.add(quantumField);

        checkboxPanel.add(roundRobinPanel);

        final JCheckBox hrrnCheckBox = new JCheckBox("HRRN");
        checkboxPanel.add(hrrnCheckBox);

        JButton executeButton = new JButton("Ejecutar");

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Collect selected options
                System.out.println("Selected options:");
                if (fcfsCheckBox.isSelected()) {
                    System.out.println("FCFS");
                }
                if (sjfExpCheckBox.isSelected()) {
                    System.out.println("SJF (Expulsivo)");
                }
                if (sjfNoExpCheckBox.isSelected()) {
                    System.out.println("SJF (No Expulsivo)");
                }
                if (priorityCheckBox.isSelected()) {
                    System.out.println("Prioridad");
                }
                if (roundRobinCheckBox.isSelected()) {
                    String quantumText = quantumField.getText().trim();
                    int intSize;

                    if (quantumText.length() < 10 && !containsLetter(quantumText)){
                        intSize = Integer.parseInt(quantumText);
                    } 
                    else if (containsLetter(quantumText)) {
                        System.out.println("Anina nde ñembotavy");
                        intSize = -1;
                    } else{
                        intSize = -1;
                    }

                    if (quantumText.isEmpty() || intSize < 1) {
                        System.out.println("Round Robin selected (Quantum not provided)");
                    } else {
                        System.out.println("Round Robin (Quantum: " + quantumText + ")");
                    }

                }
                if (hrrnCheckBox.isSelected()) {
                    System.out.println("HRRN");
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(checkboxPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(executeButton);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        this.setVisible(true);
    }

    private boolean containsLetter(String s) {
    for (int i = 0; i < s.length(); i++) {
        if (Character.isLetter(s.charAt(i))) {
            return true;
        }
    }
    return false;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View.getInstance();
        });
    }

    private static View instance;
}