package it.lettoreSeriale.GUI;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

public class Password extends JPanel implements ActionListener {

	private static String OK = "ok";

	private JFrame controllingFrame; //needed for dialogs
	private JPasswordField passwordField;

	
	public Password(JFrame f) {
		//Use the default FlowLayout.
		controllingFrame = f;
		
		//Create everything.
		passwordField = new JPasswordField(10);
		passwordField.setActionCommand(OK);
		passwordField.addActionListener(this);

		JLabel label = new JLabel("Password: ");
		label.setLabelFor(passwordField);

		JComponent buttonPane = createButtonPanel();

		//Lay out everything.
		JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		textPane.add(label);
		textPane.add(passwordField);

		add(textPane);
		add(buttonPane);
	}

	protected JComponent createButtonPanel() {
		JPanel p = new JPanel(new GridLayout(0,1));
		JButton okButton = new JButton("OK");
		okButton.setActionCommand(OK);
		okButton.addActionListener(this);
		p.add(okButton);
		return p;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (OK.equals(cmd)) { //Process the password.
			char[] input = passwordField.getPassword();
			if (isPasswordCorrect(input)) 
			{
				SwingUtilities.invokeLater(new Runnable() {
		            public void run() 
		            
		            { 
		            	 JFrame frameCfg = new JFrame("Configurazione Sonda");
		            	 frameCfg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		                 
		            	 final PanelCfg newContentPane = new PanelCfg(frameCfg);
		                 newContentPane.setOpaque(true); //content panes must be opaque
		                 frameCfg.setContentPane(newContentPane);
		                 
		                 
		                 frameCfg.pack();
		                 frameCfg.setVisible(true);
		            	
		            }
		        });
				controllingFrame.dispose();
			
			
			} else {
				JOptionPane.showMessageDialog(controllingFrame,
						"Password Errata",
						"Error Message",
						JOptionPane.ERROR_MESSAGE);
			}

			//Zero out the possible password, for security.
			Arrays.fill(input, '0');

			passwordField.selectAll();
			resetFocus();
		}

	}

	void resetFocus() {
		passwordField.requestFocusInWindow();
	}

	/**
	 * Checks the passed-in array against the correct password.
	 * After this method returns, you should invoke eraseArray
	 * on the passed-in array.
	 */
	private static boolean isPasswordCorrect(char[] input) {
		boolean isCorrect = true;
		char[] correctPassword = { '1', '2', '3', '4' };

		if (input.length != correctPassword.length) {
			isCorrect = false;
		} else {
			isCorrect = Arrays.equals (input, correctPassword);
		}

		//Zero out the password.
		Arrays.fill(correctPassword,'0');

		return isCorrect;
	}

}
