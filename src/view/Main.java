package view;

import java.awt.Dimension;

import javax.swing.*; 

public class Main {
    private static void createAndShowGUI() {
    	
        JFrame frame = new JFrame("Trivium");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 100));
        
        JLabel label = new JLabel("Hello World", SwingConstants.CENTER);
        frame.getContentPane().add(label);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> { createAndShowGUI(); });
    }
}