package it.lettoreSeriale.bo;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.print.PageLayout;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

public class PortReader implements SerialPortEventListener {

	SerialPort serialPort;
	String canale;
	//	private  ArrayList<String> lista;
	public static  String msg;


	public PortReader(SerialPort serialPort) {
		this.serialPort = serialPort;
		//	lista = new ArrayList<String>();

	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		//		if (event.isRXCHAR() && event.getEventValue() > 0) {
		//				
		//				try {
		//					
		//					synchronized (this){
		//						 byte[] buffer = serialPort.readBytes(1024);
		//					     String s=new String(buffer);
		//					     
		//					     String[] parts= s.split("\\r\\n");
		//						 for (int i = 0; i < parts.length; i++) 
		//						 {
		//							 lista.add(parts[i]);		
		//						 }
		//					}		
		////				
		//			} catch (Exception ex) {
		//				System.out.println("Error in receiving string from COM-port: " + ex);
		//			}
		//		
		//		}
		String playload="";

		byte[] by= new byte[1];
		while(true){
			try {             
				by = serialPort.readBytes(1);

				if(by[0]!=10 && by[0]!=13)
				{                         

					playload+=(char)by[0];
				}
				else
				{                       
					if(by[0]==13)
					{
						msg=playload;
				//		System.out.println("Mess: "+msg);
						break;
					}     
				}

			}catch (Exception ex) {
				ex.printStackTrace();
			}                           
		}
	}
	public  String getMessages() {
		synchronized (this){

			return msg;
		}
	}
}

