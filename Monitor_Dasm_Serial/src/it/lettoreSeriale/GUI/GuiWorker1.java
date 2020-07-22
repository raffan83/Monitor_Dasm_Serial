package it.lettoreSeriale.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import it.lettoreSeriale.DTO.SondaDTO;
import it.lettoreSeriale.bo.GestioneSonda;
import it.lettoreSeriale.bo.PortReader;
import jssc.SerialPortException;
import net.miginfocom.swing.MigLayout;


class GuiWorker1 extends SwingWorker<Integer, Integer>{

	/*
	 * This should just create a frame that will hold a progress bar until the
	 * work is done. Once done, it should remove the progress bar from the dialog
	 * and add a label saying the task complete.
	 */

	private JFrame g = new JFrame();
	private  JLabel lab;
	public static JSlider slider1;
	DefaultValueDataset dataset1;
	GuiWorker1 worker=null;
	SondaDTO sonda=null;
	PortReader portReader;
	JButton transfer;
	JLabel tf;
	private JButton btnZero;
	private double zero;
	private boolean write=false;
	
	Timer t=null;
	int secondTimer=0;
	
	int precision;
	static int delay;
	static int media;
	static int bufferMedia;

	ArrayList<BigDecimal> listaValori;
	private JButton btnSetting;
	private JButton btn_record;
	private JButton btnStop;
	private JTextField textTime;
	private File dataLogger;
	FileOutputStream fos;
	PrintStream ps;

	public GuiWorker1(SondaDTO s, PortReader _portReader) {


		/*Metodo calcolo valore*/  
		media=1;
		bufferMedia=10;
		listaValori= new ArrayList<>();

		worker=this;  
		sonda=s;
		portReader=_portReader;
		lab= new JLabel("Temp");

		delay=800;
		final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		zero=0;
		precision=0;

		tf= new JLabel();
		g.getContentPane().setLayout(new BorderLayout());

		dataset1 = new DefaultValueDataset(0D);

		DialPlot dialplot = new DialPlot();

		dialplot.setView(0.00D, 0.00D, 1.00D, 1.00D);
		dialplot.setDataset(0, dataset1);


		StandardDialFrame standarddialframe = new StandardDialFrame();
		standarddialframe.setBackgroundPaint(Color.lightGray);
		standarddialframe.setForegroundPaint(Color.darkGray);
		dialplot.setDialFrame(standarddialframe);

		GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(0, 170, 220));
		DialBackground dialbackground = new DialBackground(gradientpaint);

		dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
		dialplot.setBackground(dialbackground);

		DialTextAnnotation dialtextannotation = new DialTextAnnotation(s.getUm() +" "+s.getLabel());
		dialtextannotation.setFont(new Font("Dialog", 1, 12));
		dialtextannotation.setRadius(0.69999999999999996D);
		dialplot.addLayer(dialtextannotation);

		DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
		dialvalueindicator.setFont(new Font("Dialog", 0, 14));
		dialvalueindicator.setOutlinePaint(Color.GREEN);
		dialvalueindicator.setRadius(0.59999999999999998D);
		dialvalueindicator.setAngle(-90D);
		NumberFormat df=NumberFormat.getNumberInstance();

		precision=s.getPrecision().intValue();
		dialvalueindicator.setNumberFormat(df);

		double maxScale=s.getMaxScale().doubleValue();

		BigDecimal perc =(new BigDecimal(maxScale).subtract(s.getMinScale()).divide(new BigDecimal(20)).setScale(2,RoundingMode.HALF_UP));


