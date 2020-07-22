package it.lettoreSeriale.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.NumericShaper;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import javax.swing.JSlider;

public class PanelCfg extends JPanel {
	private JFrame controllingFrame;
	private JTextField textField_delay;
	
	public PanelCfg(JFrame frameCfg) {
	
		controllingFrame=frameCfg;
		setSize(400, 180);
		setLayout(new MigLayout("", "[grow][][grow]", "[][][][][][][]"));
		
		JLabel lblDelay = new JLabel("Delay");
		add(lblDelay, "cell 0 1,alignx trailing");
		
		textField_delay = new JTextField();
		add(textField_delay, "cell 1 1");
		textField_delay.setColumns(10);
		
		JLabel lblCalcoloValoreSonda = new JLabel("Calcolo valore Sonda");
		add(lblCalcoloValoreSonda, "cell 0 2,alignx right");
		
		final JRadioButton rd_dir = new JRadioButton("Diretto");
		add(rd_dir, "flowx,cell 1 2");
		
		final JRadioButton rd_inst = new JRadioButton("Istantaneo");
		add(rd_inst, "cell 1 3");
		
		final JRadioButton rd_riemp = new JRadioButton("Riempimento");
		add(rd_riemp, "cell 1 4");
		
		JLabel lblBufferMedia = new JLabel("Buffer Media");
		add(lblBufferMedia, "cell 0 5,alignx trailing");
		
		JLabel lblMs = new JLabel("ms");
		add(lblMs, "cell 1 1");
		
		textField_delay.setText(""+GuiWorker1.delay);
		
		final JSlider slider = new JSlider(JSlider.HORIZONTAL,1,10,2);
		add(slider, "cell 1 5");
		slider.setMajorTickSpacing(1);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(GuiWorker1.bufferMedia);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rd_dir);
		group.add(rd_inst);
		group.add(rd_riemp);
		
		if(GuiWorker1.media==0){rd_dir.setSelected(true);}
		if(GuiWorker1.media==1){rd_inst.setSelected(true);}
		if(GuiWorker1.media==2){rd_riemp.setSelected(true);}
		
		JButton btnSet = new JButton("Configura");
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				try 
				{
					GuiWorker1.delay=Integer.parseInt(textField_delay.getText());
					GuiWorker1.bufferMedia=slider.getValue();
					if(rd_dir.isSelected())
					{
						GuiWorker1.media=0;
					}
					if(rd_inst.isSelected())
					{
						GuiWorker1.media=1;
					}
					if(rd_riemp.isSelected())
					{
						GuiWorker1.media=2;
					}
				} 
				catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "I campi delay e buffer accettano solo numeri","Errore",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		add(btnSet, "cell 2 5,alignx center");
		
	}

}
