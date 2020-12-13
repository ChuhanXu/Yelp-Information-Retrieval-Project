package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FPGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel panel = new JPanel(new GridBagLayout());
	private JLabel findLabel = new JLabel("Input");
	private JTextField searchTextField = new JTextField(30);
	private JButton searchButton = new JButton("Search");
	
	public FPGUI() {
		// GUI's title 
		super("INFSCI2140 IR Search Panel");
	    GridBagConstraints c = new GridBagConstraints();
	    c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 10, 10, 10);
        
        // 1 row, 1 col
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(findLabel, c);
        
        // 1 row, 2 col
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(searchTextField, c);
        
        c.gridx = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(searchButton, c);
        
        this.add(panel);
        
        /*
         * Not specify or (don’t know) the exact size of the frame
         * Let the frame resizes itself in a manner which ensures 
         * all its subcomponents have their preferred sizes.
         */
        this.pack();
		
        // Center the frame on screen
        setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		 // It is recommended to instantiate and showing the frame in the Swing event dispatcher thread (EDT) like this:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FPGUI frame = new FPGUI();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                /*
                 * Not specify or (don’t know) the exact size of the frame
                 * Let the frame resizes itself in a manner which ensures 
                 * all its subcomponents have their preferred sizes.
                 */
                frame.pack();
                frame.setVisible(true);
            }
        });
	}
}