		StandardDialScale standarddialscale = new StandardDialScale(s.getMinScale().doubleValue(),maxScale, -120D, -300D,perc.doubleValue(), 0);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.14999999999999999D);
		standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
		dialplot.addScale(0, standarddialscale);


		/*/
		 * Quadrante rosso
		 */
		StandardDialScale standarddialscale1 = new StandardDialScale(s.getMinScale().doubleValue(), maxScale, -120D, -300D, perc.doubleValue(), 4);
		standarddialscale1.setTickRadius(0.5D);
		standarddialscale1.setTickLabelOffset(0.14999999999999999D);
		standarddialscale1.setTickLabelFont(new Font("Dialog", 0, 10));
		standarddialscale1.setMajorTickPaint(Color.red);
		standarddialscale1.setMinorTickPaint(Color.red);
		dialplot.addScale(1, standarddialscale1);

		dialplot.mapDatasetToScale(1, 1);
		org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer(0);
		dialplot.addPointer(pointer);

		DialCap dialcap = new DialCap();
		dialcap.setRadius(0.10000000000000001D);
		dialplot.setCap(dialcap);



		JFreeChart jfreechart = new JFreeChart(dialplot);
		jfreechart.setTitle(s.getId_sonda());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setPreferredSize(new Dimension(400, 400));
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new MigLayout("", "[150px][100px][150px,grow]", "[23px][23px][23px]"));
		slider1 = new JSlider(s.getMinScale().intValue(), s.getMaxScale().intValue());
		slider1.setMajorTickSpacing(1);
		slider1.setPaintTicks(true);
		slider1.setPaintLabels(true);
		slider1.addChangeListener(new BoundedChangeListener());

		tf.setFont(new Font("Dialog", 0, 30));
		tf.setHorizontalAlignment(SwingConstants.CENTER);
		tf.setVerticalAlignment(SwingConstants.CENTER);

		Border border = BorderFactory.createLineBorder(Color.red, 2);
		tf.setBorder(border);
		jpanel.add(tf, "cell 1 0,grow");



		g.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try {
					Thread.sleep(50);
					worker.cancel(true);
					g.dispose();
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					//	e.printStackTrace();
				}

			}
		});

		g.getContentPane().add(chartpanel);      
		g.getContentPane().add(jpanel,"South");
		transfer = new JButton("TRANS");
		jpanel.add(transfer, "flowx,cell 0 0,alignx center,growy");

		btnZero = new JButton("ZERO");
		btnZero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while(true)
				{
					Double d;
					try {
						d = GestioneSonda.getValue(sonda,portReader);
						if(d!=null)
						{
							zero=d;
							break;
						}
					} catch (SerialPortException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					

				}
			}
		});

		//  if(s.isZero())
			//  {
			jpanel.add(btnZero, "cell 2 0,alignx center,growy");

			//  }    

			btnSetting = new JButton("Setting");
			btnSetting.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) 
				{
				     SwingUtilities.invokeLater(new Runnable() {
				            public void run() 
				            
				            {
				               
				            	 JFrame frame = new JFrame("Password");
				                 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				         				  
					         	
				               
					    		
				                 //Create and set up the content pane.
				                 final Password newContentPane = new Password(frame);
				                 newContentPane.setOpaque(true); //content panes must be opaque
				                 frame.setContentPane(newContentPane);
				          
				                 Point d=g.getLocation(); 
						         	
					                int x = ( g.getWidth()  - 300) / 2;
						    		int y = ( g.getHeight()  - 100) / 2;
						    		
						    		x=x+(int)d.getX();
						    		y=y+(int)d.getY();
						    		
						    		frame.setLocation(x, y);
				
				                 
				                 //Make sure the focus goes to the right component
				                 //whenever the frame is initially given the focus.
				                 frame.addWindowListener(new WindowAdapter() {
				                     public void windowActivated(WindowEvent e) {
				                         newContentPane.resetFocus();
				                     }
				                 });
				          
				                 //Display the window.
				                 frame.pack();
				                 frame.setVisible(true);
				            }
				        });
				    }
			});
			
			final ActionListener time;
			time = new ActionListener() {

			    @Override
			    public void actionPerformed(ActionEvent evt) {
			        textTime.setText(getTimerTime()); 
			        
			    }

				
			};
			
			btn_record = new JButton("Rec");
			
			btn_record.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					t=new Timer(1000, time);
					t.start();
					write=true;
					
					
					
					try {
						
						dataLogger= new File("dataLogger_"+sonda.getId_sonda()+"_"+sdf.format(new Date())+".csv");
						fos= new FileOutputStream(dataLogger);
						ps= new PrintStream(fos);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			
			

			jpanel.add(btn_record, "flowx,cell 0 1,alignx right");
			
			btnStop = new JButton("Stop");
			
			btnStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					t.stop();
					write=false;
					try 
					{
						fos.close();
						ps.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			jpanel.add(btnStop, "cell 2 1");

			jpanel.add(btnSetting, "cell 1 2,alignx center,growy");
			
			textTime = new JTextField();
			textTime.setEditable(false);
			textTime.setText("00:00:00");
			jpanel.add(textTime, "cell 1 1,growx");
			textTime.setColumns(10);

			transfer.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					Double value;
					try {
						value = GestioneSonda.getValue(sonda,portReader);
						System.out.println(value);
					} catch (SerialPortException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
				}
			});

			g.pack();
			g.setVisible(true);
	}

	@Override
	protected  Integer doInBackground() throws Exception {
		System.out.println( "GuiWorker.doInBackground" );

		try{

			while(true)
			{	
				Double value=GestioneSonda.getValue(sonda,portReader);

				if(value!=null)	
				{

					System.out.println(value);
					if(media==0)
					{
						BigDecimal valueFormat=new BigDecimal(value-zero).setScale(precision,RoundingMode.HALF_UP);
						dataset1.setValue(valueFormat);
						tf.setText(""+valueFormat.toPlainString());
					}
					if(media==1 || media==2)
					{

						BigDecimal valoreMediano=getMedia(value-zero);
						dataset1.setValue(valoreMediano);
						tf.setText(""+valoreMediano.toPlainString());

					}
					if(write)
					{
						SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						if(textTime.getText().length()==0)
						{
							System.out.println("sleep");
							Thread.sleep(5);
							System.out.println(textTime.getText());
						}
						ps.println(sdf.format(new Date())+";"+textTime.getText()+";"+tf.getText().replaceAll("\\.",","));
					}
				}

				Thread.sleep(delay);
			}
		}
		catch (InterruptedException iex) {
		
		}
		catch (Exception ie) {
			ie.printStackTrace();
		}
		return 0;
	}


	private BigDecimal getMedia(double d) 
	{
		listaValori.add(new BigDecimal(d));

		if(listaValori.size()<bufferMedia)
		{
			return new BigDecimal(d).setScale(precision,RoundingMode.HALF_UP);
		}else
		{

			BigDecimal toReturn =BigDecimal.ZERO;

			for (int i = 0; i < bufferMedia; i++) 
			{
				toReturn=toReturn.add(listaValori.get(i));
			}

			swapArry(media);

			return toReturn.divide(new BigDecimal(bufferMedia),precision,RoundingMode.HALF_UP);
		}
	}
	private void swapArry(int method) {

		ArrayList<BigDecimal> newArray= new ArrayList<>();
		if(method==2)
		{	
			listaValori=newArray;

		}else
		{
			for (int i = 0; i < bufferMedia-1; i++) 
			{
				newArray.add(listaValori.get(i+1));
			}
			listaValori=newArray;
		}
	}


	@Override
	protected void done() {
		System.out.println("done");
		JLabel label = new JLabel("Task Complete");
	}


	class BoundedChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent changeEvent) {
			Object source = changeEvent.getSource();
			if (source instanceof BoundedRangeModel) {
				BoundedRangeModel aModel = (BoundedRangeModel) source;
				if (!aModel.getValueIsAdjusting()) {
					System.out.println("Changed: " + aModel.getValue());
				}
			} else if (source instanceof JSlider) {
				JSlider theJSlider = (JSlider) source;
				if (!theJSlider.getValueIsAdjusting()) {
					System.out.println("Slider changed: " + theJSlider.getValue());
				}
			} else {
				System.out.println("Something changed: " + source);
			}
		}
	}


	private String getTimerTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		String value="00:00:00";
		try {
			Date d =sdf.parse("00:00:00");
			
		  
		    Calendar gcal = new GregorianCalendar();
		    gcal.setTime(d);
		    gcal.add(Calendar.SECOND, secondTimer);
		    secondTimer++;
		    return sdf.format(gcal.getTime());
		    
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
}
