import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatePicker extends JDialog {
    private final JDateChooser startDateChooser;
    private final JDateChooser endDateChooser;

    public DatePicker(JFrame parent) {
        super(parent, "Choose Two Dates To Search Between", true);
        setLayout(new BorderLayout());

        JLabel startDateLabel = new JLabel("First Date:");
        JLabel endDateLabel = new JLabel("Second Date:");


        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        startDateChooser.setPreferredSize(new java.awt.Dimension(150, 30));
        endDateChooser.setPreferredSize(new java.awt.Dimension(150, 30));

      startDateChooser.getDateEditor().getUiComponent().setEnabled(false);
      endDateChooser.getDateEditor().getUiComponent().setEnabled(false);

        JPanel startDatePanel = new JPanel(new BorderLayout());
        startDatePanel.add(startDateLabel, BorderLayout.WEST);

        startDatePanel.add(startDateChooser, BorderLayout.EAST);

        JPanel endDatePanel = new JPanel(new BorderLayout());
        endDatePanel.add(endDateLabel, BorderLayout.WEST);

        endDatePanel.add(endDateChooser, BorderLayout.EAST);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(startDatePanel, BorderLayout.NORTH);
        add(endDatePanel, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public JDateChooser getStartDateChooser() {
        return startDateChooser;
    }

    public JDateChooser getEndDateChooser() {
        return endDateChooser;
    }
}