package it.lettoreSeriale.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import it.lettoreSeriale.DTO.SondaDTO;
import it.lettoreSeriale.bo.GestioneSonda;
import it.lettoreSeriale.bo.PortReader;
import it.lettoreSeriale.bo.Serial;
import jssc.SerialPort;
import jssc.SerialPortException;
import java.awt.Font;

public class DASM_GUI extends JFrame {
	
	JFrame g=null;
	JPanel initPanel;
	JScrollPane scrollPane;
	PortReader portReader;
	jssc.SerialPort serialPort;
	
	
	public static HashMap<Long, String> msgsOrd = new HashMap<Long, String>();
	public ArrayList<SondaDTO> listaSonde= new ArrayList<SondaDTO>();
	private JTextField txtCom;
	private JTextField textField_1;
	
	public DASM_GUI()
	{
	
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		setTitle("DASM Monitor\u00AE");
		setSize(300,650);
		setResizable(false);
		g=this;
		
	
			costruisciFrame();
			
			addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	                if(serialPort!=null && serialPort.isOpened()) 
	                {
	                	try {
	                		System.out.println("Disconnect");
							serialPort.closePort();
						} catch (SerialPortException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                }
	                e.getWindow().dispose();
	            }
	        });
	}
	

	private void costruisciFrame() {
		
		initPanel = new JPanel();
		initPanel.setSize(300,650);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - initPanel.getWidth()) / 2;
		int y = (dim.height - initPanel.getHeight()) / 2;
		setLocation(x, y);
		
		initPanel.setLayout(null);
		
		scrollPane = new JScrollPane(initPanel);
		scrollPane.setSize(new Dimension(240,  630));
		scrollPane.setBackground(Color.red);
	
		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		btnConnect.setBounds(105, 50, 90, 25);
		initPanel.add(btnConnect);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font("Arial", Font.BOLD, 14));
		lblPort.setBounds(10, 22, 54, 14);
		initPanel.add(lblPort);
		
		txtCom = new JTextField();
		txtCom.setFont(new Font("Arial", Font.PLAIN, 14));
		txtCom.setText("COM");
		txtCom.setBounds(47, 19, 62, 20);
		initPanel.add(txtCom);
		txtCom.setColumns(10);
		
		JLabel lblRate = new JLabel("Rate");
		lblRate.setFont(new Font("Arial", Font.BOLD, 14));
		lblRate.setBounds(154, 22, 41, 14);
		initPanel.add(lblRate);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Arial", Font.PLAIN, 14));
		textField_1.setText("115200");
		textField_1.setBounds(191, 19, 80, 20);
		initPanel.add(textField_1);
		textField_1.setColumns(10);
		
		
		btnConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				int rate=0;
				try
				{
					
					rate=Integer.parseInt(textField_1.getText());
				}catch (Exception e) {
					
					JOptionPane.showMessageDialog(null, "Il campo Rate accetta solo numeri");
					return;
				}
				

				try
				{
				   serialPort =Serial.getConnection(txtCom.getText(), rate);
				   
				   portReader = new PortReader(serialPort);

				   serialPort.addEventListener(portReader, SerialPort.MASK_RXCHAR);
				   

				   Thread.sleep(1000);

				  listaSonde=GestioneSonda.getListaSonde(portReader);
				}
		
				
				catch (Exception e) 
				{
					JOptionPane.showMessageDialog(null, "Porta Inesistente","Errore",JOptionPane.ERROR_MESSAGE);
					System.out.println(e.getMessage());
				}
				int index=100;
				
				for (Iterator iterator = listaSonde.iterator(); iterator.hasNext();) 
				{
					SondaDTO sonda = (SondaDTO) iterator.next();
					
					JButton btnSnd= new JButton("["+sonda.getId_sonda()+"] "+sonda.getLabel());
					btnSnd.setBounds(10, index, 280,30);
					btnSnd.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
					index=index+40;
					
					
					btnSnd.addActionListener(new ActionListener() {
					
						@Override
						public void actionPerformed(final ActionEvent e) {	
						
								    	  String command = ((JButton) e.getSource()).getActionCommand();
											
											String pv=command.substring(1,command.indexOf("]"));
										
											SondaDTO sonda=getSonda(listaSonde,pv);
											 
											GuiWorker1 d =	 new GuiWorker1(sonda,portReader);
											d.execute();
											
									
											 
		
						}

						private SondaDTO getSonda(ArrayList<SondaDTO> listaSonde, String pv) {
							
							for (int i = 0; i < listaSonde.size(); i++) {
								
								if(listaSonde.get(i).getId_sonda().equalsIgnoreCase(pv))
								{
									return listaSonde.get(i);
								}
							}
							return null;
						}

					});
					
					initPanel.add(btnSnd);
				}
				
				initPanel.update(g.getGraphics());
				g.update(g.getGraphics());
					
			}
		});	
		
		g.getContentPane().add(scrollPane);
	}
}
